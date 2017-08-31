package com.yundian.celebrity.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.testin.agent.Bugout;
import com.testin.agent.BugoutConfig;
import com.yundian.celebrity.R;
import com.yundian.celebrity.app.AppApplication;
import com.yundian.celebrity.app.AppConfig;
import com.yundian.celebrity.bean.MyAddressBean;
import com.yundian.celebrity.bean.QiNiuAdressBean;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.utils.GetIPAddressUtils;
import com.yundian.celebrity.utils.LogUtils;

import java.lang.ref.WeakReference;

import static com.yundian.celebrity.app.AppConfig.QI_NIU_PIC_ADRESS;

/**
 * Created by Administrator on 2017/5/5.
 * #12
 */

public class SplashActivity extends Activity {

    private MyHandler mHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }
//
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 1 :
//                    startNextAct();
//                    break;
//            }
//        }
//
//
//    };

    private static class MyHandler extends Handler{
        private final WeakReference<SplashActivity> mActivity;
        private MyHandler(SplashActivity splashActivity) {
            mActivity = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mActivity.get();
            if(activity!=null){
                switch (msg.what){
                    case 1 :
                        activity.startNextAct();
                        break;
                }
            }
        }
    }

    private void startNextAct() {
        startActivity(new Intent(this,MainActivity.class));
        overridePendingTransition(R.anim.act_in_from_right, R.anim.act_out_from_left);
        finish();
    }



    public void initView() {
        mHandler = new MyHandler(this);
        Bugout.init(this, "1664ea921dcbe122834e440f7f584e2e", "yingyongbao");
        initBugOut();
        initGeTui();
        mHandler.sendEmptyMessageDelayed(1,2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final MyAddressBean ipAddress = GetIPAddressUtils.getIpAddress();
                if (ipAddress!=null&&ipAddress.getData()==null){
                    return;
                }
                AppConfig.AREA_ID = Long.valueOf(ipAddress.getData().getArea_id());
                AppConfig.AREA = ipAddress.getData().getArea();
                AppConfig.ISP_ID = Long.valueOf(ipAddress.getData().getIsp_id());
                AppConfig.ISP = ipAddress.getData().getIsp();
                NetworkAPIFactoryImpl.getUserAPI().getQiNiuPicDress(new OnAPIListener<QiNiuAdressBean>() {
                    @Override
                    public void onError(Throwable ex) {
                        LogUtils.loge("ysl_七牛error");
//                        AppConfig.HUANAN_QI_NIU_PIC_ADRESS =AppConfig.QI_NIU_PIC_ADRESS;
                    }

                    @Override
                    public void onSuccess(QiNiuAdressBean o) {
                        LogUtils.loge("ysl_七牛"+o.toString());
                        String area = ipAddress.getData().getArea();
                        AppConfig.HUANAN_QI_NIU_PIC_ADRESS = o.getQINIU_URL_HUANAN();
                        if ("华东".equals(area)&& !TextUtils.isEmpty(o.getQINIU_URL_HUADONG())){
                            QI_NIU_PIC_ADRESS = o.getQINIU_URL_HUANAN();
                            LogUtils.loge("ysl_七牛"+"华东");
                        }else if ("华北".equals(area)&& !TextUtils.isEmpty(o.getQINIU_URL_HUABEI())){
                            QI_NIU_PIC_ADRESS = o.getQINIU_URL_HUANAN();
                            LogUtils.loge("ysl_七牛"+"华北");
                        }else if ("华南".equals(area)&& !TextUtils.isEmpty(o.getQINIU_URL_HUANAN())){
                            QI_NIU_PIC_ADRESS = o.getQINIU_URL_HUANAN();

                            LogUtils.loge("ysl_七牛"+"华南");
                        }
                    }
                });
            }
        }).start();
//        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.3f, 1f);
//        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.3f, 1f);
//        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1f);
//        ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(tvName, alpha, scaleX, scaleY);
//        ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(ivLogo, alpha, scaleX, scaleY);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(objectAnimator1, objectAnimator2);
//        animatorSet.setInterpolator(new AccelerateInterpolator());
//        animatorSet.setDuration(2000);
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                startActivity(MainActivity.class);
//                overridePendingTransition(R.anim.act_in_from_right, R.anim.act_out_from_left);
//                finish();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//        animatorSet.start();
    }

    private void initBugOut() {
        BugoutConfig config = new BugoutConfig.Builder(this)
                .withAppKey("1664ea921dcbe122834e440f7f584e2e")     // 您的应用的项目ID,如果已经在 Manifest 中配置则此处可略
                //  .withAppChannel(cnl)     // 发布应用的渠道,如果已经在 Manifest 中配置则此处可略
                .withUserInfo(AppApplication.getAndroidId())    // 用户信息-崩溃分析根据用户记录崩溃信息
                .withDebugModel(true)    // 输出更多SDK的debug信息
                .withErrorActivity(true)    // 发生崩溃时采集Activity信息
                .withCollectNDKCrash(true) //  收集NDK崩溃信息
                .withOpenCrash(true)    // 收集崩溃信息开关
                .withOpenEx(true)     // 是否收集异常信息
                .withReportOnlyWifi(true)    // 仅在 WiFi 下上报崩溃信息
                .withReportOnBack(true)    // 当APP在后台运行时,是否采集信息
                .withQAMaster(true)    // 是否收集摇一摇反馈
                .withCloseOption(false)   // 是否在摇一摇菜单展示‘关闭摇一摇选项’
                .withLogCat(true)  // 是否系统操作信息
                .build();
        Bugout.init(config);
    }

    private void initGeTui() {
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
//        PushManager.getInstance().initialize(this.getApplicationContext(), com.yundian.star.service.DemoPushService.class);
//
//        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
//        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.yundian.star.service.DemoIntentService.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //* 注：回调 1
       Bugout.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //* 注：回调 2
      Bugout.onPause(this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //* 注：回调 3
      Bugout.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

}
