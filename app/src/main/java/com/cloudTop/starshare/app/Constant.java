package com.cloudTop.starshare.app;

import com.cloudTop.starshare.R;

public interface Constant {

    String[] mTitles = {"收益情况", "联系粉丝", "约见管理","粉丝问答"};
    int[] mIconUnselectIds = {
            R.drawable.market_no_ok, R.drawable.differ_answer_no_ok, R.drawable.icon_manager_normal,R.drawable.icon_ask_normal};
    int[] mIconSelectIds = {
            R.drawable.market_ok, R.drawable.differ_answer_ok, R.drawable.icon_manager_sel,R.drawable.icon_ask_sel};

    String[] rechargeType = new String[]{"微信支付", "支付宝支付"};
    String[] handleText = new String[]{" 未操作 ", " 双倍返还 ", " 货运 ", " 退舱 "};
    String APP_ID = "wx9dc39aec13ee3158";
    String SECRET = "a12a88f2c4596b2726dd4ba7623bc27e";
    String BUG_OUT_ID = "1664ea921dcbe122834e440f7f584e2e";

    String USER_ENTITY = "user_entity";
    String USER_PWD = "user_pwd";
    String PAY_PWD = "pay_pwd";

    interface IntentKey {
        String CHOOSE_IMGS_SCAN = "CHOOSE_IMGS_SCAN";
        String CHOOSE_IMGS_RES = "CHOOSE_IMGS_RES";
        String IMGS_BIG = "IMGS_BIG";
        String IMGS_LIST = "IMGS_LIST";
        String IMGS_POSITION = "IMGS_POSITION";
    }

    int TYPE_BUY_MINUS = 0; //买跌
    int TYPE_BUY_PLUS = 1; //买涨
    int TYPE_INSUFFICIENT_BALANCE = 2; //余额不足

    //    60-1分钟K线，300-5分K线，900-15分K线，1800-30分K线，3600-60分K线，5-日K线
    int MIN_LINE1 = 0;
    int MIN_LINE5 = 300;
    int MIN_LINE15 = 900;
    int MIN_LINE30 = 1800;
    int MIN_LINE60 = 3600;

    int ACTION_NONE = 0;
    int ACTION_DOUBLE = 1;
    int ACTION_FREIGHT = 2;  //货运
    int ACTION_RETURN = 3;

    interface payType {
        String WECHAT_QRCODE_PAY = "WECHAT_QRCODE_PAY";  //微信扫码支付
        String ALIPAY_QRCODE_PAY = "ALIPAY_QRCODE_PAY";//支付宝扫码
        String QQ_QRCODE_PAY = "QQ_QRCODE_PAY"; //QQ钱包扫码
        String JD_QRCODE_PAY = "JD_QRCODE_PAY";  //京东钱包
        String UNION_PAY_QRCODE_PAY = "UNION_PAY_QRCODE_PAY"; //银联
        String H5_ONLINE_BANK_PAY = "H5_ONLINE_BANK_PAY"; //网关  H5_ONLINE_BANK_PAY
        String WECHAT_JSAPI_PAY = "WECHAT_JSAPI_PAY"; //微信公众号
        String ALIPAY_JSAPI_PAY = "ALIPAY_JSAPI_PAY"; //支付宝服务窗
        String DEBIT_BANK_CARD_PAY = "DEBIT_BANK_CARD_PAY"; //储蓄卡(借记卡)支付
        String CREDIT_BANK_CARD_PAY = "CREDIT_BANK_CARD_PAY"; //信用卡支付
    }

    String STATUS_PYAED = "PYAED";  //代付成功
    String STATUS_PAYING = "PAYING"; //代付中
    String STATUS_PAY_FAILED = "PAY_FAILED"; //代付失败
    String STATUS_REFUND = "REFUND";//已退款

    String ABOUT_US_URL = "http://122.144.169.219:3389/aboutStar";
    String DEAL_RULE_URL = "https://www.baidu.com/";

    String INCOME_INFO = "income_info";
    String INCOME = "income";
    String MEET_TYPE = "MEET_TYPE";
    String MEET_TYPE_DETAIL = "MEET_TYPE_DETAIL";
    String QI_NIU_TOKEN_URL = "http://122.144.169.219:3378/imageToken";
//    String QI_NIU_BASE_URL = "http://ot81955n7.bkt.clouddn.com/";
    String QI_NIU_BASE_URL = "http://ouim6qew1.bkt.clouddn.com/";


}
