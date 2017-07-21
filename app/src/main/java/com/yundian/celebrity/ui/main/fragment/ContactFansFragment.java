package com.yundian.celebrity.ui.main.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseFragment;
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
    public void initView() {
        initFindViewById();
        ntTitle.setTitleText(getContext().getResources().getString(R.string.contact_fans));
        ntTitle.setTvLeftVisiable(false);
        initFragment();
        switchTo(1);
    }

    private void initFindViewById() {
         ntTitle = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
    }

    private void initFragment() {
        fansTalkFragment = new FansTalkFragment();
        fansInteractionFragment = new FansInteractionFragment();
    }


    private void switchTo(int type) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (type) {
            case 1:
                transaction.replace(R.id.fl_container, fansTalkFragment);
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.fl_container, fansInteractionFragment);
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
