package com.yundian.celebrity;

import android.os.UserManager;


import com.yundian.celebrity.listener.IDataRequestListener;
import com.yundian.celebrity.ui.main.contract.LoginContract;
import com.yundian.celebrity.ui.main.model.LoginModel;
import com.yundian.celebrity.ui.main.presenter.LoginPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 接口部分使用真实数据，只进行了view的mock测试，验证各种数据返回后的view的处理是否符合预期
 */
public class LoginPresenterTest1 {

    private LoginPresenter presenter;

    @Mock
    private LoginContract.View view;

    @Mock
    private LoginModel loginModel;
    private String phone;
    private String password;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

//        // The presenter wont't update the view unless it's active.
//        when(view.isActive()).thenReturn(true);

//        // 设置标识，用于区分处理Observer的线程等情况
//        BoreConstants.isUnitTest = true;
        phone = "18551681236";
        password = "123456";
        presenter = new LoginPresenter(view);
        presenter.setLoginModel(loginModel);
    }

    @Test
    public void testLogin_EmptyPassword() throws Exception {


        presenter.login(phone, "");

        verify(view).update2LoginFail("密码不能小于6个字符");
//        verify(loginModel).login();
    }

    @Test
    public void testLogin_EmptyUsername() throws Exception {
        presenter.login("", password);

        verify(view).update2LoginFail("手机号码不正确");
    }
//
    @Test
    public void testLogin_Success() throws Exception {

        presenter.login(phone, password);

        Mockito.verify(loginModel).login(anyString(), anyString(),any(IDataRequestListener.class));
    }

    @Test
    @Spec(desc = "should mock perform certain action")
    public void testMockAnswerFail() {
//        UserManager mockUserManager = Mockito.mock(UserManager.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                //这里可以获得传给performLogin的参数
                Object[] arguments = invocation.getArguments();

                IDataRequestListener callback = (IDataRequestListener) arguments[2];
                callback.loadFail("1");
                return 500; //对于如果mock的是非void方法来说，这个将作为目标方法的返回值
            }
        }).when(loginModel).login(anyString(), anyString(), any(IDataRequestListener.class));

        presenter.login(phone, password);
        verify(view).update2LoginFail();
//        loginModel.login("xiaochuang", "xiaochuang password", Mockito.mock(IDataRequestListener.class));
    }

    @Test
    @Spec(desc = "should mock perform certain action")
    public void testMockAnswerSuccess() throws Exception {
//        UserManager mockUserManager = Mockito.mock(UserManager.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                //这里可以获得传给performLogin的参数
                Object[] arguments = invocation.getArguments();

                IDataRequestListener callback = (IDataRequestListener) arguments[2];
                callback.loadSuccess("1");
                return 500; //对于如果mock的是非void方法来说，这个将作为目标方法的返回值
            }
        }).when(loginModel).login(anyString(), anyString(), any(IDataRequestListener.class));

        presenter.login(phone, password);

        verify(view).update2LoginSuccess();

//        私有方法的唤起
//        PowerMockito.verifyPrivate(presenter).invoke("initNotificationConfig");

//        verify(presenter).initNotificationConfig();
//        loginModel.login("xiaochuang", "xiaochuang password", Mockito.mock(IDataRequestListener.class));
    }
//
//    @Test
//    public void testLogin_UserNotExit() throws Exception {
//        String phone = "110110110";
//        presenter.login(phone, "123456");
//
//        verify(view).showProgress();
//        verify(view).dismissProgress();
//        verify(view).showTip("找不到用户");
//    }
//
//    @Test
//    public void testLogin_PswError() throws Exception {
//        String phone = "18551681236";
//        presenter.login(phone, "110119120");
//
//        verify(view).showProgress();
//        verify(view).dismissProgress();
//        verify(view).showTip("密码不正确");
//    }
}
