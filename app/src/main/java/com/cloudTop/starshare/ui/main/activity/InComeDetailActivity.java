package com.cloudTop.starshare.ui.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.Constant;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.IncomeReturnBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.widget.NormalTitleBar;

import butterknife.Bind;
/**
 * Created by sll on 2017/7/12.
 */

public class InComeDetailActivity extends BaseActivity {
    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.star_money)
    TextView starMoney;
    @Bind(R.id.tv_turnover_count)
    TextView tvTurnoverCount;
    @Bind(R.id.tv_time_count)
    TextView tvTimeCount;
    @Bind(R.id.tv_order_money_count)
    TextView tvOrderMoneyCount;
    @Bind(R.id.parent_view)
    LinearLayout parentView;
    @Bind(R.id.tv_today_price)
    TextView todayPrice;
    @Bind(R.id.tv_yesToday_price)
    TextView yestodayPrice;

    @Override
    public int getLayoutId() {
        return R.layout.activity_income_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        ntTitle.setBackVisibility(true);
        ntTitle.setTitleColor(Color.rgb(255, 255, 255));
        ntTitle.setBackGroundColor(Color.rgb(251, 153, 56));
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra(Constant.INCOME);
        if (bundle != null) {
            IncomeReturnBean bean = (IncomeReturnBean) bundle.getParcelable(Constant.INCOME_INFO);
            ntTitle.setTitleText(bean.getOrderdate() + "");

            starMoney.setText(String.valueOf(bean.getPrice()));
            tvTurnoverCount.setText(String.valueOf(bean.getOrder_count()));
            tvTimeCount.setText(String.valueOf(bean.getOrder_num()));
            tvOrderMoneyCount.setText(String.valueOf(bean.getPrice()));

            requestData(bean.getOrderdate());
        }
    }

    private void requestData(int date) {
        String starCode = SharePrefUtil.getInstance().getStarcode();
        NetworkAPIFactoryImpl.getDealAPI().yesterdayIncome(starCode, date, new OnAPIListener<IncomeReturnBean>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("昨收金开失败------------------");
            }

            @Override
            public void onSuccess(IncomeReturnBean incomeReturnBean) {
                LogUtils.loge("昨收金开成功------------------");
                todayPrice.setText(String.format(getResources().getString(R.string.today_price), incomeReturnBean.getMin_price() + ""));  //今开
                yestodayPrice.setText(String.format(getResources().getString(R.string.yestoday_price), incomeReturnBean.getMax_price() + ""));  //昨收
            }
        });
    }


}
