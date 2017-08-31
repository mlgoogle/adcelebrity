package com.yundian.celebrity.widget.audioplayer;

import android.media.AudioManager;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * Created by Administrator on 2017/8/24.
 */

public class MyAudioPlayer {
    private IjkMediaPlayer ijkMediaPlayer;
    private boolean mIsStopped=true;
    private boolean isPrepared=false;


    public MyAudioPlayer() {
        // 初始化播放器
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOnPreparedListener(mPreparedListener);

        ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            isPrepared=false;
            start();
        }
    };

    public void setOnCompleteListener(IMediaPlayer.OnCompletionListener mCompletionListener){
        ijkMediaPlayer.setOnCompletionListener(mCompletionListener);
    }

    public MyAudioPlayer setDataSource(String path) {
        try {
            isPrepared=true;
            if(!mIsStopped){
                    stop();
            }
            ijkMediaPlayer.setDataSource(path);
            ijkMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void start() {
        if (ijkMediaPlayer != null) {
            mIsStopped=false;
            ijkMediaPlayer.start();
        }
    }

    public void stop(){
        if (ijkMediaPlayer != null&&!mIsStopped) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.reset();
        }
        mIsStopped = true;
//        mMediaPlayer = null;
    }

    public void release(){
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
    }
    public boolean isMediaPlayerStop(){
        return mIsStopped;
    }
    public boolean isMediaPlayerPrepared(){
        return isPrepared;
    }

//
//    public void setPreparedListener(IMediaPlayer.OnPreparedListener preparedListener){
//        this.preparedListener=preparedListener;
//
//    }
}
