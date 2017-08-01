//package com.yundian.celebrity;
//
//import android.content.Intent;
//
//import com.yundian.celebrity.bean.AssetDetailsBean;
//import com.yundian.celebrity.listener.OnAPIListener;
//import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
//import com.yundian.celebrity.ui.main.activity.LoginActivity;
//import com.yundian.celebrity.ui.main.activity.MainActivity;
//import com.yundian.celebrity.ui.main.activity.SplashActivity;
//import com.yundian.celebrity.utils.LogUtils;
//import com.yundian.celebrity.widget.WPEditText;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricGradleTestRunner;
//import org.robolectric.Shadows;
//import org.robolectric.annotation.Config;
//import org.robolectric.internal.Shadow;
//import org.robolectric.shadows.ShadowActivity;
//
//import static com.yundian.celebrity.R.drawable.splash;
//import static org.robolectric.Shadows.shadowOf;
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, sdk = 21)
//public class SplashTest {
//    //    @Test
////    public void addition_isCorrect() throws Exception {
////        assertEquals(4, 2 + 2);
////    }
//    @Before
//    public void setup() {
//        SplashActivity splashActivity = Robolectric.setupActivity(SplashActivity.class);
////        WPEditText userNameEditText = (WPEditText)splash.findViewById(R.id.userNameEditText);
//        Intent intent=new Intent(splashActivity, MainActivity.class);
//        ShadowActivity shadowActivity=Shadows.shadowOf(splashActivity);
//        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
//
////        Asset
//    }
//
//    @Test
//    @Spec(desc = "should loging use CheckHelper")
//    public void testLogin() {
//
////        SplashActivity splashActivity = PowerMockito.spy(SplashActivity.class);
////        splashActivity.startNextAct();
//        PowerMockito.spy(SplashActivity.class);
//    }
//
//    @Test
//    public void testNet(){
//        NetworkAPIFactoryImpl.getDealAPI().balance(new OnAPIListener<AssetDetailsBean>() {
//            @Override
//            public void onSuccess(AssetDetailsBean bean) {
//                LogUtils.loge("余额请求成功:" + bean.toString());
//
//
//            }
//
//            @Override
//            public void onError(Throwable ex) {
//                LogUtils.loge("余额请求失败:" + ex.getMessage());
//            }
//        });
//    }
//}