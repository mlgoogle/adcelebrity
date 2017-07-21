package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.OrderListReturnBean;
import com.yundian.celebrity.utils.ImageLoaderUtils;


import java.util.List;

/**
 * Created by sll on 2017/5/25.
 */

public class MeetTypeAdapter extends BaseQuickAdapter<OrderListReturnBean, BaseViewHolder> {


    public MeetTypeAdapter(@LayoutRes int layoutResId, @Nullable List<OrderListReturnBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final OrderListReturnBean item) {
        holder.setText(R.id.tv_content, item.getName())
                .setText(R.id.tv_price,item.getPrice() + "秒");
        ImageView imageView = holder.getView(R.id.imagview);
        ImageLoaderUtils.display(mContext, imageView, item.getShowpic_url());
    }
}