package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.BookingStarListBean;
import com.yundian.celebrity.bean.MoneyDetailListBean;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import com.yundian.celebrity.utils.FormatUtil;

import java.util.List;

/**
 * 模拟数据adapter
 */

public class TestAdapter extends BaseQuickAdapter<MoneyDetailListBean, BaseViewHolder> {


    public TestAdapter(@LayoutRes int layoutResId, @Nullable List<MoneyDetailListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MoneyDetailListBean item) {

        helper.setText(R.id.tv_cash_money, "-" + item.getDepositTime());

    }
}