package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.MeetOrderListBean;
import com.yundian.celebrity.utils.BuyHandleStatuUtils;
import com.yundian.celebrity.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by sll on 2017/7/11.
 */

public class MeetingFansOrderAdapter extends BaseQuickAdapter<MeetOrderListBean, BaseViewHolder> {
    public MeetingFansOrderAdapter(@LayoutRes int layoutResId, @Nullable List<MeetOrderListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetOrderListBean item) {
        ImageView iconView = helper.getView(R.id.iv_meet_type);
        ImageLoaderUtils.displayUrl(mContext, iconView, item.getHeadurl());
        helper.setText(R.id.tv_meet_des, item.getNickname())
                .setText(R.id.tv_spend_time,
                        String.format(mContext.getResources().getString(R.string.star_meet_type), item.getName(), item.getOrder_time()));
        TextView bookingStatus = helper.getView(R.id.tv_booking);  //   "meet_type":1,      #'1-待确认 2-已拒绝 3-已完成 4-已同意；',
        bookingStatus.setText(BuyHandleStatuUtils.getMeetStatus(item.getMeet_type()));
        if (item.getMeet_type() == 1 || item.getMeet_type() == 2) {
            bookingStatus.setBackgroundColor(mContext.getResources().getColor(R.color.color_cccccc));
        } else {
            bookingStatus.setBackgroundColor(mContext.getResources().getColor(R.color.color_FB9938));
        }
    }
}
