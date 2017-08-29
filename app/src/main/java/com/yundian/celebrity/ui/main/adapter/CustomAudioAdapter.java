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

public class CustomAudioAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    OnAdapterCallBack onAdapterCallBack;

    public interface OnAdapterCallBack {
        void onGoRecord();
    }


    public CustomAudioAdapter(@LayoutRes int layoutResId, @Nullable List data, OnAdapterCallBack OnAdapterCallBack) {
        super(layoutResId, data);
        this.onAdapterCallBack = OnAdapterCallBack;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {


//        helper.setText(R.id.tv_talk_msg_count, 0+"");
//
//
//
//
        LinearLayout ll_go_record = helper.getView(R.id.ll_go_record);
        ll_go_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterCallBack.onGoRecord();
            }
        });

//        ImageLoaderUtils.display(mContext, headView, item.getHead_url());
//        helper.setText(R.id.tv_star_name, item.getNickname());
//        helper.setText(R.id.tv_talk_msg_count, item.get);
    }


}