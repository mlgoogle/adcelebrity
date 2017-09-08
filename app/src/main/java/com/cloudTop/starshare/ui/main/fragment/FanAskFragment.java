package com.cloudTop.starshare.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.base.BaseFragment;
import com.cloudTop.starshare.widget.NormalTitleBar;

import butterknife.OnClick;

/**
 * 粉丝问答
 */

public class FanAskFragment extends BaseFragment {

    NormalTitleBar ntTitle;
    private CustomAudioFragment customAudioFragment;
    private VideoAskFragment videoAskFragment;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_fan_ask;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            customAudioFragment = new CustomAudioFragment();
            videoAskFragment = new VideoAskFragment();
            transaction.add(R.id.fl_container, customAudioFragment, "customAudioFragment");
            transaction.add(R.id.fl_container, videoAskFragment, "videoAskFragment");
        } else {
            customAudioFragment = (CustomAudioFragment) getChildFragmentManager().findFragmentByTag("customAudioFragment");
            videoAskFragment = (VideoAskFragment) getChildFragmentManager().findFragmentByTag("videoAskFragment");
        }
        transaction.commit();
    }

    @Override
    public void initView() {
        initFindViewById();
        ntTitle.setTitleText(getContext().getResources().getString(R.string.ask_fans));
        ntTitle.setTvLeftVisiable(false);


//        initFragment();
        switchTo(1);

    }


    private void initFindViewById() {
        ntTitle = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
    }

    private void switchTo(int type) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (type) {
            case 1:
                transaction.hide(customAudioFragment);
                transaction.hide(videoAskFragment);
                transaction.show(customAudioFragment);
                transaction.commit();
                break;
            case 2:
                transaction.hide(customAudioFragment);
                transaction.hide(videoAskFragment);
                transaction.show(videoAskFragment);
                transaction.commit();
                break;
        }
    }


    @OnClick({R.id.rb_fans_talk, R.id.rb_fans_interaction})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_fans_talk:
                switchTo(1);
                break;
            case R.id.rb_fans_interaction:
                switchTo(2);
                break;
        }
    }

    public void loadData() {



//        if (customAudioFragment != null){
//            customAudioFragment.getData(false, 1, 10);
//        }
//
////        if (fansInteractionFragment == null) {
////            LogUtils.loge("登陆成功ssssssss-----------------------fansInteractionFragment为null---");
////            fansInteractionFragment = (FansInteractionFragment) getChildFragmentManager().findFragmentByTag("fansInteractionFragment");
////        }
//        if (videoAskFragment != null){
//            videoAskFragment.getData(false, 0, 10);
//
//        }
    }
}
