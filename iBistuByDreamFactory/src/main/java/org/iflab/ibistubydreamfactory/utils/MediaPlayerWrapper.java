package org.iflab.ibistubydreamfactory.utils;

import android.media.MediaPlayer;

import java.io.IOException;


/**
 * 用于配置播放器
 * http://developer.android.com/intl/zh-cn/reference/android/media/MediaPlayer.html
 * status
 */
public class MediaPlayerWrapper implements MediaPlayer.OnPreparedListener {
    public static final int STATUS_IDLE = 0;
    public static final int STATUS_PREPARING = 1;
    public static final int STATUS_PREPARED = 2;
    public static final int STATUS_STARTED = 3;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_STOPPED = 5;
    protected MediaPlayer mPlayer;
    private MediaPlayer.OnPreparedListener mPreparedListener;
    private int mStatus = STATUS_IDLE;

    public void init() {
        mStatus = STATUS_IDLE;
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

    }


    public void openRemoteFile(String url) {
        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    public void prepare() {
        if (mPlayer == null) {
            return;
        }
        if (mStatus == STATUS_IDLE || mStatus == STATUS_STOPPED) {
            mPlayer.prepareAsync();
            mStatus = STATUS_PREPARING;
        }
    }

    private void start() {
        if (mPlayer == null) {
            return;
        }
        if (mStatus == STATUS_PREPARED || mStatus == STATUS_PAUSED) {
            mPlayer.start();
            mStatus = STATUS_STARTED;
        }

    }

    public void setPreparedListener(MediaPlayer.OnPreparedListener mPreparedListener) {
        this.mPreparedListener = mPreparedListener;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mStatus = STATUS_PREPARED;
        start();
        if (mPreparedListener != null) {
            mPreparedListener.onPrepared(mp);
        }
    }

    public void onPause() {
        if (mPlayer == null) {
            return;
        }
        if (mPlayer.isPlaying() && mStatus == STATUS_STARTED) {
            mPlayer.pause();
            mStatus = STATUS_PAUSED;
        }

    }


    public void onResume() {
        start();
    }

    public void onDestroy() {
        if (mPlayer == null) {
            return;
        }
        if (mStatus == STATUS_STARTED || mStatus == STATUS_PAUSED) {
            mPlayer.stop();
            mStatus = STATUS_STOPPED;
        }
        if (mPlayer != null) {
            mPlayer.setSurface(null);
            mPlayer.release();
        }
        mPlayer = null;
    }
}
