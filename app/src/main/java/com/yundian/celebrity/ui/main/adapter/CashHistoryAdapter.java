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

public class CashHistoryAdapter extends BaseQuickAdapter<WithDrawCashHistoryBean, BaseViewHolder> {


    public CashHistoryAdapter(@LayoutRes int layoutResId, @Nullable List<WithDrawCashHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithDrawCashHistoryBean item) {
        helper.setText(R.id.tv_cash_time, item.getWithdrawTime());
        helper.setText(R.id.tv_bank_name, String.format(mContext.getResources().getString(R.string.bank_end_number),
                item.getBank(), FormatUtil.getCardEnd(item.getCardNo())));
         helper.setText(R.id.tv_cash_time, item.getWithdrawTime());
        helper.setText(R.id.tv_bank_name, String.format(mContext.getResources().getString(R.string.bank_end_number),
                item.getBank(), FormatUtil.getCardEnd(item.getCardNo())));
        helper.setText(R.id.tv_cash_money, "-" + item.getAmount());

    }
}