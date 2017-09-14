package com.cloudTop.starshare.networkapi.socketapi;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketAPIRequestManage;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketAPIResponse;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketDataPacket;
import com.cloudTop.starshare.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yaowang on 2017/2/20.
 */

public class SocketBaseAPI {

    /**
     * socket请求返回 JSONObject
     * @param socketDataPacket
     * @param listener
     */
    public void requestJsonObject(SocketDataPacket socketDataPacket, final OnAPIListener listener) {
        SocketAPIRequestManage.getInstance().startJsonRequest(socketDataPacket, new OnAPIListener<SocketAPIResponse>() {
            @Override
            public void onError(Throwable ex) {
                SocketBaseAPI.this.onError(listener,ex);
            }

            @Override
            public void onSuccess(SocketAPIResponse socketAPIResponse) {
                //ResultCodeUtil.showEeorMsg(socketAPIResponse);
                SocketBaseAPI.this.onSuccess(listener,socketAPIResponse.jsonObject());
            }
        });
    }

    protected void onError(final OnAPIListener listener, final Throwable ex) {
        if( listener != null ) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listener.onError(ex);
                }
            });
        }
    }

//    public Looper myLooper = Looper.getMainLooper();

    //用于单元测试,解决异步问题
//    public void setLooper(Looper myLooper){
//        this.myLooper=myLooper;
//    }

    protected void onSuccess(final OnAPIListener listener, final Object object) {
        if( listener != null ) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
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
    public void requestEntity(SocketDataPacket socketDataPacket, final Class<?> cls, final OnAPIListener listener) {
        SocketAPIRequestManage.getInstance().startJsonRequest(socketDataPacket, new OnAPIListener<SocketAPIResponse>() {
            @Override
            public void onError(Throwable ex ){
                SocketBaseAPI.this.onError(listener,ex);
            }

            @Override
            public void onSuccess(SocketAPIResponse socketAPIResponse) {
                if( listener != null ) {
                    LogUtils.loge("解析"+socketAPIResponse.jsonObject().toString());
                    Object object = JSON.parseObject(socketAPIResponse.jsonObject().toString(),cls);
                    SocketBaseAPI.this.onSuccess(listener,object);

                }
            }
        });
    }

    /**
     * socket请求返回 List<cls Entity?
     * @param socketDataPacket
     * @param listName 列表字段名
     * @param cls
     * @param listener
     */
    public void requestEntitys(SocketDataPacket socketDataPacket, final String listName, final Class<?> cls, final OnAPIListener listener) {
        SocketAPIRequestManage.getInstance().startJsonRequest(socketDataPacket, new OnAPIListener<SocketAPIResponse>() {
            @Override
            public void onError(Throwable ex) {
                SocketBaseAPI.this.onError(listener,ex);
            }

            @Override
            public void onSuccess(SocketAPIResponse socketAPIResponse) {
                if( listener != null ) {
                    try {
                        //ResultCodeUtil.showEeorMsg(socketAPIResponse);
                        JSONArray jsonArray = socketAPIResponse.jsonObject().getJSONArray(listName);
                        List list = JSON.parseArray(jsonArray.toString(), cls);
                        SocketBaseAPI.this.onSuccess(listener,list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError(e);
                    }
                }
            }
        });
    }

    /**
     * socket请求返回 List<cls Entity?
     * @param socketDataPacket
     * @param listName 列表字段名
     * @param cls
     * @param listener
     */
    public void requestEntitys(SocketDataPacket socketDataPacket, final String listName, final Class<?> cls, final OnAPIListener listener, final String resultCode) {
        SocketAPIRequestManage.getInstance().startJsonRequest(socketDataPacket, new OnAPIListener<SocketAPIResponse>() {
            @Override
            public void onError(Throwable ex) {

                SocketBaseAPI.this.onError(listener,ex);
            }

            @Override
            public void onSuccess(SocketAPIResponse socketAPIResponse) {
                if( listener != null ) {
                    try {
                        String  jsonResult = socketAPIResponse.jsonObject().toString();

//                        JSONObject jsonObject1 = jsonObject.getJSONObject(resultCode);
//                        String string = jsonObject.getString(resultCode);
//                        socketAPIResponse.jsonObject().
//                        jsonObject
                        if(!jsonResult.contains("-658")){
                            //ResultCodeUtil.showEeorMsg(socketAPIResponse);
                            JSONArray jsonArray = socketAPIResponse.jsonObject().getJSONArray(listName);
                            List list = JSON.parseArray(jsonArray.toString(), cls);
                            SocketBaseAPI.this.onSuccess(listener,list);
                        }else {
                            NullPointerException nullPointerException = new NullPointerException("starlists is null");
                            onError(nullPointerException);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError(e);
                    }
                }
            }
        });
    }

    protected SocketDataPacket socketDataPacket(short operateCode,byte type,HashMap<String,Object> map) {
        SocketDataPacket socketDataPacket = null;
        try {
            socketDataPacket = new SocketDataPacket(operateCode, type,map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return socketDataPacket;
    }
}
