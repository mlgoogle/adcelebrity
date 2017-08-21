package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.app.CommentConfig;
import com.yundian.celebrity.bean.ActionItem;
import com.yundian.celebrity.bean.CircleFriendBean;
import com.yundian.celebrity.ui.main.presenter.CirclePresenter;
import com.yundian.celebrity.utils.ImageLoaderUtils;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.SharePrefUtil;
import com.yundian.celebrity.utils.TimeUtil;
import com.yundian.celebrity.widget.CommentListView;
import com.yundian.celebrity.widget.PraiseListView;
import com.yundian.celebrity.widget.SnsPopupWindow;
import com.yundian.celebrity.widget.emoji.MoonUtils;

import java.util.List;

import static com.yundian.celebrity.R.id.commentList;


//package com.yundian.celebrity.ui.main.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.yundian.celebrity.R;
//import com.yundian.celebrity.app.CommentConfig;
//import com.yundian.celebrity.bean.CircleFriendBean;
//import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
//import com.yundian.celebrity.ui.main.presenter.CirclePresenter;
//import com.yundian.celebrity.utils.ImageLoaderUtils;
//import com.yundian.celebrity.utils.SharePrefUtil;
//import com.yundian.celebrity.utils.TimeUtil;
//import com.yundian.celebrity.utils.ToastUtils;
//import com.yundian.celebrity.widget.emoji.MoonUtils;
//
//import java.util.List;
//
///**
// * Created by Administrator on 2017/7/12.
// */
//
public class CircleFriendAdapter extends BaseQuickAdapter<CircleFriendBean.CircleListBean, BaseViewHolder> {
    public CircleFriendAdapter(@LayoutRes int layoutResId, @Nullable List<CircleFriendBean.CircleListBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(final BaseViewHolder holder, CircleFriendBean.CircleListBean item) {
//        final CircleViewHolder circleViewHolder = (CircleViewHolder) holder;
        final CircleFriendBean.CircleListBean circleItem = item;
        final String content = circleItem.getContent();
        LogUtils.loge("获取的content:" + content);
        final List<CircleFriendBean.CircleListBean.ApproveListBean> favortDatas = circleItem.getApprove_list();
        final List<CircleFriendBean.CircleListBean.CommentListBean> commentsDatas = circleItem.getComment_list();
        boolean hasFavort = circleItem.hasFavort();
        boolean hasComment = circleItem.hasComment();

        ImageView headIv = holder.getView(R.id.headIv);
        ImageView img_back = holder.getView(R.id.img_back);
        ImageLoaderUtils.displaySmallPhoto(mContext, headIv, circleItem.getHead_url_tail());
        holder.addOnClickListener(R.id.img_back);
        long createTime = circleItem.getCreate_time() * 1000;
        holder.setText(R.id.nameTv, circleItem.getSymbol_name())
                .setText(R.id.tv_time, TimeUtil.getfriendlyTime(createTime));

        if (TextUtils.isEmpty(circleItem.getPic_url_tail())) {
            img_back.setVisibility(View.GONE);
        } else {
            ImageLoaderUtils.display(mContext, img_back, circleItem.getPic_url_tail());
        }
        EditText contentTv = holder.getView(R.id.contentTv);
        if (!TextUtils.isEmpty(content)) {
            contentTv.setText(content);

            MoonUtils.replaceEmoticons(mContext, contentTv.getText(), 0, contentTv.getText().toString().length());
        }
        contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);


        PraiseListView praiseListView = holder.getView(R.id.praiseListView);
        CommentListView commentListView = holder.getView(commentList);
        LinearLayout digCommentBody = holder.getView(R.id.digCommentBody);
        ImageView snsBtn = holder.getView(R.id.snsBtn);
        View digLine = holder.getView(R.id.lin_dig);
//        circleViewHolder.tv_time.setText();
        if (hasFavort || hasComment) {
            if (hasFavort) {//处理点赞列表
                praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {

                    }
                });
                praiseListView.setDatas(favortDatas);
                praiseListView.setVisibility(View.VISIBLE);
            } else {
                praiseListView.setVisibility(View.GONE);
            }

