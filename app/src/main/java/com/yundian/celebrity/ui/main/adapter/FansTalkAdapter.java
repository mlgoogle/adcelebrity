package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import com.yundian.celebrity.utils.FormatUtil;

import java.util.List;

/**
 * Created by sll on 2017/5/25.
 */

public class FansTalkAdapter extends BaseQuickAdapter<WithDrawCashHistoryBean, BaseViewHolder> {


    public FansTalkAdapter(@LayoutRes int layoutResId, @Nullable List<WithDrawCashHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithDrawCashHistoryBean item) {
        helper.setText(R.id.tv_star_name, item.getWithdrawTime());
    }
}