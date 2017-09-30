package com.cloudTop.starshare.networkapi.socketapi;


import com.cloudTop.starshare.app.SocketAPIConstant;
import com.cloudTop.starshare.bean.AliPayReturnBean;
import com.cloudTop.starshare.bean.AssetDetailsBean;
import com.cloudTop.starshare.bean.BankCardBean;
import com.cloudTop.starshare.bean.BankInfoBean;
import com.cloudTop.starshare.bean.BookingStarListBean;
import com.cloudTop.starshare.bean.CircleFriendBean;
import com.cloudTop.starshare.bean.FansAskBean;
import com.cloudTop.starshare.bean.HaveStarUsersBean;
import com.cloudTop.starshare.bean.IdentityInfoBean;
import com.cloudTop.starshare.bean.IncomeReturnBean;
import com.cloudTop.starshare.bean.MeetOrderListBean;
import com.cloudTop.starshare.bean.MoneyDetailListBean;
import com.cloudTop.starshare.bean.OrderListReturnBean;
import com.cloudTop.starshare.bean.RequestResultBean;
import com.cloudTop.starshare.bean.ResultBeen;
import com.cloudTop.starshare.bean.ResultCodeBeen;
import com.cloudTop.starshare.bean.SubmitAddressTimeInfo;
import com.cloudTop.starshare.bean.WXPayReturnEntity;
import com.cloudTop.starshare.bean.WithDrawCashHistoryBean;
import com.cloudTop.starshare.bean.WithDrawCashReturnBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.DealAPI;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketDataPacket;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yaowang on 2017/2/20.
 */

public class SocketDealAPI extends SocketBaseAPI implements DealAPI {

