package com.cloudTop.starshare.LoginUnitTest.model.net;



import com.cloudTop.starshare.app.SocketAPIConstant;
import com.cloudTop.starshare.bean.CheckUpdateInfoEntity;
import com.cloudTop.starshare.bean.LoginReturnInfo;
import com.cloudTop.starshare.bean.QiNiuAdressBean;
import com.cloudTop.starshare.bean.RegisterReturnBeen;
import com.cloudTop.starshare.bean.RegisterReturnWangYiBeen;
import com.cloudTop.starshare.bean.RegisterVerifyCodeBeen;
import com.cloudTop.starshare.bean.UptokenBean;
import com.cloudTop.starshare.bean.WXinLoginReturnBeen;
import com.cloudTop.starshare.common.SocketBaseAPIWrapper;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.UserAPI;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketDataPacket;

import java.util.HashMap;

/**
 * Created by sll on 2017/8/2.
 */
//必须继承自己的BaseAPIWrapper,最终走到test的onSuccess和onError中
public class SocketUserAPIWrapper extends SocketBaseAPIWrapper implements UserAPI {


    //方法复制粘贴原来的就行了,不要有任何改动,毕竟单元测试
    public void login(String phone, String password, OnAPIListener<LoginReturnInfo> listener) {
        isNetBreak();
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pwd", password);
        map.put("deviceId",1+"");
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Login,
                SocketAPIConstant.ReqeutType.User, map);
//        Looper myLooper = Looper.myLooper();

        requestEntity(socketDataPacket, LoginReturnInfo.class, listener);
    }

    @Override
    public void registerWangYi(int user_type,String phone, String name_value, long uid, OnAPIListener<RegisterReturnWangYiBeen> listener) {
        isNetBreak();
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("uid", uid);
        map.put("user_type", user_type);
        map.put("name_value", name_value);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.WangYi,
                SocketAPIConstant.ReqeutType.Wangyi, map);
        requestEntity(socketDataPacket, RegisterReturnWangYiBeen.class, listener);
    }

    @Override
    public void wxLogin(String openId, OnAPIListener<WXinLoginReturnBeen> listener) {

    }

    @Override
    public void verifyCode(String phone, OnAPIListener<RegisterVerifyCodeBeen> listener) {

    }

    @Override
    public void resetPasswd(String phone, String pwd, OnAPIListener<Object> listener) {

    }

    @Override
    public void bindNumber(String phone, String openid, String password, long timeStamp, String vToken, String vCode, String memberId, String agentId, String recommend, String sub_agentId, String nickname, String headerUrl, OnAPIListener<RegisterReturnBeen> listener) {

    }

    @Override
    public void loginWithToken(long token_time, OnAPIListener<LoginReturnInfo> listener) {

    }

    @Override
    public void isRegisted(String phone, OnAPIListener<RegisterReturnBeen> listener) {

    }

    @Override
    public void update(OnAPIListener<CheckUpdateInfoEntity> listener) {

    }

    @Override
    public void saveDevice(long uid, OnAPIListener<Object> listener) {

    }

    @Override
    public void getQiNiuPicDress(OnAPIListener<QiNiuAdressBean> listener) {

    }

    @Override
    public void getQiNiuToken(OnAPIListener<UptokenBean> listener) {

    }


}
