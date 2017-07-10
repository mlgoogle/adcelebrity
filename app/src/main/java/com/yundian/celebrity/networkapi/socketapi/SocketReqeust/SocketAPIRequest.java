package com.yundian.celebrity.networkapi.socketapi.SocketReqeust;

import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIException;
import com.yundian.celebrity.networkapi.socketapi.SocketAPIFactoryImpl;
import com.yundian.celebrity.utils.LogUtils;

import java.util.Date;

/**
 * Created by yaowang on 2017/2/20.
 */

public class SocketAPIRequest {
    private long timestamp = new Date().getTime();
    private OnAPIListener<SocketAPIResponse> listener;

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isReqeustTimeout() {
        return  (new Date().getTime() -  timestamp) > SocketAPIFactoryImpl.getInstance().getConfig().getSocketTimeout();
    }


    public OnAPIListener<SocketAPIResponse> getListener() {
        return listener;
    }

    public void setListener(OnAPIListener<SocketAPIResponse> listener) {
        this.listener = listener;
    }

    public void onSuccess(SocketAPIResponse socketAPIResponse) {
        if( listener != null ) {
            listener.onSuccess(socketAPIResponse);
        }
    }

    public void  onError(Throwable ex) {
        if( listener != null ) {
            listener.onError(ex);
        }
    }


    public void  onErrorCode(int errorCode) {
        LogUtils.logd("-------------------------errorCode:"+errorCode);
        onError(new NetworkAPIException(errorCode,"error"));

    }

}
