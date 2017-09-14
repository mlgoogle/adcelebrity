package com.cloudTop.starshare.ui.main.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.Constant;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.EventBusMessage;
import com.cloudTop.starshare.bean.MeetOrderListBean;
import com.cloudTop.starshare.bean.RequestResultBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.view.RoundImageView;
import com.cloudTop.starshare.utils.BuyHandleStatuUtils;
import com.cloudTop.starshare.utils.ImageLoaderUtils;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.widget.NormalTitleBar;

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

    public static final int UNCERTAIN = 1;
    public static final int DENY = 2;
    public static final int COMPLETE = 3;
    public static final int AGREE = 4;


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

            ImageLoaderUtils.displayUrl(this, headImage, bean.getHeadurl());
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

        NetworkAPIFactoryImpl.getDealAPI().agreeMeet(bean.getStarcode(),AGREE, bean.getId(), new OnAPIListener<RequestResultBean>() {
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
