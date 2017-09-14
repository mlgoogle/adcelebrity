package com.cloudTop.starshare.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.bean.OrderReturnBeen;
import com.cloudTop.starshare.utils.LogUtils;


/**
 * 支付密码弹窗
 */

public class PayDialog extends BaseDialog {

    public PayPwdEditText payPwdEditText;
    private Context context;
    private TextView resetPwd;
    private ImageView img_view;
    private RelativeLayout rl_content;
    private OrderReturnBeen.OrdersListBean ordersListBean;

    public PayDialog(Context context) {
        super(context);
        this.context = context;
    }

    public OrderReturnBeen.OrdersListBean getOrdersListBean() {
        return ordersListBean;
    }

    public void setOrdersListBean(OrderReturnBeen.OrdersListBean ordersListBean) {
        this.ordersListBean = ordersListBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_dialog_lyaout);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        payPwdEditText = (PayPwdEditText) findViewById(R.id.ppet);
        resetPwd = (TextView) findViewById(R.id.tv_reset_pwd);
        img_view = (ImageView) findViewById(R.id.img_view);
        payPwdEditText.initStyle(R.drawable.edit_num_bg_red, 6, 0.33f, R.color.colorAccent, R.color.colorAccent, 20);
        payPwdEditText.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {//密码输入完后的回调
//                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                //校验支付密码



            }

            @Override
            public void onChange(String str) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                payPwdEditText.setFocus();
            }
        }, 100);

        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.loge("重新设置交易密码---------------------");
                payPwdEditText.clearText();
            }
        });

        img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //currentBean = null;
                //KeyBordUtil.popSoftKeyboard(payPwdEditText);
                dismiss();
            }
        });

    }

    public void setLayoutHigh(int v) {
        if (rl_content == null) {
            return;
            //rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        }
        LogUtils.loge("键盘高度" + v);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rl_content.getLayoutParams();
        layoutParams.height = v;
        rl_content.setLayoutParams(layoutParams);
    }

    @Override
    public void show() {
        super.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                payPwdEditText.clearText();
                payPwdEditText.setFocus();
            }
        }, 100);

    }

    @Override
    public void dismiss() {
        ordersListBean = null;
        if (payPwdEditText != null && payPwdEditText.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(payPwdEditText.getWindowToken(), 0);
        }
        super.dismiss();
    }

    public interface checkPasCallBake {
        void checkSuccess(OrderReturnBeen.OrdersListBean ordersListBean);

        void checkError();

        void checkSuccessPwd();

    }

    private checkPasCallBake checkPasCallBake;

    public void setCheckPasCallBake(PayDialog.checkPasCallBake checkPasCallBakes) {
        this.checkPasCallBake = checkPasCallBakes;
    }
}
