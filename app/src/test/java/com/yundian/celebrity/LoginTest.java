//package com.yundian.celebrity;
//
//import android.view.View;
//
//import com.netease.nim.uikit.common.util.C;
//import com.yundian.celebrity.helper.CheckHelper;
//import com.yundian.celebrity.ui.main.activity.LoginActivity;
//import com.yundian.celebrity.widget.CheckException;
//import com.yundian.celebrity.widget.WPEditText;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//
//import org.robolectric.RobolectricGradleTestRunner;
//import org.robolectric.annotation.Config;
//
//
//import static org.mockito.Matchers.anyString;
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, sdk = 21)
//public class LoginTest {
//    //    @Test
////    public void addition_isCorrect() throws Exception {
////        assertEquals(4, 2 + 2);
////    }
//    @Before
//    public void setup() {
////        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
////        WPEditText userNameEditText = (WPEditText)loginActivity.findViewById(R.id.userNameEditText);
//    }
//
//    @Test
//    @Spec(desc = "should loging use CheckHelper")
//    public void testCheck() {
////        LoginActivity mockLoginActivity = Mockito.spy(LoginActivity.class);
////        CheckHelper checkHelper=new CheckHelper();
//        CheckHelper mockCheckHelper = Mockito.mock(CheckHelper.class);
//        //把这个mock出来的对象用set方法设置进去
////        mockLoginActivity.set
////        CheckException checkException = new CheckException();
//        CheckException checkException = Mockito.mock(CheckException.class);
//        CheckException checkException1 = Mockito.mock(CheckException.class);
//
//        Mockito.when(mockCheckHelper.checkMobile(anyString(),checkException)).thenReturn(true);
////        Mockito.when(mockCheckHelper.checkPassword(anyString(),checkException)).thenReturn(true);
//
//
//        Mockito.verify(mockCheckHelper).checkMobile("dsgdsg",checkException1);
////        Mockito.verify(mockCheckHelper).checkPassword("dsgdsg",checkException);
//
////        mockLoginActivity.loging();
//    }
//
//    @Test
//    @Spec(desc = "should loging")
//    public void testLogin(){
//
//    }
//}