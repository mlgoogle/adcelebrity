package com.cloudTop.starshare.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.bean.HaveStarUsersBean;
import com.cloudTop.starshare.utils.ImageLoaderUtils;

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
        helper.setText(R.id.tv_talk_msg_count, 0+"");
//        helper.setVisible(R.id.tv_talk_msg_count,false);
        if(recentContacts!=null&&!recentContacts.isEmpty()){
            for (int i = 0; i <recentContacts.size() ; i++) {
                RecentContact recentContact = recentContacts.get(i);
                String contactId = recentContact.getContactId();
//            String FromAccount = recent.getFromAccount();
                //item当前这个条目的id
//            contactId刚发送消息的那个人的id
                String faccid = item.getFaccid();
                if(String.valueOf(faccid).equals(contactId)){
                    helper.setText(R.id.tv_talk_msg_count, recentContact.getUnreadCount()+"");
//                helper.setVisible(R.id.tv_talk_msg_count,true);
                }
//            else{
//                helper.setText(R.id.tv_talk_msg_count, 0+"");
//            }
            }
        }



        ImageView headView = helper.getView(R.id.iv_star_head);
        ImageLoaderUtils.displayUrl(mContext, headView, item.getHead_url());
        helper.setText(R.id.tv_star_name, item.getNickname());
//        helper.setText(R.id.tv_talk_msg_count, item.get);
    }

    public void setRecentContacts(List<RecentContact> recentContacts){
        this.recentContacts=recentContacts;
        this.notifyDataSetChanged();
    }
}