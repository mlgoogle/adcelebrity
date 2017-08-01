package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.HaveStarUsersBean;
import com.yundian.celebrity.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by sll on 2017/5/25.
 */

public class FansTalkAdapter extends BaseQuickAdapter<HaveStarUsersBean, BaseViewHolder> {


    private List<RecentContact> recentContacts;

    public FansTalkAdapter(@LayoutRes int layoutResId, @Nullable List<HaveStarUsersBean> data, List<RecentContact> recentContacts) {
        super(layoutResId, data);
        this.recentContacts=recentContacts;
    }

    @Override
    protected void convert(BaseViewHolder helper, HaveStarUsersBean item) {
        for(RecentContact recent:recentContacts){
            if(String.valueOf(item.getUid()).equals(recent.getContactId())){
                helper.setText(R.id.tv_talk_msg_count, recent.getUnreadCount());
            }
        }
        ImageView headView = helper.getView(R.id.iv_star_head);
        ImageLoaderUtils.display(mContext, headView, item.getHead_url());
        helper.setText(R.id.tv_star_name, item.getNickname());
//        helper.setText(R.id.tv_talk_msg_count, item.get);
    }

    public void setRecentContacts(List<RecentContact> recentContacts){
        this.recentContacts=recentContacts;
    }
}