package com.yundian.celebrity.ui.main.presenter;

import android.view.View;

import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.yundian.celebrity.R;
import com.yundian.celebrity.app.CommentConfig;
import com.yundian.celebrity.bean.CircleFriendBean;
import com.yundian.celebrity.bean.EventBusMessage;
import com.yundian.celebrity.helper.CheckHelper;
import com.yundian.celebrity.listener.IDataRequestListener;
import com.yundian.celebrity.ui.main.activity.LoginActivity;
import com.yundian.celebrity.ui.main.contract.CircleContract;
import com.yundian.celebrity.ui.main.contract.LoginContract;
import com.yundian.celebrity.ui.main.model.CircleModel;
import com.yundian.celebrity.ui.main.model.LoginModel;
import com.yundian.celebrity.ui.wangyi.DemoCache;
import com.yundian.celebrity.ui.wangyi.config.preference.Preferences;
import com.yundian.celebrity.ui.wangyi.config.preference.UserPreferences;
import com.yundian.celebrity.utils.DatasUtil;
import com.yundian.celebrity.utils.SharePrefUtil;
import com.yundian.celebrity.widget.CheckException;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class LoginPresenter implements LoginContract.Presenter {
    private LoginModel loginModel;
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
//        在构造presenter的时候把具体的那个frament对象传过来了
        loginModel = new LoginModel();
        this.view = view;
    }


//
//    public void loadData(int loadType) {
//
//        List<CircleFriendBean.CircleListBean> datas = DatasUtil.createCircleDatas();
//        if (view != null) {
//            //
//            view.update2loadData(loadType, datas);
//        }
//    }

//    @Override
//    public void addFavort(String symbol, long circle_id,final int uid, final int circlePosition,final String user_name) {
//        circleModel.addFavort(symbol, circle_id, uid, new IDataRequestListener() {
//
//            @Override
//            public void loadSuccess(Object object) {
//                CircleFriendBean.CircleListBean.ApproveListBean item = DatasUtil.createFavortItem(uid, user_name);
//                if (view != null) {
//                    view.update2AddFavorite(circlePosition, item);
//                }
//            }
//        });
//    }
    private boolean isOnClicked = false;
    @Override
    public void login(String userName, String password,CheckHelper checkHelper) {
//        if (config == null) {
//            return;
//        }
        if (isOnClicked) {
            return;
        }
        isOnClicked = true;
        CheckException exception = new CheckException();
        if (checkHelper.checkMobile(userName, exception)
                && checkHelper.checkPassword(password, exception)) {

            loginModel.login(userName, password, new IDataRequestListener() {
                @Override
                public void loadSuccess(Object object) {
                    // 初始化消息提醒配置
                    initNotificationConfig();
                    EventBus.getDefault().postSticky(new EventBusMessage(1));  //登录成功消息

                    isOnClicked=false;
                    if (view != null) {
                        view.update2LoginSuccess();
                    }
                }

                @Override
                public void loadFail(Object object) {
                    isOnClicked=false;
                    if (view != null) {
                        view.update2LoginFail();
                    }
                }

            });

        } else {
            exception.getMessage();
            isOnClicked = false;
            if (view != null) {
                view.update2LoginFail();
            }
        }
    }


    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle() {
        this.view = null;
    }
}