            if (hasComment) {//处理评论列表
                commentListView.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {
                        CircleFriendBean.CircleListBean.CommentListBean commentItem = commentsDatas.get(commentPosition);
                        LogUtils.loge("当前的ssss:" + commentItem.getDirection());
                        if (0 == commentItem.getDirection()) {//只有是用户的评论明星才能回复
                            LogUtils.loge("回复明星的评论");
                            if (presenter != null) {
                                CommentConfig config = new CommentConfig();
                                config.circlePosition = holder.getLayoutPosition();
                                config.commentPosition = commentPosition;
                                config.Circle_id = circleItem.getCircle_id();
                                config.commentType = CommentConfig.Type.REPLY;
                                config.symbol_name = circleItem.getSymbol_name();
                                config.symbol_code = circleItem.getSymbol();
                                config.uid = commentItem.getUid();  //回复对应的用户id
                                presenter.showEditTextBody(config);
                            }
                        }
                    }
                });

                commentListView.setDatas(commentsDatas, circleItem.getSymbol_name());
                commentListView.setVisibility(View.VISIBLE);

            } else {
                commentListView.setVisibility(View.GONE);
            }
            digCommentBody.setVisibility(View.VISIBLE);
        } else {
            digCommentBody.setVisibility(View.GONE);
        }
        digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

        final SnsPopupWindow snsPopupWindow = new SnsPopupWindow(mContext);
        snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
        snsPopupWindow.update();
        snsPopupWindow.setmItemClickListener(new PopupItemClickListener(holder.getLayoutPosition(), circleItem, SharePrefUtil.getInstance().getUserId(), SharePrefUtil.getInstance().getUserNickName()));
        snsBtn.setOnClickListener(new View.OnClickListener() {  //评论点赞隐藏
            @Override
            public void onClick(View view) {
                //弹出popupwindow
                snsPopupWindow.showPopupWindow(view);
            }
        });
    }

    //    private Context context;
    //从adapter传过来
    private CirclePresenter presenter;

    public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }

//    public CircleFriendAdapter(Context context) {
//        this.context = context;
//    }


//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);
//        return new CircleViewHolder(view);
//    }

