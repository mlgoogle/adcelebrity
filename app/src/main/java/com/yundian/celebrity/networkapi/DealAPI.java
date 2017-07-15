package com.yundian.celebrity.networkapi;


import com.yundian.celebrity.bean.AliPayReturnBean;
import com.yundian.celebrity.bean.AssetDetailsBean;
import com.yundian.celebrity.bean.BankCardBean;
import com.yundian.celebrity.bean.BankInfoBean;
import com.yundian.celebrity.bean.BookingStarListBean;
import com.yundian.celebrity.bean.IdentityInfoBean;
import com.yundian.celebrity.bean.MoneyDetailListBean;
import com.yundian.celebrity.bean.RequestResultBean;
import com.yundian.celebrity.bean.ResultCodeBeen;
import com.yundian.celebrity.bean.WXPayReturnEntity;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import com.yundian.celebrity.bean.WithDrawCashReturnBean;
import com.yundian.celebrity.listener.OnAPIListener;

import java.util.List;

/**
 * Created by yaowang on 2017/2/20.
 * 交易 行情相关接口
 */

public interface DealAPI {

    //    //微信支付
    void weixinPay(String title, double price, OnAPIListener<WXPayReturnEntity> listener);

    //
//    void unionPay(String title, double price, OnAPIListener<Object> listener);  //银联支付
//
//    void payment(String outTradeNo, long amount, String content, String payType, OnAPIListener<UnionPayReturnEntity> listener);  //第三方支付
//
//    //提现
//    void cash(double money, long cardId, String pwd, OnAPIListener<WithDrawCashReturnEntity> listener);
//
//    //第三方  提现
//    void cashOut(long bid, long amount, String receiverBankName,
//                 String receiverBranchBankName, String receiverCardNo, String receiverAccountName,
//                 OnAPIListener<CashOutReturnEntity> listener);
//
//    //提现列表
    void cashList(int status, int startPos, int count, OnAPIListener<List<WithDrawCashHistoryBean>> listener);

    //
//    //提现列表
//    void currentPosition(double pid, OnAPIListener<CurrentPositionEntity> listener);
//
//    //提现列表
//    void profit(long tid, int handle, OnAPIListener<HistoryPositionListReturnEntity> listener);
//
//    void wxpayResult(String rid, int payResult, OnAPIListener<WXPayResultEntity> listener);//支付结果
//
    void bankCardList(OnAPIListener<BankCardBean> listener);//银行卡列表

    //
    void bankCardInfo(String cardNo, OnAPIListener<BankInfoBean> listener);//获取银行账户信息

    //
    void bindCard(String bankUsername, String account, OnAPIListener<BankInfoBean> listener);//获取银行账户信息

    void unBindCard(OnAPIListener<ResultCodeBeen> listener);//解绑操作

    //
//    void rechargeList(int startPos, int count, OnAPIListener<List<RechargeRecordItemEntity>> listener);//解绑操作
    void moneyList(String time, int status, int count, int startPos, OnAPIListener<List<MoneyDetailListBean>> listener);//资金明细

    void bookingStarList(int startPos, int count, OnAPIListener<List<BookingStarListBean>> listener);//预约明星列表

    void identityAuthentication(String realname, String id_card, OnAPIListener<RequestResultBean> listener);

    void dealPwd(String phone, String vToken, String vCode, long timestamp, int type, String pwd, OnAPIListener<RequestResultBean> listener);//预约明星列表

    void test(String title, double price, OnAPIListener<Object> listener);//预约明星列表

    void balance(OnAPIListener<AssetDetailsBean> listener);//余额

    void identity(OnAPIListener<IdentityInfoBean> listener);//身份

    void nikeName(String nickname, OnAPIListener<RequestResultBean> listener);//身份

    void starMeet(String starcode, long mid, String city_name, String appoint_time, int meet_type, String comment, OnAPIListener<RequestResultBean> listener);

//    void statServiceList(String starcode, OnAPIListener<StatServiceListBean> listener);

    void alipay(String title, double price, OnAPIListener<AliPayReturnBean> listener);

    void cancelPay(String rid, int payResult, OnAPIListener<Object> listener);

    void cashOut(double price, String withdrawPwd, OnAPIListener<WithDrawCashReturnBean> listener);

//    void meetStatus(int pos, int count, OnAPIListener<MeetStarStatusBean> listener);
}
