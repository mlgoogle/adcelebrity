package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;

import java.util.List;

/**
 * Created by sll on 2017/7/12.
 */

public class InComeInfoAdapter extends BaseQuickAdapter<WithDrawCashHistoryBean, BaseViewHolder> {
    public InComeInfoAdapter(@LayoutRes int layoutResId, @Nullable List<WithDrawCashHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithDrawCashHistoryBean item) {
        int layoutPosition = helper.getLayoutPosition();

        if (layoutPosition % 2 == 0) {
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_fafafa));
        }
    }
}
