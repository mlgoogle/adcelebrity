package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;

import java.util.List;

/**
 * Created by sll on 2017/5/25.
 */

public class VideoAskAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    OnAdapterCallBack onAdapterCallBack;

    public interface OnAdapterCallBack {
        void onGoRecordVideo();
    }


    public VideoAskAdapter(@LayoutRes int layoutResId, @Nullable List data,OnAdapterCallBack onAdapterCallBack) {
        super(layoutResId, data);
        this.onAdapterCallBack=onAdapterCallBack;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
//        helper.setText(R.id.tv_talk_msg_count, 0+"");
//
//
//
//
//        ImageView headView = helper.getView(R.id.iv_star_head);
        LinearLayout ll_look = helper.getView(R.id.ll_look);
        ll_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterCallBack.onGoRecordVideo();
            }
        });
//        ImageLoaderUtils.display(mContext, headView, item.getHead_url());
//        helper.setText(R.id.tv_star_name, item.getNickname());
//        helper.setText(R.id.tv_talk_msg_count, item.get);
    }


}