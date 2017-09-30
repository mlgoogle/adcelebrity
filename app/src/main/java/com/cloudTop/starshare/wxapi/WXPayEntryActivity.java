package com.cloudTop.starshare.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.cloudTop.starshare.app.AppApplication;
import com.cloudTop.starshare.bean.EventBusMessage;
import com.cloudTop.starshare.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api = AppApplication.api;
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            LogUtils.logd("已经接受到了支付的消息:"+resp.errCode);
            if (resp.errCode == 0){
                EventBus.getDefault().postSticky(new EventBusMessage(resp.errCode));  //支付成功,发送广播
            }else{
                EventBus.getDefault().postSticky(new EventBusMessage(resp.errCode));
//                AppManager.getAppManager().finishActivity(RechargeActivity.class);
//                ToastUtils.showShort("您已取消本次充值");
            }
            finish();

        }
    }
}