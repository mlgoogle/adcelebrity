package com.yundian.celebrity;

import android.app.Application;
import android.text.TextUtils;

import com.yundian.celebrity.networkapi.Host;
import com.yundian.celebrity.networkapi.NetworkAPIConfig;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.networkapi.socketapi.SocketReqeust.SocketAPINettyBootstrap;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.SharePrefUtil;

/**
 * Created by Administrator on 2017/7/11.
 */

public class TestAppliction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        checkNet();
        initNetworkAPIConfig();
    }
    private void initNetworkAPIConfig() {
        NetworkAPIConfig networkAPIConfig = new NetworkAPIConfig();
        networkAPIConfig.setContext(getApplicationContext());
        networkAPIConfig.setSocketServerIp(Host.getSocketServerIp());
        networkAPIConfig.setSocketServerPort(Host.getSocketServerPort());
        NetworkAPIFactoryImpl.initConfig(networkAPIConfig);
    }

    private void checkNet() {
        LogUtils.logd("检测网络-------------------");
        SocketAPINettyBootstrap.getInstance().setOnConnectListener(new SocketAPINettyBootstrap.OnConnectListener() {
            @Override
            public void onExist() {
                LogUtils.logd("检测到链接存在-------------------");
            }

            @Override
            public void onSuccess() {
                LogUtils.logd("检测到连接成功-------------------");
                //token交易暂时关闭
//                judgeIsLogin();
                // checkUpdate();
            }

            @Override
            public void onFailure(boolean tag) {
                LogUtils.logd("检测到连接失败--------------");
                if (tag) {
                    if (!TextUtils.isEmpty(SharePrefUtil.getInstance().getToken())) {
                        LogUtils.logd("检测到连接失败----logout----------");
//                        logout();
                    }
                    // connectionError();
                    //logout();
                }
            }
        });
    }
}
