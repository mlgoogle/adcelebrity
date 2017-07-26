package com.yundian.celebrity.ui.main.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.bean.BankInfoBean;
import com.yundian.celebrity.helper.CheckHelper;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.utils.CountUtil;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.SharePrefUtil;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.CheckException;
import com.yundian.celebrity.widget.NormalTitleBar;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by sll on 2017/7/5.
 */

public class AddBankCardActvivity extends BaseActivity {


    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.et_user_name)
    EditText etUserName;
    @Bind(R.id.et_user_cardno)
    EditText etUserCardno;
    @Bind(R.id.et_user_phone)
    EditText etUserPhone;
    @Bind(R.id.et_code_msg)
    EditText etCodeMsg;
    @Bind(R.id.btn_get_code)
    Button btnGetCode;
    @Bind(R.id.btn_bind_bank)
    Button btnBindBank;
    private CheckHelper checkHelper = new CheckHelper();

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_bank_card;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initData();
    }

    private void initData() {
        ntTitle.setTitleText(getString(R.string.bank_info));
        checkHelper.checkButtonState1(btnBindBank, etUserName, etUserCardno, etUserPhone, etCodeMsg);
    }

    @OnClick({R.id.btn_get_code, R.id.btn_bind_bank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:
                getCodeMsg();
                break;
            case R.id.btn_bind_bank:
                bindBankCard();
                break;
        }
    }

    private void bindBankCard() {
        //判断全部非空
        if (etUserName.getText().toString().trim().isEmpty() ||
                etUserCardno.getText().toString().trim().isEmpty() ||
                etUserPhone.getText().toString().trim().isEmpty() ||
                etCodeMsg.getText().toString().trim().isEmpty()) {
            ToastUtils.showShort("输入不能为空");
            return;
        }

        CheckException exception = new CheckException();
        //判断手机格式是否符合格式
        if (checkHelper.checkMobile(etUserPhone.getText().toString(), exception)) {


            String bankUsername = etUserName.getText().toString().trim();
            String account = etUserCardno.getText().toString().trim();
            String phone = etUserPhone.getText().toString().trim();
            String codeMsg = etCodeMsg.getText().toString().trim();


            NetworkAPIFactoryImpl.getDealAPI().bindCard(bankUsername, account, new OnAPIListener<BankInfoBean>() {
                @Override
                public void onError(Throwable ex) {
                }

                @Override
                public void onSuccess(BankInfoBean bean) {
                    LogUtils.loge("绑定成功");
//                    EventBus.getDefault().postSticky(new EventBusMessage(-3));  //传递消息
                    if (bean.getBankName() != null && bean.getCardNO() != null && bean.getName() != null) {
//                        把银行卡存一下
                        SharePrefUtil.getInstance().saveCardNo(bean.getCardNO());
                        ToastUtils.showStatusView("绑定成功", true);
//                        AppManager.getAppManager().finishActivity(BankCardInfoActivity.class);
                        finish();
                    }

                }
            });
        } else {
            exception.getErrorMsg();
        }
    }

    private void getCodeMsg() {
        if (etUserName.getText().toString().trim().isEmpty() ||
                etUserCardno.getText().toString().trim().isEmpty() ||
                etUserPhone.getText().toString().trim().isEmpty()) {
            ToastUtils.showShort("输入不能为空");
            return;
        }
        new CountUtil(btnGetCode).start();   //收到回调才开启计时
        //获取验证码

    }
}
