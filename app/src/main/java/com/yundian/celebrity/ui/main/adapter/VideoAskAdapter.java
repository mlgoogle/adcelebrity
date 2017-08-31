package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.FansAskBean;
import com.yundian.celebrity.utils.ImageLoaderUtils;
import com.yundian.celebrity.utils.TimeUtil;

import java.util.List;

/**
 * Created by sll on 2017/5/25.
 */

public class VideoAskAdapter extends BaseQuickAdapter<FansAskBean, BaseViewHolder> {

    OnAdapterCallBack onAdapterCallBack;

    public interface OnAdapterCallBack {
        void onGoRecordVideo(FansAskBean item,int position);
        void onLookAnswerVideo(FansAskBean item);
        void onLookAskVideo(FansAskBean item);
    }


    public VideoAskAdapter(@LayoutRes int layoutResId, @Nullable List data,OnAdapterCallBack onAdapterCallBack) {
        super(layoutResId, data);
        this.onAdapterCallBack=onAdapterCallBack;
    }

    @Override
    protected void convert(BaseViewHolder helper, final FansAskBean item) {
        helper.setText(R.id.tv_ask_question,item.getUask());
        TextView tvHeardNum = helper.getView(R.id.tv_heard_num);
        String heardNum = mContext.getResources().getString(R.string.look_num, item.getS_total());
        tvHeardNum.setText(heardNum);
        helper.setText(R.id.tv_name,item.getNickName());

        final int layoutPosition = helper.getLayoutPosition();

        LinearLayout ll_look = helper.getView(R.id.ll_look);
        LinearLayout ll_go_record = helper.getView(R.id.ll_go_record);
        TextView tv_look_answer = helper.getView(R.id.tv_look_answer);
        TextView tv_look_ask = helper.getView(R.id.tv_look_ask);
        ll_go_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterCallBack.onGoRecordVideo(item,layoutPosition);
            }
        });

        tv_look_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterCallBack.onLookAskVideo(item);
            }
        });

        tv_look_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterCallBack.onLookAnswerVideo(item);
            }
        });

//        ll_go_record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onAdapterCallBack.onGoRecordVideo(item);
//            }
//        });

        String askTime = TimeUtil.formatData(TimeUtil.dateFormatYMD, item.getAsk_t());
        helper.setText(R.id.tv_time,askTime);

        if(item.getC_type()==0){
            helper.setText(R.id.tv_custom_time,"15s定制");
        }else if(item.getC_type()==1){
            helper.setText(R.id.tv_custom_time,"30s定制");
        }else if(item.getC_type()==2){
            helper.setText(R.id.tv_custom_time,"45s定制");
        }else if(item.getC_type()==3){
            helper.setText(R.id.tv_custom_time,"60s定制");
        }

        if(item.getAnswer_t()==0){
            ll_go_record.setVisibility(View.VISIBLE);
            ll_look.setVisibility(View.GONE);
        }else{
            ll_go_record.setVisibility(View.GONE);
            ll_look.setVisibility(View.VISIBLE);

        }

        ImageView headView = helper.getView(R.id.iv_head);
        ImageLoaderUtils.displaySmallPhotoRound(mContext, headView, item.getHeadUrl());
    }


}