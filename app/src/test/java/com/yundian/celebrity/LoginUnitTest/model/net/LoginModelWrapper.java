package com.yundian.celebrity.LoginUnitTest.model.net;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.LoginReturnInfo;
import com.yundian.celebrity.bean.RegisterReturnWangYiBeen;
import com.yundian.celebrity.listener.IDataRequestListener;
import com.yundian.celebrity.listener.OnAPIListener;

import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.networkapi.socketapi.SocketUserAPI;
import com.yundian.celebrity.ui.main.model.LoginModel;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.MD5Util;
import com.yundian.celebrity.utils.ToastUtils;

/**
 * Created by sll on 2017/8/2.
 */

public class LoginModelWrapper extends LoginModel {
    public SocketUserAPIWrapper mockSocketUserAPI;
    public void setSocketUserAPI(SocketUserAPIWrapper mockSocketUserAPI){
        this.mockSocketUserAPI=mockSocketUserAPI;
    }
//    因为要用自己的UserAPIWrapper
    public void login(final String userName, String password,SocketUserAPIWrapper userAPI, final IDataRequestListener listener){
        //这里要用封装类
//        SocketUserAPIWrapper userAPI = new SocketUserAPIWrapper();
        //执行封装类的login方法后的回调
        //这里的userAPI应该由外部传进来,放到presenter里,这样可以方便单元测试
        userAPI
                .login(userName, MD5Util.MD5(password), new OnAPIListener<LoginReturnInfo>() {
                    @Override
                    public void onError(Throwable ex) {
                        requestError(listener);
                        LogUtils.logd("登录失败_____" + ex.toString());
                        ToastUtils.showShort("登录失败");

                    }

                    @Override
                    public void onSuccess(final LoginReturnInfo loginReturnInfo) {

                        if (loginReturnInfo.getResult() == -301) {
                            ToastUtils.showShort("用户不存在,请先注册");
                            requestError(listener);
                            return;
                        } else if (loginReturnInfo.getResult() == -302) {
                            ToastUtils.showShort("账号或密码错误");
                            requestError(listener);
                            return;
                        } else if (loginReturnInfo.getResult() == -303) {
                            ToastUtils.showShort("登录信息失效，请重新登录");
                            requestError(listener);
                            return;
                        } else if (loginReturnInfo.getResult() == -304) {
                            ToastUtils.showShort("用户已存在");
                            requestError(listener);
                            return;
                        } else if (loginReturnInfo != null && loginReturnInfo.getUserinfo() != null) {
//					requestServer(listener);

                            LogUtils.logd("登录成功" + loginReturnInfo.toString());

                            wangyiRegister(userName,loginReturnInfo,mockSocketUserAPI,listener);
//                            requestServer(listener);
                        }

                    }
                });

        LogUtils.logi("");
    }

    public void wangyiRegister(String userName, final LoginReturnInfo loginReturnInfo,SocketUserAPIWrapper userAPI, final IDataRequestListener listener){
        //网易云注册   usertype  : 0普通用户 1,明星
        userAPI.registerWangYi(0, userName, userName, loginReturnInfo.getUserinfo().getId(), new OnAPIListener<RegisterReturnWangYiBeen>() {
            @Override
            public void onError(Throwable ex) {
                requestError(listener);
                LogUtils.logd("网易云注册失败" + ex.toString());
                ToastUtils.showShort("网易云注册失败");
            }

            @Override
            public void onSuccess(RegisterReturnWangYiBeen registerReturnWangYiBeen) {

                LogUtils.logd("网易云注册成功" + registerReturnWangYiBeen.getResult_value() + "网易云token" + registerReturnWangYiBeen.getToken_value());
                loginWangYi(loginReturnInfo, registerReturnWangYiBeen,listener);
            }
        });
    }
    private AbortableFuture<LoginInfo> loginRequest;
    private void loginWangYi(final LoginReturnInfo loginReturnInfos, RegisterReturnWangYiBeen registerReturnWangYiBeen ,final IDataRequestListener listener) {
        LogUtils.logd(loginReturnInfos.getUserinfo().getPhone() + "..." + registerReturnWangYiBeen.getToken_value());
        // 登录
        loginRequest = NimUIKit.doLogin(new LoginInfo(loginReturnInfos.getUserinfo().getPhone(), registerReturnWangYiBeen.getToken_value()), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                saveDevice(loginReturnInfos,param,listener);
            }

            @Override
            public void onFailed(int code) {
                requestError(listener);
                if (code == 302 || code == 404) {
                    LogUtils.logd("网易云登录失败" + R.string.login_failed);
                } else {
                    LogUtils.logd("网易云登录失败" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                LogUtils.logd("网易云登录失败" + R.string.login_exception);
            }
        });
    }
}
