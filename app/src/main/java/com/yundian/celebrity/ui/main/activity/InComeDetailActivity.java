package com.yundian.celebrity.ui.main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.widget.NormalTitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    @Bind(R.id.ll_recharge)
    LinearLayout llRecharge;
    @Bind(R.id.tv_time_count)
    TextView tvTimeCount;
    @Bind(R.id.tv_order_money_count)
    TextView tvOrderMoneyCount;
    @Bind(R.id.parent_view)
    LinearLayout parentView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_income_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ntTitle.setTitleText(getResources().getString(R.string.user_asset_manage));
        ntTitle.setBackVisibility(true);
        ntTitle.setTitleColor(Color.rgb(255, 255, 255));
        ntTitle.setBackGroundColor(Color.rgb(251, 153, 56));
    }


}
