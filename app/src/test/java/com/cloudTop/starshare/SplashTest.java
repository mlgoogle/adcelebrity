//package com.cloudTop.starshare;
//
//import android.content.Intent;
//
//import com.cloudTop.starshare.bean.AssetDetailsBean;
//import com.cloudTop.starshare.listener.OnAPIListener;
//import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
//import com.cloudTop.starshare.ui.main.activity.LoginActivity;
//import com.cloudTop.starshare.ui.main.activity.MainActivity;
//import com.cloudTop.starshare.ui.main.activity.SplashActivity;
//import com.cloudTop.starshare.utils.LogUtils;
//import com.cloudTop.starshare.widget.WPEditText;
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
//import static com.cloudTop.starshare.R.drawable.splash;
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
//    @Spec(desc = "should loging use CheckInfoHelper")
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