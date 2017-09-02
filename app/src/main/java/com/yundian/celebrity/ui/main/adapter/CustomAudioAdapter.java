package com.yundian.celebrity.ui.main.adapter;

import android.graphics.drawable.AnimationDrawable;
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

public class CustomAudioAdapter extends BaseQuickAdapter<FansAskBean, BaseViewHolder> {
    OnAdapterCallBack onAdapterCallBack;
    AnimationDrawable voiceBackground;

    public interface OnAdapterCallBack {
        void onGoRecord(FansAskBean item, int positon);

        void onListenAnswer(FansAskBean item, int positon, ImageViewDelegate imageViewDelegate);
    }


    ImageViewDelegate imageViewDelegate;

    public interface ImageViewDelegate {
        void onPlayAnimation();
        void onStopAnimation();
        void onCompletionAnimation();
    }


    public CustomAudioAdapter(@LayoutRes int layoutResId, @Nullable List data, OnAdapterCallBack OnAdapterCallBack) {
        super(layoutResId, data);
        this.onAdapterCallBack = OnAdapterCallBack;
//        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FansAskBean item) {

        final int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.tv_ask_question, item.getUask());
        TextView tvHeardNum = helper.getView(R.id.tv_heard_num);
        String heardNum = mContext.getResources().getString(R.string.heard_num, item.getS_total());

        tvHeardNum.setText(heardNum);
        helper.setText(R.id.tv_name, item.getNickName());

        String askTime = TimeUtil.formatData(TimeUtil.dateFormatYMD, item.getAsk_t());
        helper.setText(R.id.tv_time, askTime);

        if (item.getC_type() == 0) {
            helper.setText(R.id.tv_custom_time, "15s定制");
        } else if (item.getC_type() == 1) {
            helper.setText(R.id.tv_custom_time, "30s定制");
        } else if (item.getC_type() == 2) {
            helper.setText(R.id.tv_custom_time, "45s定制");
        } else if (item.getC_type() == 3) {
            helper.setText(R.id.tv_custom_time, "60s定制");
        }
//
//
//
//
        LinearLayout ll_go_record = helper.getView(R.id.ll_go_record);
        LinearLayout ll_listen = helper.getView(R.id.ll_listen);
        final ImageView ivPlay = (ImageView) ll_listen.findViewById(R.id.iv_play_record);
        ll_go_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterCallBack.onGoRecord(item, layoutPosition);
            }
        });
        ll_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdapterCallBack.onListenAnswer(item, layoutPosition, new ImageViewDelegate() {
                    @Override
                    public void onPlayAnimation() {
//                        ImageView ivPlay = (ImageView)view.findViewById(R.id.iv_play_record);
//                    CustomAudioFragment.this.currentPlayIvPlay=ivPlay;
                        if(voiceBackground!=null){
                            voiceBackground.stop();
                            ivPlay.setBackground(mContext.getResources().getDrawable(R.drawable.voice_icon));
                        }

                        ivPlay.setBackground(mContext.getResources().getDrawable(R.drawable.animation_voice_paly));
                        voiceBackground = (AnimationDrawable) ivPlay.getBackground();
                        voiceBackground.start();
                    }

                    @Override
                    public void onStopAnimation() {
                        if(voiceBackground!=null){
                            voiceBackground.stop();
                            ivPlay.setBackground(mContext.getResources().getDrawable(R.drawable.voice_icon));
                        }
                    }

                    @Override
                    public void onCompletionAnimation() {
                        if (ivPlay != null) {
                            ivPlay.setBackground(mContext.getResources().getDrawable(R.drawable.voice_icon));
                        }
                    }
                });

            }
        });

        ImageView headView = helper.getView(R.id.iv_head);

        if (item.getAnswer_t() == 0) {
            ll_go_record.setVisibility(View.VISIBLE);
            ll_listen.setVisibility(View.GONE);
        } else {
            ll_go_record.setVisibility(View.GONE);
            ll_listen.setVisibility(View.VISIBLE);

        }


        ImageLoaderUtils.displaySmallPhotoRound(mContext, headView, item.getHeadUrl());
//        helper.setText(R.id.tv_star_name, item.getNickname());
//        helper.setText(R.id.tv_talk_msg_count, item.get);
    }


}