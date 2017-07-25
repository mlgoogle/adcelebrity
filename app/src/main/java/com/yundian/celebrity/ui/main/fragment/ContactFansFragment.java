package com.yundian.celebrity.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseFragment;
import com.yundian.celebrity.ui.main.activity.PublishStateActivity;
import com.yundian.celebrity.widget.NormalTitleBar;

import butterknife.OnClick;

/**
 * 联系粉丝
 */

public class ContactFansFragment extends BaseFragment {

    NormalTitleBar ntTitle;
    private FansTalkFragment fansTalkFragment;
    private FansInteractionFragment fansInteractionFragment;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_contact_fans;
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
            fansTalkFragment = new FansTalkFragment();
            fansInteractionFragment = new FansInteractionFragment();
            transaction.add(R.id.fl_container, fansTalkFragment, "fansTalkFragment");
            transaction.add(R.id.fl_container, fansInteractionFragment, "fansInteractionFragment");
        } else {
            fansTalkFragment = (FansTalkFragment) getChildFragmentManager().findFragmentByTag("fansTalkFragment");
            fansInteractionFragment = (FansInteractionFragment) getChildFragmentManager().findFragmentByTag("fansInteractionFragment");
        }
        transaction.commit();
    }

    @Override
    public void initView() {
        initFindViewById();
        ntTitle.setTitleText(getContext().getResources().getString(R.string.contact_fans));
        ntTitle.setTvLeftVisiable(false);
        ntTitle.setRightTitle("发布状态");
        ntTitle.setRightTitleVisibility(true);
//        initFragment();
        switchTo(1);
        initListener();
    }

    private void initListener() {
        ntTitle.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PublishStateActivity.class);
            }
        });
    }

    private void initFindViewById() {
        ntTitle = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
    }

    private void switchTo(int type) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (type) {
            case 1:
                transaction.hide(fansTalkFragment);
                transaction.hide(fansInteractionFragment);
                transaction.show(fansTalkFragment);
                transaction.commit();
                break;
            case 2:
                transaction.hide(fansTalkFragment);
                transaction.hide(fansInteractionFragment);
                transaction.show(fansInteractionFragment);
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
//        if (fansTalkFragment == null) {
//            LogUtils.loge("登陆成功ssssssss-----------------------fansTalkFragment为null---");
//            fansTalkFragment = (FansTalkFragment) getChildFragmentManager().findFragmentByTag("fansTalkFragment");
//        }
        fansTalkFragment.getData(false, 1, 10);
//        if (fansInteractionFragment == null) {
//            LogUtils.loge("登陆成功ssssssss-----------------------fansInteractionFragment为null---");
//            fansInteractionFragment = (FansInteractionFragment) getChildFragmentManager().findFragmentByTag("fansInteractionFragment");
//        }
        fansInteractionFragment.getData(false, 0, 10);
    }
}
