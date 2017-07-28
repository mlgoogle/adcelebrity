package com.yundian.celebrity.ui.main.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseFragment;
import com.yundian.celebrity.ui.main.activity.PublishStateActivity;
import com.yundian.celebrity.ui.main.activity.TimeAddressActivity;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.widget.NormalTitleBar;


import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/5.
 */

public class MeetManageFragment extends BaseFragment {

    NormalTitleBar ntTitle;
    private MeetingFansTypeFragment meetingFansTypeFragment;
    private MeetingFansOrderFragment meetingFansOrderFragment;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_meet_manage;
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
            meetingFansTypeFragment = new MeetingFansTypeFragment();
            meetingFansOrderFragment = new MeetingFansOrderFragment();
            transaction.add(R.id.fl_container, meetingFansTypeFragment, "meetingFansTypeFragment");
            transaction.add(R.id.fl_container, meetingFansOrderFragment, "meetingFansOrderFragment");
        } else {
            meetingFansTypeFragment = (MeetingFansTypeFragment) getChildFragmentManager().findFragmentByTag("meetingFansTypeFragment");
            meetingFansOrderFragment = (MeetingFansOrderFragment) getChildFragmentManager().findFragmentByTag("meetingFansOrderFragment");
        }
        transaction.commit();
    }


    @Override
    protected void initView() {
        initFindViewById();
        ntTitle.setTitleText(getContext().getResources().getString(R.string.meeting_manager));
        ntTitle.setTvLeftVisiable(false);
        ntTitle.setRightTitle(getContext().getResources().getString(R.string.time_address_manager));
        initFragment();
        switchTo(1);
        initListener();
    }

    private void initListener() {
        ntTitle.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TimeAddressActivity.class);
            }
        });
    }

    private void initFindViewById() {
        ntTitle = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
    }

    private void initFragment() {

    }


    private void switchTo(int type) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (type) {
            case 1:
                transaction.hide(meetingFansTypeFragment);
                transaction.hide(meetingFansOrderFragment);
                transaction.show(meetingFansTypeFragment);
                transaction.commit();
                break;
            case 2:
                transaction.hide(meetingFansTypeFragment);
                transaction.hide(meetingFansOrderFragment);
                transaction.show(meetingFansOrderFragment);
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
        LogUtils.loge("登陆成功ssssssss-------------------------------------MeetManageFragment");
//        if (meetingFansTypeFragment == null) {
//            LogUtils.loge("登陆成功ssssssss-----------------------meetingFansTypeFragment为null---");
//            meetingFansTypeFragment = (MeetingFansTypeFragment) getChildFragmentManager().findFragmentByTag("meetingFansTypeFragment");
//        }
        if (meetingFansTypeFragment != null){
            meetingFansTypeFragment.getData();
        }

//        if (meetingFansOrderFragment == null) {
//            LogUtils.loge("登陆成功ssssssss-----------------------meetingFansOrderFragment为null---");
//            meetingFansOrderFragment = (MeetingFansOrderFragment) getChildFragmentManager().findFragmentByTag("meetingFansOrderFragment");
//        }
        if (meetingFansOrderFragment != null){
            meetingFansOrderFragment.getData(false, 1, 10);
        }

    }
}
