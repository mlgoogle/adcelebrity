package com.cloudTop.starshare.common;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.socketapi.SocketBaseAPI;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketAPINettyBootstrap;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketAPIRequestManage;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketAPIResponse;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketDataPacket;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.ToastUtils;

/**
 * Created by sll on 2017/8/2.
 */

public class SocketBaseAPIWrapper extends SocketBaseAPI{
    //为了提高访问权限
    protected void isNetBreak() {
        if (!SocketAPINettyBootstrap.getInstance().isOpen()) {
            ToastUtils.showShort("网络连接失败,请检查网络");
        }
    }

    //重写为了把单元测试中的异步改为同步
    @Override
    protected void onError(final OnAPIListener listener, final Throwable ex) {
        if( listener != null ) {
            new Handler(Looper.myLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listener.onError(ex);
                }
            });
        }
    }
    @Override
    protected void onSuccess(final OnAPIListener listener, final Object object) {
        if( listener != null ) {
            //重写为了把单元测试中的异步改为同步
            new Handler(Looper.myLooper()).post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.logi("进来没有");
                    listener.onSuccess(object);
                }
            });
            LogUtils.logi("过来没有");
        }
    }
    /**
     * socket请求返回 cls Entity
     * @param socketDataPacket
     * @param cls
     * @param listener
     */
    //重写为了调用自己的onSuccess和onError
    public void requestEntity(SocketDataPacket socketDataPacket, final Class<?> cls, final OnAPIListener listener) {
        SocketAPIRequestManage.getInstance().startJsonRequest(socketDataPacket, new OnAPIListener<SocketAPIResponse>() {
            @Override
            public void onError(Throwable ex ){
                SocketBaseAPIWrapper.this.onError(listener,ex);
            }

            @Override
            public void onSuccess(SocketAPIResponse socketAPIResponse) {
                if( listener != null ) {
                    //LogUtils.loge("解析"+socketAPIResponse.jsonObject().toString());
                    Object object = JSON.parseObject(socketAPIResponse.jsonObject().toString(),cls);
                    SocketBaseAPIWrapper.this.onSuccess(listener,object);

                }
            }
        });
    }
}
