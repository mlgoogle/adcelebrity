package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.IncomeReturnBean;

import java.util.List;


/**
 * Created by sll on 2017/7/12.
 */

public class InComeInfoAdapter extends BaseQuickAdapter<IncomeReturnBean, BaseViewHolder> {

    public InComeInfoAdapter(@LayoutRes int layoutResId, @Nullable List<IncomeReturnBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IncomeReturnBean item) {
        if (helper.getLayoutPosition()%2 == 0){
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_fafafa));
        }

        helper.setText(R.id.tv_turnover_date, String.valueOf(item.getOrderdate()))
                .setText(R.id.tv_turnover_count, String.valueOf(item.getOrder_count()))
                .setText(R.id.tv_time_count, String.valueOf(item.getOrder_num()))
                .setText(R.id.tv_total_money, String.valueOf(item.getPrice()));
    }
}