    @Override
    public void weixinPay(String title, double price, OnAPIListener<WXPayReturnEntity> listener) {
        LogUtils.logd("请求微信支付");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("title", title);
        map.put("price", price);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.WXPay,
                SocketAPIConstant.ReqeutType.Pay, map);
        requestEntity(socketDataPacket, WXPayReturnEntity.class, listener);
    }

    @Override
    public void moneyList(String time, int status, int count, int startPos, OnAPIListener<List<MoneyDetailListBean>> listener) {
        LogUtils.logd("请求钱包明细");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken()); //临时写死
        map.put("status", 0); //(1:处理中,2:成功,3:失败),不传则查所有状态
        map.put("count", count);
        map.put("startPos", startPos);
        map.put("time", time);  //time  不传是获取所有  1 2 3
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.MoneyDetail,
                SocketAPIConstant.ReqeutType.History, map);
        requestEntitys(socketDataPacket, "depositsinfo", MoneyDetailListBean.class, listener);
    }

    @Override
    public void identityAuthentication(String realname, String id_card, OnAPIListener<RequestResultBean> listener) {
        LogUtils.logd("实名认证--");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("realname", realname);
        map.put("id_card", id_card);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Identity,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, RequestResultBean.class, listener);
    }

    @Override
    public void dealPwd(String phone, String vToken, String vCode, long timestamp, int type, String pwd, OnAPIListener<RequestResultBean> listener) {
        LogUtils.logd("交易密码--");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
//        map.put("vToken", SharePrefUtil.getInstance().getToken());
        map.put("phone", phone);
        map.put("vToken", vToken);
        map.put("vCode", vCode);
        map.put("timestamp", timestamp);  //时间戳
        map.put("type", type);
        map.put("pwd", pwd);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.DealPwd,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, RequestResultBean.class, listener);
    }

    @Override
    public void test(String title, double price, OnAPIListener<Object> listener) {
        LogUtils.logd("测试端口");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Products,
                SocketAPIConstant.ReqeutType.Deal, map);
        requestJsonObject(socketDataPacket, listener);
    }

    @Override
    public void balance(OnAPIListener<AssetDetailsBean> listener) {
        LogUtils.logd("请求资产明细---");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Balance,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, AssetDetailsBean.class, listener);
    }

    @Override
    public void identity(OnAPIListener<IdentityInfoBean> listener) {
        LogUtils.loge("身份信息");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.IdentityInfo,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, IdentityInfoBean.class, listener);
    }

    @Override
    public void nikeName(String nickname, OnAPIListener<RequestResultBean> listener) {
        LogUtils.loge("设置昵称--------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("nickname", nickname);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.NikeName,
                SocketAPIConstant.ReqeutType.User, map);
        requestEntity(socketDataPacket, RequestResultBean.class, listener);
    }

    @Override
    public void starMeet(String starcode, long mid, String city_name, String appoint_time, int meet_type, String comment, OnAPIListener<RequestResultBean> listener) {
        LogUtils.loge("设置开始约见--------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", SharePrefUtil.getInstance().getUserId());
        map.put("mid", mid);
        map.put("starcode", starcode);
        map.put("city_name", city_name);
        map.put("appoint_time", appoint_time);
        map.put("meet_type", meet_type);
        map.put("comment", comment);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.StarMeet,
                SocketAPIConstant.ReqeutType.NewInfos, map);
        requestEntity(socketDataPacket, RequestResultBean.class, listener);
    }

    @Override
    public void alipay(String title, double price, OnAPIListener<AliPayReturnBean> listener) {
        LogUtils.loge("支付宝----------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("title", title);
        map.put("price", price);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.ALiPay,
                SocketAPIConstant.ReqeutType.Pay, map);
        requestEntity(socketDataPacket, AliPayReturnBean.class, listener);
    }

    @Override
    public void cancelPay(String rid, int payResult, OnAPIListener<Object> listener) {
        LogUtils.loge("取消支付----------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("rid", rid);
        map.put("payResult", payResult);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.CancelPay,
                SocketAPIConstant.ReqeutType.Pay, map);
//        requestEntity(socketDataPacket,RequestResultBean.class,listener);
        requestJsonObject(socketDataPacket, listener);
    }

    @Override
    public void cashOut(double price, String withdrawPwd, OnAPIListener<WithDrawCashReturnBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("price", price);
        map.put("withdrawPwd", withdrawPwd);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.CashOut,
                SocketAPIConstant.ReqeutType.Pay, map);
        requestEntity(socketDataPacket, WithDrawCashReturnBean.class, listener);
    }

    @Override
    public void requestIncome(String starcode, int stardate, int enddate, OnAPIListener<List<IncomeReturnBean>> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("starcode", starcode);
        map.put("stardate", stardate);
        map.put("enddate", enddate);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Income,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        LogUtils.loge(map.toString());

        requestEntitys(socketDataPacket, "OrderList", IncomeReturnBean.class, listener,"result");
    }

    @Override
    public void yesterdayIncome(String starcode, int orderdate, OnAPIListener<IncomeReturnBean> listener) {
        LogUtils.loge("昨收今开---------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("starcode", starcode);
        map.put("orderdate", orderdate);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.YesterdayIncome,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        requestEntity(socketDataPacket, IncomeReturnBean.class, listener);
    }

    @Override
    public void orderList(String starcode, OnAPIListener<List<OrderListReturnBean>> listener) {
        LogUtils.loge("所有活动类型---------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("starcode", starcode);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.OrderList,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        requestEntitys(socketDataPacket, "OrderList",OrderListReturnBean.class, listener);
    }

    @Override
    public void haveOrderType(String starcode, OnAPIListener<List<OrderListReturnBean>> listener) {
        LogUtils.loge("明星拥有活动类型---------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("starcode", starcode);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.HaveOrderList,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        requestEntitys(socketDataPacket, "OrderList",OrderListReturnBean.class, listener,"result");
    }

    @Override
    public void updateOrderType(String starcode, int mid, int type, OnAPIListener<RequestResultBean> listener) {
        LogUtils.loge("修改约见类型---------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("starcode", starcode);
        map.put("mid", mid);
        map.put("type", type);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.UpdateType,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        requestEntity(socketDataPacket,RequestResultBean.class, listener);
    }

    @Override
    public void cashList(int status, int startPos, int count, OnAPIListener<List<WithDrawCashHistoryBean>> listener) {
        LogUtils.loge("提现列表请求网络---------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("startPos", startPos);
        map.put("count", count);
        map.put("status", status);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.CashList,
                SocketAPIConstant.ReqeutType.History, map);
        requestEntitys(socketDataPacket, "withdrawList", WithDrawCashHistoryBean.class, listener);
    }

    @Override
    public void bankCardList(OnAPIListener<BankCardBean> listener) {
        LogUtils.loge("银行卡信息");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.BankCard,
                SocketAPIConstant.ReqeutType.Bank, map);
        requestEntity(socketDataPacket, BankCardBean.class, listener);
    }
    @Override
    public void bankCardInfo(String cardNo, OnAPIListener<BankInfoBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("cardNo", cardNo);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.BankName,
                SocketAPIConstant.ReqeutType.Bank, map);
        requestEntity(socketDataPacket, BankInfoBean.class, listener);
    }

    @Override
    public void bindCard(String bankUsername, String account,String prov,String city,OnAPIListener<BankInfoBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("bankUsername", bankUsername);
        map.put("prov", prov);
        map.put("city", city);
        map.put("account", account);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.BindCard,
                SocketAPIConstant.ReqeutType.Bank, map);
        requestEntity(socketDataPacket, BankInfoBean.class, listener);
    }

    @Override
    public void unBindCard(OnAPIListener<ResultCodeBeen> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.UnBindCard,
                SocketAPIConstant.ReqeutType.Bank, map);
        requestEntity(socketDataPacket, ResultCodeBeen.class, listener);
    }
