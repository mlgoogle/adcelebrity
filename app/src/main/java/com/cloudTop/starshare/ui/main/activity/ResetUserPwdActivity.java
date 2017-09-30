package com.cloudTop.starshare.ui.main.activity;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.RegisterReturnBeen;
import com.cloudTop.starshare.bean.RegisterVerifyCodeBeen;
import com.cloudTop.starshare.helper.CheckInfoHelper;
import com.cloudTop.starshare.helper.CheckViewHelper;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIException;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketAPINettyBootstrap;
import com.cloudTop.starshare.utils.CountUtil;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.MD5Util;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.widget.CheckException;
import com.cloudTop.starshare.widget.NormalTitleBar;
import com.cloudTop.starshare.widget.WPEditText;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/10.
 *  #11
 */

public class ResetUserPwdActivity extends BaseActivity {

    @Bind(R.id.nt_title)
    NormalTitleBar nt_title;
    @Bind(R.id.phoneEditText)
    WPEditText phoneEditText;
    @Bind(R.id.msgEditText)
    WPEditText msgEditText;
    @Bind(R.id.pwdEditText1)
    WPEditText pwdEditText1;
    @Bind(R.id.pwdEditText2)
    WPEditText pwdEditText2;
    @Bind(R.id.okButton)
    Button okButton;

    private RegisterVerifyCodeBeen verifyCodeBeen;

    private CheckViewHelper checkHelper = new CheckViewHelper();
    private CheckInfoHelper checkInfoHelper = new CheckInfoHelper();

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_user_pwd;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        nt_title.setTvLeftVisiable(true);
        nt_title.setTitleText(getString(R.string.butten_reset_psd));
        phoneEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        msgEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        //当验证码接口有了后才用下面的方法
        checkHelper.checkButtonState(okButton, phoneEditText, msgEditText, pwdEditText1, pwdEditText2);
        checkHelper.checkVerificationCode(msgEditText.getRightText(), phoneEditText);
        /*msgEditText.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.logd("此时网络的连接状态是:" + SocketAPINettyBootstrap.getInstance().isOpen());
                int verifyType = 1;// 0-注册 1-登录 2-更新服务
                VerifyCodeUtils.getCode(msgEditText, verifyType, context, v, phoneEditText);
            }
        });*/
        msgEditText.getRightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SocketAPINettyBootstrap.getInstance().isOpen()) {
                    ToastUtils.showShort("网络连接失败,请检查网络");
                    return;
                }
                judgeRegister();  //首先判断用户有没有注册
            }
        });
        nt_title.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @OnClick(R.id.okButton)
    public void okButton() {
        LogUtils.logd("此时网络的连接状态是:" + SocketAPINettyBootstrap.getInstance().isOpen());
        CheckException exception = new CheckException();
        if (checkInfoHelper.checkMobile(phoneEditText.getEditTextString(), exception)
                && checkInfoHelper.checkMobile(phoneEditText.getEditTextString(), exception)
                && checkInfoHelper.checkPassword(pwdEditText1.getEditTextString(), exception)
                && checkInfoHelper.checkPassword2(pwdEditText1.getEditTextString(), pwdEditText2.getEditTextString(), exception)) {
            resetUserPwd();

        } else {
            showShortToast(exception.getErrorMsg());
        }

    }

    private void resetUserPwd() {
        //本地校验验证码   MD5(yd1742653sd + code_time + rand_code + phone)
        if (verifyCodeBeen == null || TextUtils.isEmpty(verifyCodeBeen.getVToken())) {
            ToastUtils.showShort("无效验证码");
            return;
        }
        if (!verifyCodeBeen.getVToken().equals(MD5Util.MD5("yd1742653sd" + verifyCodeBeen.getTimeStamp() + msgEditText.getEditTextString() + phoneEditText.getEditTextString()))) {
            ToastUtils.showShort("验证码错误,请重新输入");
            return;
        }
        //int type = 0;//0：登录密码 1：交易密码，提现密码
        if (!TextUtils.equals(pwdEditText1.getEditTextString(), pwdEditText2.getEditTextString())) {
            ToastUtils.showShort("2次密码不一致");
            return;
        }
        NetworkAPIFactoryImpl.getUserAPI().resetPasswd(phoneEditText.getEditTextString(), MD5Util.MD5(pwdEditText2.getEditTextString())
                , new OnAPIListener<Object>() {
                    @Override
                    public void onError(Throwable ex) {
                        ex.printStackTrace();
                        ToastUtils.showStatusView("修改登录密码失败", false);
                    }

                    @Override
                    public void onSuccess(Object o) {
                        JSONObject object = JSON.parseObject(o.toString());
                        int result = object.getInteger("result");
                        if (result == -301) {
                            ToastUtils.showShort("用户不存在");
                        } else if (result == 1) {
                            ToastUtils.showStatusView("修改登录密码成功", true);
                            finish();
                            startActivity(LoginActivity.class);
                            overridePendingTransition(R.anim.activity_open_down_in, R.anim.activity_off_top_out);
                        }

                    }
                });
    }

    private void getCode() {
        LogUtils.logd("请求网络获取短信验证码------------------------------");
        CheckException exception = new CheckException();
        String phoneEdit = phoneEditText.getEditTextString();
        if (new CheckInfoHelper().checkMobile(phoneEdit, exception)) {
            //Utils.closeSoftKeyboard(view);
            NetworkAPIFactoryImpl.getUserAPI().verifyCode(phoneEdit, new OnAPIListener<RegisterVerifyCodeBeen>() {
                @Override
                public void onError(Throwable ex) {
                    ex.printStackTrace();
                    LogUtils.logd("验证码请求网络错误------------------" + ((NetworkAPIException) ex).getErrorCode());
                }

                @Override
                public void onSuccess(RegisterVerifyCodeBeen o) {
                    verifyCodeBeen = o;
                    new CountUtil(msgEditText.getRightText()).start();   //收到回调才开启计时
                    LogUtils.logd("获取到--注册短信验证码,时间戳是:" + o.toString());
                }
            });
        } else {
            ToastUtils.showShort(exception.getErrorMsg());
        }
    }

    private void judgeRegister() {
        startProgressDialog();
        CheckException exception = new CheckException();
        String phoneEdit = phoneEditText.getEditTextString();
        if (new CheckInfoHelper().checkMobile(phoneEdit, exception)) {
            NetworkAPIFactoryImpl.getUserAPI().isRegisted(phoneEdit, new OnAPIListener<RegisterReturnBeen>() {
                @Override
                public void onError(Throwable ex) {
                    stopProgressDialog();
                    LogUtils.loge("错误--------------");
                    ToastUtils.showShort("网络异常,请检查网络连接");
                }

                @Override
                public void onSuccess(RegisterReturnBeen registerReturnBeen) {
                    stopProgressDialog();
                    if (registerReturnBeen.getResult() == 1) {
                        getCode();  //已经注册,获取验证码
                    } else if (registerReturnBeen.getResult() == 0) {
                        ToastUtils.showShort("手机号码未注册,请先注册");
                    }

                }
            });
        } else {
            ToastUtils.showShort(exception.getErrorMsg());
        }
    }
}
