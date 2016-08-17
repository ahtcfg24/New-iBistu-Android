package org.iflab.ibistubydreamfactory.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.BarrelDistortionConfig;

import org.iflab.ibistubydreamfactory.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.iamding.md360player4android.MediaPlayerWrapper;
import cn.iamding.md360player4android.utils.StringUtil;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * VR视频播放器
 */
public class VRVideoPlayerActivity extends VRPlayerActivity {
    private SeekBar videoProgressSeekBar;
    private ImageButton playOrPauseButton;
    private TextView currentTimeTextView, durationTimeTextView;
    private int currentTime, durationTime;
    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    private TimerTask timeTask;

    private Handler handler = new Handler();//用于在异步线程中更新播放时间
    private Runnable updateThread = new Runnable() {//异步线程
        public void run() {
            currentTimeTextView.setText(StringUtil.transToTime(currentTime));
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                cancelBusy();//隐藏progressBar
                playOrPauseButton.setImageResource(R.drawable.ic_pause_button);
                durationTime = (int) (mMediaPlayerWrapper.getPlayer().getDuration() / 1000);
                videoProgressSeekBar.setMax(durationTime);
                durationTimeTextView.setText(StringUtil.transToTime(durationTime));

                timeTask = new TimerTask() {//更新进度条
                    @Override
                    public void run() {
                        currentTime = (int) mMediaPlayerWrapper.getPlayer()
                                                               .getCurrentPosition() / 1000;
                        videoProgressSeekBar.setProgress(currentTime);
                        handler.post(updateThread);

                    }
                };
                new Timer().schedule(timeTask, 0, 300);
            }
        });

        mMediaPlayerWrapper.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                Toast.makeText(VRVideoPlayerActivity.this, "播放失败：" + what + "，extra=" + extra, Toast.LENGTH_SHORT)
                     .show();
                return true;
            }
        });

        mMediaPlayerWrapper.getPlayer()
                           .setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
                               @Override
                               public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                                   getVRLibrary().onTextureResize(width, height);
                               }
                           });

        mMediaPlayerWrapper.getPlayer().setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                mMediaPlayerWrapper.getPlayer().seekTo(0);
                playOrPauseButton.setImageResource(R.drawable.ic_play_button);
            }
        });

        Uri uri = getUri();
        //加载URL视频
        if (uri != null) {
            mMediaPlayerWrapper.openRemoteFile(uri.toString());
            mMediaPlayerWrapper.prepare();
        }

        findViewById(R.id.playControlBar).setVisibility(View.VISIBLE);
        currentTimeTextView = (TextView) findViewById(R.id.text_currentTime);
        durationTimeTextView = (TextView) findViewById(R.id.text_durationTime);
        videoProgressSeekBar = (SeekBar) findViewById(R.id.seekBar_videoProgress);
        videoProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             *
             * @param seekBar
             * @param progress 当前进度
             * @param fromUser 是否用户触发
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayerWrapper.getPlayer().seekTo(progress * 1000);
                }
            }

            /**
             * 开始拖动时
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * 停止拖动时
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        playOrPauseButton = (ImageButton) findViewById(R.id.imageButton_playOrPause);
        playOrPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayerWrapper.getPlayer().isPlaying()) {
                    mMediaPlayerWrapper.getPlayer().pause();
                    playOrPauseButton.setImageResource(R.drawable.ic_play_button);
                } else {
                    mMediaPlayerWrapper.getPlayer().start();
                    playOrPauseButton.setImageResource(R.drawable.ic_pause_button);
                }

            }
        });


    }

    /**
     * 初始化VR播放器
     */
    @Override
    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                          .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                          .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH)//默认即可触摸也可感应
                          .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                              @Override
                              public void onSurfaceReady(Surface surface) {
                                  mMediaPlayerWrapper.getPlayer().setSurface(surface);
                              }
                          })
                          .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                              @Override
                              public void onNotSupport(int mode) {
                                  String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION ? "设备不支持感应模式，请滑动视频控制视角" : "设备不支持感应模式，请滑动视频控制视角";
                                  Toast.makeText(VRVideoPlayerActivity.this, tip, Toast.LENGTH_SHORT)
                                       .show();
                              }
                          })
                          .pinchEnabled(true)
                          .barrelDistortionConfig(new BarrelDistortionConfig().setDefaultEnabled(false)
                                                                              .setScale(0.95f))
                          .build(R.id.gl_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeTask.cancel();
        mMediaPlayerWrapper.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayerWrapper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayerWrapper.onResume();
    }

}
