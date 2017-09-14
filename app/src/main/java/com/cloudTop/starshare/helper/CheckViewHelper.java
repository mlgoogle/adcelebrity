package com.cloudTop.starshare.helper;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.cloudTop.starshare.utils.StringUtil;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.widget.CheckException;
import com.cloudTop.starshare.widget.SimpleTextWatcher;
import com.cloudTop.starshare.widget.WPEditText;


/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2016-11-23 10:02
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public class CheckViewHelper {

    private static final String EMPEY_VERIFY_CODE = "短信验证码不能为空";
    private static final String LENGTH_MAX_VERIFY = "验证码不能大于10个字符";


    public void checkVerificationCode(final View button, final WPEditText editText) {

        //button.setEnabled(!StringUtil.isEmpty(editText.getEditTextString()));
        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                //button.setEnabled(!StringUtil.isEmpty(editable.toString()));
                button.setTag(editable.toString());
            }
        });

    }

    public void checkButtonState(final View button, final WPEditText... editText) {
        button.setEnabled(false);
        for (int i = 0; i < editText.length; i++) {
            final int positon = i;
            editText[i].addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!StringUtil.isEmpty(editable.toString())) {
                        boolean enable = true;
                        for (int j = 0; j < editText.length; j++) {
                            if (StringUtil.isEmpty(editText[j].getEditTextString())) {
                                enable = false;
                                break;
                            }
                        }
                        button.setEnabled(enable);
                    } else {
                        button.setEnabled(false);
                    }

                }
            });
        }
    }

    public boolean checkButtonState1(final View button, final EditText... editText) {
        button.setEnabled(false);
        for (int i = 0; i < editText.length; i++) {
            final int positon = i;
            editText[i].addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    if (!StringUtil.isEmpty(editable.toString())) {

                        boolean enable = true;
                        for (int j = 0; j < editText.length; j++) {
                            if (StringUtil.isEmpty(editText[j].getText().toString().trim())) {
                                enable = false;
                                break;
                            }
                        }
                        button.setEnabled(enable);
                    } else {
                        button.setEnabled(false);
                    }

                }
            });
        }
        return button.isEnabled();
    }

    public boolean checkVerifyCode(String verifycode, CheckException exception) {
        if (exception == null) {
            exception = new CheckException();
        }

        if (TextUtils.isEmpty(verifycode)) {
            exception.setErrorMsg(EMPEY_VERIFY_CODE);
            return false;
        } else if (verifycode.length() > 8) {
            exception.setErrorMsg(LENGTH_MAX_VERIFY);
            return false;
        }
        return true;
    }

//    public void checkIdentityCard(EditText editText, Context context) {
//        editText.addTextChangedListener(new NoChineseTextWatcher(editText, context));
//    }

//    public class NoChineseTextWatcher implements TextWatcher {
//        final String reg = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";//正则表达式，非中文
//        private boolean isNotMatch = false;
//        private EditText editText;
//        private Context context;
//
//        public NoChineseTextWatcher(EditText editText, Context context) {
//            this.editText = editText;
//            this.context = context;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            String str = s.toString();
//            if (!TextUtils.isEmpty(str)) {
//                char[] chars = str.toCharArray();
//                for (int i = 0; i < str.length(); i++) {
//                    String aChar = String.valueOf(chars[i]);
//                    if ( !Pattern.matches(reg, aChar)){
//                        isNotMatch = true;
//                    }else{
//                        isNotMatch = false;
//                    }
//
//                }
//                if (!isNotMatch) {
//                    ToastUtils.showShort("请输入正确的身份证号码");
//                    editText.setText("");
//                    isNotMatch = true;
//                }
//            } else {
//                isNotMatch = false;
//            }
//        }
//    }
    public void checkPwdInPutType(EditText editText, Context context) {
        editText.addTextChangedListener(new NoChineseTextWatcher(editText, context));
    }

    public class NoChineseTextWatcher implements TextWatcher {
        final String reg = "[^[\u4E00-\u9FA5]]";//正则表达式，非中文
        private boolean isNotMatch = false;
        private EditText editText;
        private Context context;

        public NoChineseTextWatcher(EditText editText, Context context) {
            this.editText = editText;
            this.context = context;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if (!TextUtils.isEmpty(str)) {
                char[] chars = str.toCharArray();
                for (int i = 0; i < str.length(); i++) {
                    String aChar = String.valueOf(chars[i]);
                    if (!aChar.matches(reg)) {
                        isNotMatch = true;
                    }
                }
                if (isNotMatch) {
                    ToastUtils.showShort("密码不能设置中文，请重新设置！");
                    editText.setText("");
                    isNotMatch = false;
                }
            } else {
                isNotMatch = false;
            }
        }
    }
}
