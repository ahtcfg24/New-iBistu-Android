package org.iflab.ibistubydreamfactory.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.MediaPlayerWrapper;
import org.iflab.ibistubydreamfactory.utils.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * 普通播放器
 */
public class NormalPlayerActivity extends Activity implements TextureView.SurfaceTextureListener {
    private ImageButton playOrPauseButton;
    private SeekBar videoProgressSeekBar;
    private TimerTask timeTask;
    private TextView currentTimeTextView, durationTimeTextView;
    private int currentTime, durationTime;

    private Handler handler = new Handler();//用于在异步线程中更新播放时间
    private Runnable updateThread = new Runnable() {//异步线程
        public void run() {
            currentTimeTextView.setText(StringUtil.transToTime(currentTime));
        }
    };
    private Surface surface;

    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();

    public static void start(Context context, Uri uri) {
        Intent i = new Intent(context, NormalPlayerActivity.class);
        i.setData(uri);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_normal_player);

        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                cancelBusy();
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

        mMediaPlayerWrapper.getPlayer()
                           .setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                               @Override
                               public void onCompletion(IMediaPlayer mp) {
                                   mMediaPlayerWrapper.getPlayer().seekTo(0);
                                   playOrPauseButton.setImageResource(R.drawable.ic_play_button);
                               }
                           });

        TextureView textureView = (TextureView) findViewById(R.id.video_view);
        textureView.setSurfaceTextureListener(this);

        Uri uri = getUri();
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

    protected Uri getUri() {
        Intent i = getIntent();
        if (i == null || i.getData() == null) {
            return null;
        }
        return i.getData();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
        mMediaPlayerWrapper.getPlayer().setSurface(surface);

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mMediaPlayerWrapper.getPlayer().setSurface(null);
        this.surface = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

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

    public void cancelBusy() {
        findViewById(R.id.progress).setVisibility(View.GONE);
    }
}
