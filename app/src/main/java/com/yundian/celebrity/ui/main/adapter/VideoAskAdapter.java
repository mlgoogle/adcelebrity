package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by sll on 2017/5/25.
 */

public class VideoAskAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {




    public VideoAskAdapter(@LayoutRes int layoutResId, @Nullable List data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
//        helper.setText(R.id.tv_talk_msg_count, 0+"");
//
//
//
//
//        ImageView headView = helper.getView(R.id.iv_star_head);
//        ImageLoaderUtils.display(mContext, headView, item.getHead_url());
//        helper.setText(R.id.tv_star_name, item.getNickname());
//        helper.setText(R.id.tv_talk_msg_count, item.get);
    }


}