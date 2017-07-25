package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.HaveStarUsersBean;
import com.yundian.celebrity.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by sll on 2017/5/25.
 */

public class FansTalkAdapter extends BaseQuickAdapter<HaveStarUsersBean, BaseViewHolder> {


    public FansTalkAdapter(@LayoutRes int layoutResId, @Nullable List<HaveStarUsersBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HaveStarUsersBean item) {
        ImageView headView = helper.getView(R.id.iv_star_head);
        ImageLoaderUtils.display(mContext, headView, item.getHead_url());
        helper.setText(R.id.tv_star_name, item.getNickname());
    }
}