package com.yundian.celebrity.ui.main.contract;



import com.yundian.celebrity.helper.CheckInfoHelper;


/**
 * Created by suneee on 2016/7/15.
 */
public interface LoginContract {

    interface View  {
//
        void update2LoginSuccess();
        void update2LoginFail();
        void update2LoginFail(String msg);
//        void update2DeleteCircle(String circleId);
//        void update2AddFavorite(int circlePosition, CircleFriendBean.CircleListBean.ApproveListBean addItem);
//        void update2DeleteFavort(int circlePosition, String favortId);
//        void update2AddComment(int circlePosition, CircleFriendBean.CircleListBean.CommentListBean addItem);
//        void update2DeleteComment(int circlePosition, String commentId);
//        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);
//        void update2loadData(int loadType, List<CircleFriendBean.CircleListBean> datas);
    }

    interface Presenter {
        void login(String userName,String password);
//        void loadData(int loadType);
//        void deleteCircle(final String circleId);
//        void addFavort(String symbol, long circle_id, int uid, final int circlePosition, String user_name);
//        void deleteFavort(final int circlePosition, final String favortId);
//        void deleteComment(final int circlePosition, final String commentId);

    }
}
