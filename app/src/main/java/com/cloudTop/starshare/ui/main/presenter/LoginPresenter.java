package com.cloudTop.starshare.ui.main.presenter;

import com.cloudTop.starshare.helper.CheckInfoHelper;
import com.cloudTop.starshare.listener.IDataRequestListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.networkapi.UserAPI;
import com.cloudTop.starshare.ui.main.contract.LoginContract;
import com.cloudTop.starshare.ui.main.model.LoginModel;
import com.cloudTop.starshare.widget.CheckException;


public class LoginPresenter implements LoginContract.Presenter {

    private LoginModel loginModel;
    private LoginContract.View view;
    CheckInfoHelper checkInfoHelper=new CheckInfoHelper();
    public LoginPresenter(LoginContract.View view) {
//        在构造presenter的时候把具体的那个frament对象传过来了
        loginModel = new LoginModel();
        this.view = view;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    private boolean isOnClicked = false;
    @Override
    public void login(String userName, String password) {
//        if (config == null) {
//            return;
//        }
        if (isOnClicked) {
            return;
        }
        isOnClicked = true;
        CheckException exception = new CheckException();
        if (checkInfoHelper.checkMobile(userName, exception)
                && checkInfoHelper.checkPassword(password, exception)) {
            //注入进来比较好,由外部presenter持有
            UserAPI userAPI = NetworkAPIFactoryImpl.getUserAPI();
            loginModel.login(userName, password,userAPI, new IDataRequestListener() {
                @Override
                public void loadSuccess(Object object) {

                    isOnClicked=false;
                    if (view != null) {
                        view.update2LoginSuccess();
                    }
                }

                @Override
                public void loadFail(Object object) {
                    isOnClicked=false;
                    if (view != null) {
                        view.update2LoginFail();
                    }
                }
            });
        } else {
            isOnClicked = false;
            if (view != null) {
                view.update2LoginFail(exception.getErrorMsg());
            }
        }
    }

    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle() {
        this.view = null;
    }
}
