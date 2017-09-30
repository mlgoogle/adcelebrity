package com.cloudTop.starshare.networkapi;


import com.cloudTop.starshare.bean.CheckUpdateInfoEntity;
import com.cloudTop.starshare.bean.LoginReturnInfo;
import com.cloudTop.starshare.bean.QiNiuAdressBean;
import com.cloudTop.starshare.bean.RegisterReturnBeen;
import com.cloudTop.starshare.bean.RegisterReturnWangYiBeen;
import com.cloudTop.starshare.bean.RegisterVerifyCodeBeen;
import com.cloudTop.starshare.bean.UptokenBean;
import com.cloudTop.starshare.bean.WXinLoginReturnBeen;
import com.cloudTop.starshare.listener.OnAPIListener;

/**
 * 用户相关API接口
 */

public interface UserAPI {
    // TODO: 2017/8/21
    void login(String phone, String password, OnAPIListener<LoginReturnInfo> listener);

    void registerWangYi(int user_type,String phone, String name, long uid, OnAPIListener<RegisterReturnWangYiBeen> listener);

    //
    void wxLogin(String openId, OnAPIListener<WXinLoginReturnBeen> listener);

    //

    //
    void verifyCode(String phone, OnAPIListener<RegisterVerifyCodeBeen> listener);

    //
    void resetPasswd(String phone, String pwd, OnAPIListener<Object> listener); //修改交易/用户密码

    //
//    void test(int testID, OnAPIListener<Object> listener);
//
//    void loginWithToken(OnAPIListener<LoginReturnEntity> listener);
//
//    void balance(OnAPIListener<BalanceInfoEntity> listener);
//
    void bindNumber(String phone, String openid, String password, long timeStamp, String vToken, String vCode, String memberId, String agentId, String recommend, String sub_agentId, String nickname, String headerUrl, OnAPIListener<RegisterReturnBeen> listener);

    void loginWithToken(long token_time ,OnAPIListener<LoginReturnInfo> listener);

    //是否注册过
    void isRegisted(String phone, OnAPIListener<RegisterReturnBeen> listener);
    void update(OnAPIListener<CheckUpdateInfoEntity> listener);
    void saveDevice(long uid, OnAPIListener<Object> listener);

    void getQiNiuPicDress(OnAPIListener<QiNiuAdressBean> listener);
    void getQiNiuToken(OnAPIListener<UptokenBean> listener);
}
