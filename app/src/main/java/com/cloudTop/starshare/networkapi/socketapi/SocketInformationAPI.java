package com.cloudTop.starshare.networkapi.socketapi;


import com.cloudTop.starshare.app.SocketAPIConstant;
import com.cloudTop.starshare.bean.RequestResultBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.InformationAPI;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketDataPacket;

import java.util.HashMap;

/**
 * Created by ysl.
 */

public class SocketInformationAPI extends SocketBaseAPI implements InformationAPI {

    @Override
    public void checkPayPas(long id, String token, String paypwd, OnAPIListener<RequestResultBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", id);
        map.put("token", token);
        map.put("paypwd", paypwd);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.CheckPayPas,
                SocketAPIConstant.ReqeutType.Pay, map);
        requestEntity(socketDataPacket, RequestResultBean.class, listener);
    }
}
