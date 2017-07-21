package com.yundian.celebrity.ui.main.fragment;


import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseFragment;
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
    protected void initView() {
        initFindViewById();
        ntTitle.setTitleText(getContext().getResources().getString(R.string.meeting_manager));
        ntTitle.setTvLeftVisiable(false);
        initFragment();
        switchTo(1);
    }
    private void initFindViewById() {
        ntTitle = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
    }

    private void initFragment() {
        meetingFansTypeFragment = new MeetingFansTypeFragment();
        meetingFansOrderFragment = new MeetingFansOrderFragment();
    }


    private void switchTo(int type) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (type) {
            case 1:
                transaction.replace(R.id.fl_container, meetingFansTypeFragment);
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.fl_container, meetingFansOrderFragment);
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
}