//    @Override
//    protected void convert(BaseViewHolder helper, WithDrawCashHistoryBean item) {
//
//    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        final CircleViewHolder circleViewHolder = (CircleViewHolder) holder;
//        final CircleFriendBean.CircleListBean circleItem = (CircleFriendBean.CircleListBean) datas.get(position);
//        final String content = circleItem.getContent();
//        final List<CircleFriendBean.CircleListBean.ApproveListBean> favortDatas = circleItem.getApprove_list();
//        final List<CircleFriendBean.CircleListBean.CommentListBean> commentsDatas = circleItem.getComment_list();
//        boolean hasFavort = circleItem.hasFavort();
//        boolean hasComment = circleItem.hasComment();
//        ImageLoaderUtils.displaySmallPhoto(context, circleViewHolder.headIv, circleItem.getHead_url());
//        circleViewHolder.nameTv.setText(circleItem.getSymbol_name());
//        if (TextUtils.isEmpty(circleItem.getPic_url())) {
//            circleViewHolder.img_back.setVisibility(View.GONE);
//        } else {
//            ImageLoaderUtils.displaySmallPhoto(context, circleViewHolder.img_back, circleItem.getPic_url());
//        }
//        if (!TextUtils.isEmpty(content)) {
//            circleViewHolder.contentTv.setText(content);
//            MoonUtils.replaceEmoticons(context, circleViewHolder.contentTv.getText(), 0, circleViewHolder.contentTv.getText().length());
//        }
//        circleViewHolder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
//        circleViewHolder.tv_time.setText(TimeUtil.getfriendlyTime(circleItem.getCreate_time() * 1000));
//        if (hasFavort || hasComment) {
//            if (hasFavort) {//处理点赞列表
//                circleViewHolder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
//                    @Override
//                    public void onClick(int position) {
//
//                    }
//                });
//                circleViewHolder.praiseListView.setDatas(favortDatas);
//                circleViewHolder.praiseListView.setVisibility(View.VISIBLE);
//            } else {
//                circleViewHolder.praiseListView.setVisibility(View.GONE);
//            }
//
//            if (hasComment) {//处理评论列表
//                circleViewHolder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int commentPosition) {
//                        CircleFriendBean.CircleListBean.CommentListBean commentItem = commentsDatas.get(commentPosition);
//                        if (1 == commentItem.getDirection()) {//回复明星的评论
//                            if (presenter != null) {
//                                CommentConfig config = new CommentConfig();
//                                config.circlePosition = position;
//                                config.commentPosition = commentPosition;
//                                config.Circle_id = circleItem.getCircle_id();
//                                config.commentType = CommentConfig.Type.REPLY;
//                                config.symbol_name = circleItem.getSymbol_name();
//                                config.symbol_code = circleItem.getSymbol();
//                                presenter.showEditTextBody(config);
//                            }
//                        }
//                    }
//                });
//
//                circleViewHolder.commentList.setDatas(commentsDatas, circleItem.getSymbol_name());
//                circleViewHolder.commentList.setVisibility(View.VISIBLE);
//
//            } else {
//                circleViewHolder.commentList.setVisibility(View.GONE);
//            }
//            circleViewHolder.digCommentBody.setVisibility(View.VISIBLE);
//        } else {
//            circleViewHolder.digCommentBody.setVisibility(View.GONE);
//        }
//        circleViewHolder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);
//
//        final SnsPopupWindow snsPopupWindow = circleViewHolder.snsPopupWindow;
//        snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
//        snsPopupWindow.update();
//        snsPopupWindow.setmItemClickListener(new PopupItemClickListener(position, circleItem, SharePrefUtil.getInstance().getUserId(), SharePrefUtil.getInstance().getUserNickName()));
//        circleViewHolder.snsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //弹出popupwindow
//                snsPopupWindow.showPopupWindow(view);
//            }
//        });
//    }

//    @Override
//    public int getItemCount() {
//        return datas.size();
//    }
//
//    @Override
//    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//    }


    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private int mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private String mUserName;
        private CircleFriendBean.CircleListBean mCircleItem;

        public PopupItemClickListener(int circlePosition, CircleFriendBean.CircleListBean circleItem, int favorId, String user_name) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
            this.mUserName = user_name;
        }
        //在点击item的时候
        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
//                case 0://点赞、取消点赞  用户点赞明星动态
//                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
//                        return;
//                    mLasttime = System.currentTimeMillis();
//                    if (presenter != null) {
//                        //判断是否已点赞
//                        int curUserFavortId = mCircleItem.getCurUserFavortId(SharePrefUtil.getInstance().getUserId());
//                        if ("赞".equals(actionitem.mTitle.toString()) && curUserFavortId != mFavorId) {
//                            presenter.addFavort(mCircleItem.getSymbol(), mCircleItem.getCircle_id(), SharePrefUtil.getInstance().getUserId(), mCirclePosition, mUserName);
//                        } else {//取消点赞
//                            //presenter.deleteFavort(mCirclePosition, mFavorId);
//                            ToastUtils.showShort("您已经赞过");
//                        }
//                    }
//                    break;
                case 1://发布评论
                    //如果presenter不为空
                    if (presenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        config.Circle_id = mCircleItem.getCircle_id();
                        config.symbol_name = mCircleItem.getSymbol_name();
                        config.symbol_code = mCircleItem.getSymbol();
                        //执行presenter的找个方法
                        presenter.showEditTextBody(config);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
