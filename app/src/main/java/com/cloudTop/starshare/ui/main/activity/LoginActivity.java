package com.cloudTop.starshare.ui.main.activity;

import android.graphics.Point;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.AppApplication;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.EventBusMessage;

import com.cloudTop.starshare.helper.CheckViewHelper;
import com.cloudTop.starshare.ui.main.contract.LoginContract;
import com.cloudTop.starshare.ui.main.presenter.LoginPresenter;
import com.cloudTop.starshare.ui.wangyi.DemoCache;
import com.cloudTop.starshare.ui.wangyi.config.preference.UserPreferences;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.utils.ViewConcurrencyUtils;
import com.cloudTop.starshare.widget.WPEditText;


import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

/**
 * Created by Null on 2017/5/7.
 * 登录
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    private CheckViewHelper checkViewHelper = new CheckViewHelper();

    private long exitNow;
    boolean flag = true;
    private WPEditText userNameEditText;
    private WPEditText passwordEditText;
    private Button loginButton;
    private TextView registerText;
    private TextView tv_retrieve_password;
    private TextView tv_weixin_login;
    private LoginPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        //第一步，new 一个presenter
        presenter = new LoginPresenter(this);
    }

    @Override
    public void initView() {

        initFindById();
        //      if (flag) {
        //          EventBus.getDefault().register(this); // EventBus注册广播()
        //          flag = false;//更改标记,使其不会再进行多次注册
        //      }
        WindowManager.LayoutParams p = getWindow().getAttributes();// 获取对话框当前的参值
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        p.width = (int) (size.x * 0.85);
        getWindow().setAttributes(p); // 设置生效
        userNameEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        checkViewHelper.checkButtonState(loginButton, userNameEditText, passwordEditText);
        String phoneNum = SharePrefUtil.getInstance().getLoginPhone();
        if (!TextUtils.isEmpty(phoneNum)) {
            userNameEditText.getEditText().setText(phoneNum);
        }
    }

    private void initFindById() {
        userNameEditText = (WPEditText) findViewById(R.id.userNameEditText);
        passwordEditText = (WPEditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerText = (TextView) findViewById(R.id.registerText);
        tv_retrieve_password = (TextView) findViewById(R.id.tv_retrieve_password);
        tv_weixin_login = (TextView) findViewById(R.id.tv_weixin_login);
    }



    @OnClick(R.id.loginButton)
    public void loging() {

        if(presenter!=null){
            //ViewConcurrencyUtils.preventConcurrency();  //防止并发
//            LogUtils.loge(MD5Util.MD5(passwordEditText.getEditTextString()));
            String username = userNameEditText.getEditTextString();
            String password=passwordEditText.getEditTextString();

            presenter.login(username,password);

        }
    }

    @Override
    public void update2LoginSuccess() {
            ToastUtils.showShort("登录成功");
            //lggin结束false
            // 初始化消息提醒配置
            initNotificationConfig();
            EventBus.getDefault().postSticky(new EventBusMessage(1));  //登录成功消息
            this.finish();
            this.overridePendingTransition(0, R.anim.activity_off_top_out);

    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    @Override
    public void update2LoginFail() {
            ToastUtils.showShort("登录失败");
    }
    @Override
    public void update2LoginFail(String msg) {
            ToastUtils.showShort(msg);
    }

    @OnClick(R.id.registerText)
    public void doingReregister() {
//        startActivity(RegisterUserActivity.class);
        finish();
        overridePendingTransition(R.anim.activity_open_down_in, R.anim.activity_off_top_out);
    }

    @OnClick(R.id.tv_retrieve_password)
    public void retrievePassword() {
        ViewConcurrencyUtils.preventConcurrency();  //防止并发
        startActivity(ResetUserPwdActivity.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            close();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.tv_weixin_login)
    public void weixinLogin() {
        ViewConcurrencyUtils.preventConcurrency();  //防止并发
        if (!AppApplication.api.isWXAppInstalled()) {
            ToastUtils.showShort("您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        ToastUtils.showShort("微信登录");
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        AppApplication.api.sendReq(req);

    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.recycle();
        }

        //EventBus.getDefault().removeAllStickyEvents();
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

//    //接收消息
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void ReciveMessageEventBus(EventBusMessage eventBusMessage) {
//        switch (eventBusMessage.Message) {
//            case -6:  //成功
//                LogUtils.loge("当前是接收到微信登录成功的消息,finish");
//                finish();
//                break;
//        }
//    }


    @OnClick(R.id.close)
    public void close() {
        EventBus.getDefault().postSticky(new EventBusMessage(2));  //登录取消消息
        finish();
    }
}
