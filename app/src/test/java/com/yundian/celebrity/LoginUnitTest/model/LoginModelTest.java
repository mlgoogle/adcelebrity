package com.yundian.celebrity.LoginUnitTest.model;

import com.yundian.celebrity.BuildConfig;
import com.yundian.celebrity.bean.RegisterReturnWangYiBeen;
import com.yundian.celebrity.common.CustomTestRunner;
import com.yundian.celebrity.LoginUnitTest.model.net.LoginModelWrapper;
import com.yundian.celebrity.LoginUnitTest.model.net.SocketUserAPIWrapper;
import com.yundian.celebrity.common.TestAppliction;
import com.yundian.celebrity.bean.LoginReturnInfo;
import com.yundian.celebrity.listener.IDataRequestListener;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.socketapi.SocketUserAPI;
import com.yundian.celebrity.ui.main.contract.LoginContract;
import com.yundian.celebrity.ui.main.model.LoginModel;
import com.yundian.celebrity.ui.main.presenter.LoginPresenter;
import com.yundian.celebrity.utils.LogUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.concurrent.CountDownLatch;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * 接口部分使用真实数据，只进行了view的mock测试，验证各种数据返回后的view的处理是否符合预期
 */
@RunWith(CustomTestRunner.class)
@Config(shadows = {ShadowLog.class},constants = BuildConfig.class, sdk = 21,packageName="com.yundian.celebrity",application=TestAppliction.class)
public class LoginModelTest {

    private LoginPresenter presenter;

    @Mock
    private LoginContract.View view;

    @Mock
    private LogUtils logUtils;

    @Mock
    private RegisterReturnWangYiBeen registerReturnWangYiBeen;

    @Mock
    private SocketUserAPI socketUserAPI;

    @Spy
    private LoginModelWrapper loginModel;

    @Mock
    private IDataRequestListener listener;

    @Mock
    private LoginReturnInfo loginReturnInfo;

    private String errorPhone;
    private String successPhone;
    private String errorPassword;
    private String successPassword;
//    private LoginModelWrapper loginModel;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

//        // The presenter wont't update the view unless it's active.
//        when(view.isActive()).thenReturn(true);

//        // 设置标识，用于区分处理Observer的线程等情况
//        BoreConstants.isUnitTest = true;
        errorPhone = "18551681236";
        errorPassword = "123455";

        successPassword = "123456";
        successPhone = "18657195470";

//        loginModel = new LoginModelWrapper();
//        presenter = new LoginPresenter(view);
//        presenter.setLoginModel(loginModel);
    }

    @Test
    public void testLoginModelError() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1); //创建CountDownLatch

        loginModel.login(errorPhone,errorPassword,socketUserAPI,new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                latch.countDown();

            }

            @Override
            public void loadFail(Object object) {
                latch.countDown();
            }
        });
        latch.await();
//        assertEquals(1, result.size());

        PowerMockito.verifyPrivate(loginModel).invoke("requestError", listener);
//        verify(loginModel).requestError(listener);
    }
//    @PrepareForTest({LoginModelWrapper.class})
    @Test
    public void testLoginModelSuccess() throws Exception {
//        PowerMockito.mockStatic(LoginModelWrapper.class);

        final CountDownLatch latch = new CountDownLatch(1); //创建CountDownLatch
        IDataRequestListener listener = new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                //登录成功后还没有走到这.一直堵在这里
                latch.countDown();
            }

            @Override
            public void loadFail(Object object) {
                latch.countDown();
            }
        };
        loginModel=new LoginModelWrapper();

//        UserAPI userAPI = NetworkAPIFactoryImpl.getUserAPI();

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                //这里可以获得传给performLogin的参数
                Object[] arguments = invocation.getArguments();

                OnAPIListener callback = (OnAPIListener) arguments[5];
                callback.onSuccess(registerReturnWangYiBeen);
                return 500; //对于如果mock的是非void方法来说，这个将作为目标方法的返回值
            }
        }).when(socketUserAPI).registerWangYi(0,anyString(),anyString(), anyInt(), any(OnAPIListener.class));

//        loginModel.setSocketUserAPI(socketUserAPI);
        loginModel.login(successPhone,successPassword,socketUserAPI, listener);

        latch.await();
//        assertEquals(1, result.size());

        //这里和上面的子线程请求异步了,注意!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        PowerMockito.verifyPrivate(loginModel).invoke("wangyiRegister", successPhone,loginReturnInfo, listener);


//        Mockito.verify(logUtils).logd(anyString());
//        Assert
    }

}
