package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.MoneyDetailListBean;
import com.yundian.celebrity.utils.BuyHandleStatuUtils;
import java.util.List;



/**
 * Created by sll on 2017/5/25.
 */

public class MoneyBagDetailAdapter extends BaseQuickAdapter<MoneyDetailListBean, BaseViewHolder> {
    public MoneyBagDetailAdapter(@LayoutRes int layoutResId, @Nullable List<MoneyDetailListBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MoneyDetailListBean item) {
        TextView name = helper.getView(R.id.tv_star_name);
        TextView status = helper.getView(R.id.tv_trust_status);
//        helper.setText(R.id.tv_star_name,item.getId());
        helper.setText(R.id.tv_money_detail_time, item.getId());
        helper.setText(R.id.tv_star_name, item.getId());
        helper.setText(R.id.tv_star_name, item.getId());
        helper.setText(R.id.tv_star_name, item.getId());
        helper.setText(R.id.tv_star_name, item.getId());
        helper.setText(R.id.tv_star_name, item.getId());
// MoneyDetailListBean item = mDataList.get(position);
        String plus_minus;
        if (item.getRecharge_type() == 0) {  //充值记录
            plus_minus = "+";
            name.setText(BuyHandleStatuUtils.getRechargeType(item.getDepositType()));
            status.setText(BuyHandleStatuUtils.getRechargeStatus(item.getStatus()));
        }
    }


}