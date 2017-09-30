package com.cloudTop.starshare.LoginUnitTest.model;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.RequestCallback;
import com.cloudTop.starshare.BuildConfig;
import com.cloudTop.starshare.bean.RegisterReturnWangYiBeen;
import com.cloudTop.starshare.common.CustomTestRunner;
import com.cloudTop.starshare.LoginUnitTest.model.net.LoginModelWrapper;
import com.cloudTop.starshare.LoginUnitTest.model.net.SocketUserAPIWrapper;
import com.cloudTop.starshare.common.TestAppliction;
import com.cloudTop.starshare.listener.IDataRequestListener;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.ui.main.presenter.LoginPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;


import java.util.concurrent.CountDownLatch;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * 接口部分使用真实数据，只进行了view的mock测试，验证各种数据返回后的view的处理是否符合预期
 */

//不能mockfinal类,只有jmock才行
@PrepareForTest(NimUIKit.class)

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(CustomTestRunner.class)
@PowerMockIgnore({"org.mockito.*","org.robolectric.*","android.*"})
@Config(shadows = {ShadowLog.class}, constants = BuildConfig.class, sdk = 21, packageName = "com.cloudTop.starshare", application = TestAppliction.class)
public class LoginModelTest {

    private LoginPresenter presenter;

//    @Mock
//    private LoginContract.View view;
//
//    @Mock
//    private AuthService authService;
//
//    @Mock
//    private LogUtils logUtils;
//
//    @Mock
//    private RegisterReturnWangYiBeen registerReturnWangYiBeen;
//
//    @Mock
//    private SocketUserAPI mockSocketUserAPI;

//    @Mock
//    private NIMClient NIMClient;

//    @Mock
//    private SocketUserAPIWrapper mockSocketUserAPIWrapper;

//    @Spy
//    private LoginModelWrapper loginModel;

//    @Mock
//    private IDataRequestListener listener;
//
//    @Mock
//    private LoginReturnInfo loginReturnInfo;

//    @Mock
//    private NimUIKit NimUIKit;

    private String errorPhone;
    private String successPhone;
    private String errorPassword;
    private String successPassword;
    private SocketUserAPIWrapper socketUserAPI;
    private LoginModelWrapper loginModel;
    private RegisterReturnWangYiBeen registerReturnWangYiBeen;
    //    private LoginModelWrapper loginModel;

    //    private LoginModelWrapper loginModel;
//    LoginModelWrapper loginModel;
    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
//        MockitoAnnotations.initMocks(this);
//        loginModel = spy(new LoginModelWrapper());
//        PowerMockitoAnnotations.initMocks(this);

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

        socketUserAPI = new SocketUserAPIWrapper();
    }

    @Test
    public void testLoginModelError() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1); //创建CountDownLatch

        loginModel.login(errorPhone, errorPassword, socketUserAPI, new IDataRequestListener() {

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

        IDataRequestListener listener=mock(IDataRequestListener.class);
        PowerMockito.verifyPrivate(loginModel).invoke("requestError", listener);
//        verify(loginModel).requestError(listener);
    }

    //    @PrepareForTest({LoginModelWrapper.class})

    //final修饰
    @Test
    @PrepareForTest(NimUIKit.class)
    public void testLoginModelSuccess() throws Exception {
//        PowerMockito.mockStatic(LoginModelWrapper.class);

//        PowerMockito.mockStatic(NimUIKit.class);
        loginModel = PowerMockito.spy(new LoginModelWrapper());

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
//        loginModel=new LoginModelWrapper();

//        UserAPI userAPI = NetworkAPIFactoryImpl.getUserAPI();

        SocketUserAPIWrapper mockSocketUserAPIWrapper=mock(SocketUserAPIWrapper.class);

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                //这里可以获得传给performLogin的参数
                Object[] arguments = invocation.getArguments();

                OnAPIListener callback = (OnAPIListener) arguments[4];
                registerReturnWangYiBeen = new RegisterReturnWangYiBeen();
                callback.onSuccess(registerReturnWangYiBeen);
                return 200; //对于如果mock的是非void方法来说，这个将作为目标方法的返回值
            }
        }).when(mockSocketUserAPIWrapper).registerWangYi(eq(0), anyString(), anyString(), any(long.class), any(OnAPIListener.class));

        loginModel.setSocketUserAPI(mockSocketUserAPIWrapper);

        AbortableFuture abortableFuture = mock(AbortableFuture.class);
        RequestCallback requestCallback = mock(RequestCallback.class);
        //网易sdk里的方法 不测了
//        PowerMockito.when(NimUIKit.doLogin(any(LoginInfo.class),any(RequestCallback.class))).then(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Object[] arguments = invocation.getArguments();
//
//                RequestCallback callback = (RequestCallback) arguments[1];
////                LoginInfo loginInfo = new LoginInfo();
//                LoginInfo loginInfo = mock(LoginInfo.class);
//                callback.onSuccess(loginInfo);
//                return null;
//            }
//        });

//        Mockito.when(NIMClient.getService(AuthService.class)).thenReturn(authService);

//        PowerMockito.doReturn(authService).when(NIMClient.getService(AuthService.class));
//        PowerMockito.whenNew(AuthService.class).withArguments("path").thenReturn(file);//意思是当new File时返回模拟变量file


//        PowerMockito.when(NIMClient.class,"getService",AuthService.class).thenReturn(authService);

//        PowerMockito.whenNew(AbortableFuture.class).withArguments("path").thenReturn(file);//意思是当new File时返回模拟变量file

//

//        Mockito.doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                //这里可以获得传给performLogin的参数
//                Object[] arguments = invocation.getArguments();
//
//                RequestCallback callback = (RequestCallback) arguments[0];
//                LoginInfo loginInfo = new LoginInfo(anyString(), anyString());
//                callback.onSuccess(loginInfo);
//                return 200; //对于如果mock的是非void方法来说，这个将作为目标方法的返回值
//            }
//        }).when(abortableFuture).setCallback(any(RequestCallback.class));


        loginModel.login(successPhone, successPassword, socketUserAPI, listener);

        latch.await();
//        assertEquals(1, result.size());

        //这里和上面的子线程请求异步了,注意!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        PowerMockito.verifyPrivate(loginModel).invoke("wangyiRegister", successPhone,loginReturnInfo, listener);


//        Mockito.verify(logUtils).logd(anyString());
//        Assert
    }

}
