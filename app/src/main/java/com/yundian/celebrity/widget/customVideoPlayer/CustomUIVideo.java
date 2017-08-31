package com.yundian.celebrity.widget.customVideoPlayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yundian.celebrity.R;

import moe.codeest.enviews.ENDownloadView;

/**
 * Created by shuyu on 2016/12/7.
 * 注意
 * 这个播放器的demo配置切换到全屏播放器
 * 这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
 */

public class CustomUIVideo extends StandardGSYVideoPlayer {

    private ProgressBar topProgress;

    private ImageView close;

    public CustomUIVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public CustomUIVideo(Context context) {
        super(context);
    }

    public CustomUIVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_customed;
    }
    @Override
    protected void init(Context context) {
        super.init(context);
        this.mContext=context;
        initView();
    }

    private void initView() {
        final ImageView question = (ImageView)findViewById(R.id.iv_question);
        topProgress = (ProgressBar)findViewById(R.id.top_progressbar);
        close = (ImageView)findViewById(R.id.close);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                getBackButton().performClick();
                callBack.onClickBack();

            }
        });



//        final LinearLayout anchor = (LinearLayout)findViewById(R.id.anchor);


//        question.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CustomPopupWindow popupWindow = new CustomPopupWindow.Builder()
//                        .setContext(mContext) //设置 context
//                        .setContentView(R.layout.content_ask) //设置布局文件
//                        .setwidth(LinearLayout.LayoutParams.WRAP_CONTENT) //设置宽度，由于我已经在布局写好，这里就用 wrap_content就好了
//                        .setheight(LinearLayout.LayoutParams.WRAP_CONTENT) //设置高度
//                        .setFouse(true)  //设置popupwindow 是否可以获取焦点
//                        .setOutSideCancel(true) //设置点击外部取消
//                        .setAnimationStyle(R.style.popup_anim_style) //设置popupwindow动画
//                        .builder() //
//                        .showAtLocation(question); //设置popupwindow居中显示
//            }
//        });
    }
//    @Override
//    protected void hideAllWidget() {
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mTopContainer, INVISIBLE);
//        setViewShowState(mBottomProgressBar, VISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(mStartButton, INVISIBLE);
//        setViewShowState(close, INVISIBLE);
//    }

    protected void changeUiToClear() {
        Debuger.printfLog("changeUiToClear");

        setViewShowState(mTopContainer, INVISIBLE);
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mLoadingProgressBar, INVISIBLE);
        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(close, INVISIBLE);

        setViewShowState(mLockScreen, GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
    }

    @Override
    protected void changeUiToPlayingShow() {
        Debuger.printfLog("changeUiToPlayingShow");

        setViewShowState(mTopContainer, VISIBLE);
        setViewShowState(mBottomContainer, VISIBLE);
        setViewShowState(mStartButton, VISIBLE);
        setViewShowState(close, VISIBLE);

        setViewShowState(mLoadingProgressBar, INVISIBLE);
        setViewShowState(mThumbImageViewLayout, INVISIBLE);
        setViewShowState(mBottomProgressBar, INVISIBLE);
        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);

        if (mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView) mLoadingProgressBar).reset();
        }
        updateStartImage();
    }

//    @Override
//    protected void changeUiToNormal() {
//        Debuger.printfLog("changeUiToNormal");
//
//        setViewShowState(mTopContainer, VISIBLE);
//        setViewShowState(mBottomContainer, INVISIBLE);
//        setViewShowState(mStartButton, VISIBLE);
//        setViewShowState(mLoadingProgressBar, INVISIBLE);
//        setViewShowState(mThumbImageViewLayout, VISIBLE);
//        setViewShowState(mBottomProgressBar, INVISIBLE);
//        setViewShowState(mLockScreen, (mIfCurrentIsFullscreen && mNeedLockFull) ? VISIBLE : GONE);
//
//        updateStartImage();
//        if (mLoadingProgressBar instanceof ENDownloadView) {
//            ((ENDownloadView) mLoadingProgressBar).reset();
//        }
//    }

    @Override
    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {

//        if (mProgressBar == null || mTotalTimeTextView == null || mCurrentTimeTextView == null) {
//            return;
//        }


        if (secProgress > 94) secProgress = 100;
//        if (secProgress != 0 && !mCacheFile) {
//            mProgressBar.setSecondaryProgress(secProgress);
//        }
//        mTotalTimeTextView.setText(CommonUtil.stringForTime(totalTime));
//        if (currentTime > 0)
//            mCurrentTimeTextView.setText(CommonUtil.stringForTime(currentTime));

        if (topProgress != null) {
            if (progress != 0) topProgress.setProgress(progress);
            if (secProgress != 0 && !mCacheFile)
                topProgress.setSecondaryProgress(secProgress);
        }
    }

    @Override
    protected void updateStartImage() {
        if(mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.video_click_pause_selector);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            } else {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            }
        }
    }

    CallBack callBack;

    public interface CallBack{
        void onClickBack();
    }

    public void setOnCallBack(CallBack callBack) {
        this.callBack=callBack;
    }
}
