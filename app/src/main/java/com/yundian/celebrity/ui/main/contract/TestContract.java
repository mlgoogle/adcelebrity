package com.yundian.celebrity.ui.main.contract;

import com.yundian.celebrity.base.BaseModel;
import com.yundian.celebrity.base.BasePresenter;
import com.yundian.celebrity.base.BaseView;
import com.yundian.celebrity.ui.main.model.TestModel;

/**
 * Created by Null on 2017/5/6.
 */

public interface TestContract {
    interface Model extends BaseModel {
    }
    interface View extends BaseView {
        void showUserInfo(TestModel userInfoModel);//将网络请求得到的用户信息回调
    }
    abstract static class Presenter extends BasePresenter<View, Model> {
        //登陆请求
        //public abstract void loadUserInfo();
    }
}
