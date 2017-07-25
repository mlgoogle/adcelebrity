package com.yundian.celebrity.ui.main.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.yundian.celebrity.R;
import com.yundian.celebrity.app.Constant;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.bean.EventBusMessage;
import com.yundian.celebrity.bean.MeetOrderListBean;
import com.yundian.celebrity.bean.RequestResultBean;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.ui.view.RoundImageView;
import com.yundian.celebrity.utils.BuyHandleStatuUtils;
import com.yundian.celebrity.utils.ImageLoaderUtils;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by sll on 2017/7/12.
 */

public class MeetingFansDetailActivity extends BaseActivity {
    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.headImage)
    RoundImageView headImage;
    @Bind(R.id.tv_petname)
    TextView tvPetname;
    @Bind(R.id.tv_meeting_detail_time)
    TextView tvMeetingDetailTime;
    @Bind(R.id.tv_meeting_detail_place)
    TextView tvMeetingDetailPlace;
    @Bind(R.id.tv_meeting_detail_type)
    TextView tvMeetingDetailType;
    @Bind(R.id.tv_meeting_detail_additional)
    TextView tvMeetingDetailAdditional;
    @Bind(R.id.btn_agree)
    Button btnAgree;
    private MeetOrderListBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_meet_fans_detail;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        ntTitle.setTitleText(getString(R.string.star_meet_detail));
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra(Constant.MEET_TYPE);
        if (bundle != null) {
            bean = (MeetOrderListBean) bundle.getParcelable(Constant.MEET_TYPE_DETAIL);

            ImageLoaderUtils.display(this, headImage, bean.getHeadurl());
            tvPetname.setText(bean.getNickname());
            tvMeetingDetailTime.setText(String.format(getString(R.string.meeting_detail_time), bean.getOrder_time()));
            tvMeetingDetailPlace.setText(String.format(getString(R.string.meeting_detail_please), bean.getMeet_city()));
            tvMeetingDetailType.setText(String.format(getString(R.string.meeting_detail_type), bean.getName()));
            tvMeetingDetailAdditional.setText(String.format(getString(R.string.meeting_detail_additional), bean.getComment()));
            if (bean.getMeet_type() == 1) {
                btnAgree.setEnabled(true);
                btnAgree.setText("同意");
            } else {
                btnAgree.setEnabled(false);
                btnAgree.setText(BuyHandleStatuUtils.getMeetStatus(bean.getMeet_type()));
            }
        }
    }

    @OnClick(R.id.btn_agree)
    public void onViewClicked() {
        agreeMeet();
    }

    private void agreeMeet() {
        if (bean == null) {
            return;
        }

        NetworkAPIFactoryImpl.getDealAPI().agreeMeet(bean.getStarcode(), bean.getMeet_type(), bean.getMid(), new OnAPIListener<RequestResultBean>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("同意约见失败-------------------");
                ToastUtils.showStatusView("约见失败", true);
            }

            @Override
            public void onSuccess(RequestResultBean requestResultBean) {
                LogUtils.loge("同意约见成功-------------------");
                if (requestResultBean.getResult() == 1) {
                    ToastUtils.showStatusView("成功约见", true);
                    EventBus.getDefault().post(new EventBusMessage(-76));
                }
            }
        });
    }
}
