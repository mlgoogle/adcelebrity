package com.cloudTop.starshare.networkapi.socketapi;

import com.cloudTop.starshare.app.AppApplication;
import com.cloudTop.starshare.app.AppConfig;
import com.cloudTop.starshare.app.SocketAPIConstant;
import com.cloudTop.starshare.bean.CheckUpdateInfoEntity;
import com.cloudTop.starshare.bean.LoginReturnInfo;
import com.cloudTop.starshare.bean.QiNiuAdressBean;
import com.cloudTop.starshare.bean.RegisterReturnBeen;
import com.cloudTop.starshare.bean.RegisterReturnWangYiBeen;
import com.cloudTop.starshare.bean.RegisterVerifyCodeBeen;
import com.cloudTop.starshare.bean.UptokenBean;
import com.cloudTop.starshare.bean.WXinLoginReturnBeen;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.UserAPI;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketAPINettyBootstrap;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketDataPacket;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.utils.ToastUtils;

import java.util.HashMap;

/**
 * Created by yaowang on 2017/2/20.
 */

public class SocketUserAPI extends SocketBaseAPI implements UserAPI {
    private void isNetBreak() {
        if (!SocketAPINettyBootstrap.getInstance().isOpen()) {
            ToastUtils.showShort("网络连接失败,请检查网络");
        }
    }

    @Override
    public void login(String phone, String password, OnAPIListener<LoginReturnInfo> listener) {
        isNetBreak();
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pwd", password);
        map.put("deviceId",1+"");

        map.put("area_id", AppConfig.AREA_ID);
        map.put("area", AppConfig.AREA);
        map.put("isp_id", AppConfig.ISP_ID);
        map.put("isp", AppConfig.ISP);

        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Login,
                SocketAPIConstant.ReqeutType.User, map);
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
    public void wxLogin(String openid, OnAPIListener<WXinLoginReturnBeen> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("openid", openid);
        map.put("deviceId", AppApplication.getAndroidId());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.WXLogin,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, WXinLoginReturnBeen.class, listener);
    }


    @Override
    public void verifyCode(String phone, OnAPIListener<RegisterVerifyCodeBeen> listener) {
        isNetBreak();
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.VerifyCode,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, RegisterVerifyCodeBeen.class, listener);
    }

    @Override
    public void resetPasswd(String phone, String pwd, OnAPIListener<Object> listener) {
        isNetBreak();
        LogUtils.logd("修改用户密码-----------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pwd", pwd);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.ResetPasswd,
                SocketAPIConstant.ReqeutType.User, map);
        requestJsonObject(socketDataPacket, listener);
    }

    @Override
    public void bindNumber(String phone, String openid, String password, long timeStamp, String vToken, String vCode, String memberId, String agentId, String recommend,String sub_agentId, String nickname, String headerUrl, OnAPIListener<RegisterReturnBeen> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("openid", openid);
        map.put("pwd", password);
        map.put("vCode", vCode);
        map.put("nickname", nickname);
        map.put("headerUrl", headerUrl);
        map.put("timeStamp", timeStamp);
        map.put("vToken", vToken);
        map.put("memberId", memberId);
        map.put("agentId", agentId);
        map.put("sub_agentId", sub_agentId);
        map.put("recommend", recommend);
        map.put("deviceId", AppApplication.getAndroidId());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.WXBind,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, RegisterReturnBeen.class, listener);
    }

    @Override
    public void loginWithToken(long token_time ,OnAPIListener<LoginReturnInfo> listener) {
        LogUtils.loge("用token登录");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("token_time", token_time);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Token,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, LoginReturnInfo.class, listener);
    }

    @Override
    public void isRegisted(String phone, OnAPIListener<RegisterReturnBeen> listener) {
        LogUtils.loge("判断是否注册过");
        HashMap<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.IsRegister,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, RegisterReturnBeen.class, listener);
    }


    @Override
    public void update(OnAPIListener<CheckUpdateInfoEntity> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ttype", 3);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Update,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, CheckUpdateInfoEntity.class, listener);
    }

    @Override
    public void saveDevice(long uid, OnAPIListener<Object> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("device_type", 1);
        map.put("device_id", AppApplication.getAndroidId());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.saveDevice,
                SocketAPIConstant.ReqeutType.User, map);
        requestJsonObject(socketDataPacket, listener);
    }

    @Override
    public void getQiNiuPicDress(OnAPIListener<QiNiuAdressBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.getQiniu,
                SocketAPIConstant.ReqeutType.Time, map);
        requestEntity(socketDataPacket,QiNiuAdressBean.class, listener);
    }

    @Override
    public void getQiNiuToken(OnAPIListener<UptokenBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());

        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.getQiniuToken,
                SocketAPIConstant.ReqeutType.CircleInfo, map);
        requestEntity(socketDataPacket,UptokenBean.class, listener);
    }

}