//临时模拟数据
    @Override
    public void bookingStarList(int startPos, int count, OnAPIListener<List<BookingStarListBean>> listener) {
        LogUtils.logd("请求预约明星列表");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
//        map.put("id", 120);
//        map.put("token", "deef1f3d463139a1c50d366c63b22687");
        map.put("count", count);
        map.put("startPos", startPos);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.BookingStar,
                SocketAPIConstant.ReqeutType.History, map);
        requestEntitys(socketDataPacket, "depositsinfo", BookingStarListBean.class, listener);
    }

    @Override
    public void meetOrderList(String starcode,int startPos, int count, OnAPIListener<List<MeetOrderListBean>> listener) {
        LogUtils.logd("约见订单列表");
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("starcode", starcode);
        map.put("starPos", startPos);
        map.put("count", count);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.MeetOrderList,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        requestEntitys(socketDataPacket, "OrderList", MeetOrderListBean.class, listener);
    }

    @Override
    public void agreeMeet(String starcode, int meettype, int meetid, OnAPIListener<RequestResultBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("starcode", starcode);
        map.put("meettype", meettype);
        map.put("meetid", meetid);
        LogUtils.logd("agreeMeet"+map.toString());
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.AgreeMeet,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        requestEntity(socketDataPacket, RequestResultBean.class, listener);
    }

    @Override
    public void fansList(String starcode, int startPos, int count, OnAPIListener<List<HaveStarUsersBean>> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("starcode", starcode);
        map.put("starPos", startPos);
        map.put("count", count);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.FansList,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        requestEntitys(socketDataPacket,"OrderList", HaveStarUsersBean.class, listener);
    }

    @Override
    public void fanAskList(String starCode,int pos, int count,int type,int pType, OnAPIListener<List<FansAskBean>> listener) {
        HashMap<String, Object> map = new HashMap<>();
        int userId = SharePrefUtil.getInstance().getUserId();

        map.put("starcode", starCode);
//        map.put("starcode", userId);
        map.put("token", SharePrefUtil.getInstance().getToken());

        map.put("count", count);
        map.put("uid", userId);
        map.put("aType", type);
        map.put("pType", pType);
        map.put("pos", pos);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.FansAskList,
                SocketAPIConstant.ReqeutType.CircleInfo, map);
        requestEntitys(socketDataPacket,"circle_list", FansAskBean.class, listener);
    }

    @Override
    public void starCommentUid(String star_code, long circle_id, long uid, int direction, String content, OnAPIListener<ResultBeen> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("star_code", star_code);
        map.put("circle_id", circle_id);
        map.put("uid", uid);
        map.put("direction", direction);
        map.put("content", content);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.StarAddComment,
                SocketAPIConstant.ReqeutType.CircleInfo, map);
        requestEntity(socketDataPacket,ResultBeen.class,listener);
    }

    @Override
    public void getPraisestar(String star_code, long circle_id, long uid, OnAPIListener<ResultBeen> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("star_code", star_code);
        map.put("circle_id", circle_id);
        map.put("uid", uid);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.PresenterStar,
                SocketAPIConstant.ReqeutType.CircleInfo, map);
        requestEntity(socketDataPacket,ResultBeen.class,listener);
    }

    @Override
    public void getAllCircleInfo(int pos, int count, OnAPIListener<CircleFriendBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pos", pos);
        map.put("count", count);
        map.put("star_code",SharePrefUtil.getInstance().getStarcode() );
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.CircleFrindInfo,
                SocketAPIConstant.ReqeutType.CircleInfo, map);
        requestEntity(socketDataPacket,CircleFriendBean.class,listener);
    }

    @Override
    public void publishState(String content, String picurl, String star_code, OnAPIListener<RequestResultBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("picurl", picurl);
        map.put("star_code", star_code);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.Publish,
                SocketAPIConstant.ReqeutType.CircleInfo, map);
        requestEntity(socketDataPacket,RequestResultBean.class,listener);
    }

    @Override
    public void publishAnswer(int questionId , int pType,String Answer,   OnAPIListener<RequestResultBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", questionId);
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("pType", pType);
        map.put("sanswer", Answer);

        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.PublishAnswer,
                SocketAPIConstant.ReqeutType.CircleInfo, map);
        requestEntity(socketDataPacket,RequestResultBean.class,listener);
    }

    @Override
    public void publishVideoAnswer(int questionId , int pType,String Answer,String frameImg,   OnAPIListener<RequestResultBean> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", questionId);
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("pType", pType);
        map.put("sanswer", Answer);
        map.put("thumbnailS", frameImg);

        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.PublishAnswer,
                SocketAPIConstant.ReqeutType.CircleInfo, map);
        requestEntity(socketDataPacket,RequestResultBean.class,listener);
    }

    @Override
    public void submitAddressTimeInfo(String address, String startDate, String endDate, OnAPIListener<SubmitAddressTimeInfo> listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharePrefUtil.getInstance().getUserId());
        map.put("starcode", SharePrefUtil.getInstance().getStarcode());
        map.put("token", SharePrefUtil.getInstance().getToken());
        map.put("meet_city", address);
        map.put("startdate", startDate);
        map.put("enddate", endDate);
        SocketDataPacket socketDataPacket = socketDataPacket(SocketAPIConstant.OperateCode.AddressTimeManage,
                SocketAPIConstant.ReqeutType.StarIncome, map);
        requestEntity(socketDataPacket, SubmitAddressTimeInfo.class, listener);
    }
}
