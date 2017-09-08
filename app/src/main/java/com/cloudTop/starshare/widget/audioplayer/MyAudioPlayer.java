package com.cloudTop.starshare.widget.audioplayer;

import android.media.AudioManager;

import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.ToastUtils;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * Created by Administrator on 2017/8/24.
 */

public class MyAudioPlayer {
    private IjkMediaPlayer ijkMediaPlayer;
    private boolean mIsStopped=true;
    private boolean mIsError=false;
    private boolean isPrepared=false;
    private IMediaPlayer.OnCompletionListener mCompletionListener;


    public MyAudioPlayer() {
        // 初始化播放器
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOnPreparedListener(mPreparedListener);
        ijkMediaPlayer.setOnErrorListener(mErrorListener);

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
        this.mCompletionListener=mCompletionListener;
        ijkMediaPlayer.setOnCompletionListener(mCompletionListener);
    }
    IMediaPlayer.OnErrorListener mErrorListener=new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            isPrepared=false;
            mIsError=true;
            releaseWithError();
//            stop();
            LogUtils.logd("urlerror");
            ToastUtils.showShort("获取url链接失败");
            return true;
        }
    };

    public MyAudioPlayer setDataSource(String path) {
        try {

            if(!mIsStopped){
                    stop();
            }
//            if(mIsError){
//                releaseWithError();
//            }

            ijkMediaPlayer.setDataSource(path);
            isPrepared=true;
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

    public void releaseMedia(){
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.stop();
            ijkMediaPlayer.release();
            ijkMediaPlayer = null;
        }
    }

    public void releaseWithError(){
        if (ijkMediaPlayer != null) {
//            ijkMediaPlayer.stop();
            ijkMediaPlayer.release();
            ijkMediaPlayer =new IjkMediaPlayer();
            ijkMediaPlayer.setOnPreparedListener(mPreparedListener);
            ijkMediaPlayer.setOnErrorListener(mErrorListener);

            ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            setOnCompleteListener(mCompletionListener);
        }
        mIsError=false;
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
