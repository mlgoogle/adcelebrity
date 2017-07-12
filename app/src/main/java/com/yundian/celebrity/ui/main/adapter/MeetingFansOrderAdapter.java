package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;

import java.util.List;

/**
 * Created by sll on 2017/7/11.
 */

public class MeetingFansOrderAdapter extends BaseQuickAdapter<WithDrawCashHistoryBean, BaseViewHolder> {
    public MeetingFansOrderAdapter(@LayoutRes int layoutResId, @Nullable List<WithDrawCashHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithDrawCashHistoryBean item) {

    }
}
