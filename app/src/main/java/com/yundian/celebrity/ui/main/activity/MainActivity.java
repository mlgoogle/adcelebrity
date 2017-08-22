package com.yundian.celebrity.ui.main.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.qiangxi.checkupdatelibrary.dialog.UpdateDialog;
import com.yundian.celebrity.R;
import com.yundian.celebrity.app.AppConstant;
import com.yundian.celebrity.app.Constant;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.bean.AssetDetailsBean;
import com.yundian.celebrity.bean.BankCardBean;
import com.yundian.celebrity.bean.CheckUpdateInfoEntity;
import com.yundian.celebrity.bean.EventBusMessage;
import com.yundian.celebrity.bean.TabEntity;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.ui.main.fragment.ContactFansFragment;
import com.yundian.celebrity.ui.main.fragment.InComeInfoFragment;
import com.yundian.celebrity.ui.main.fragment.MeetManageFragment;
import com.yundian.celebrity.ui.view.ForceUpdateDialog;
import com.yundian.celebrity.ui.wangyi.chatroom.helper.ChatRoomHelper;
import com.yundian.celebrity.ui.wangyi.config.preference.UserPreferences;
import com.yundian.celebrity.utils.CheckLoginUtil;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.SharePrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

import static com.qiangxi.checkupdatelibrary.dialog.UpdateDialog.UPDATE_DIALOG_PERMISSION_REQUEST_CODE;
import static com.yundian.celebrity.ui.view.ForceUpdateDialog.FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE;

public class MainActivity extends BaseActivity {
    @Bind(R.id.tab_bottom_layout)
    CommonTabLayout tabLayout;


    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private InComeInfoFragment inComeInfoFragment;
    private ContactFansFragment contactFansFragment;
    private MeetManageFragment meetManageFragment;
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private boolean flag = true;
    private int match_info;
    private static boolean isSaveWangYi = false;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    };

    Runnable runnablePermission = new Runnable() {
        @Override
        public void run() {
            requestBasicPermission();
        }
    };

    Runnable runnableCheckLogin = new Runnable() {
        @Override
        public void run() {
            checkLogin();
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        initTab();
        checkunReadMsg();
        handler.postDelayed(runnablePermission, 2000);
        handler.postDelayed(runnableCheckLogin,1000);

    }

    private void checkLogin() {
        LogUtils.loge("mainactivity检查token---------------");
        CheckLoginUtil.checkLogin(this);
    }

    private void checkunReadMsg() {
        int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        if (unreadNum > 0) {
            tabLayout.showDot(1);
        } else {
            tabLayout.hideMsg(1);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化frament
        initFragment(savedInstanceState);
        initWangYi();
        registerObservers(true);
        if (flag) {
            EventBus.getDefault().register(this); // EventBus注册广播()
            flag = false;//更改标记,使其不会再进行多次注册
        }
        Intent intent = getIntent();
        match_info = intent.getIntExtra(AppConstant.MATCH_SUCESS_INFO, 0);
        if (match_info == 1) {
            SwitchTo(2);
            tabLayout.setCurrentTab(2);
        }
    }


    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            inComeInfoFragment = (InComeInfoFragment) getSupportFragmentManager().findFragmentByTag("InComeInfoFragment");
            contactFansFragment = (ContactFansFragment) getSupportFragmentManager().findFragmentByTag("ContactFansFragment");
            meetManageFragment = (MeetManageFragment) getSupportFragmentManager().findFragmentByTag("MeetManageFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            inComeInfoFragment = new InComeInfoFragment();
            contactFansFragment = new ContactFansFragment();
            meetManageFragment = new MeetManageFragment();
            transaction.add(R.id.fl_main, inComeInfoFragment, "InComeInfoFragment");
            transaction.add(R.id.fl_main, contactFansFragment, "ContactFansFragment");
            transaction.add(R.id.fl_main, meetManageFragment, "MeetManageFragment");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                R.anim.fade_out);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        LogUtils.loge("onSaveInstanceState进来了1");
        if (tabLayout != null) {
            LogUtils.loge("onSaveInstanceState进来了2");
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }

    private void initWangYi() {
        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {

                syncPushNoDisturb(UserPreferences.getStatusConfig());

                DialogMaker.dismissProgressDialog();
            }
        });

        LogUtils.logd("sync completed = " + syncCompleted);
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(MainActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        } else {
            syncPushNoDisturb(UserPreferences.getStatusConfig());
        }
        // 聊天室初始化
        ChatRoomHelper.init();
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {
        LogUtils.logd("主页菜单position" + position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                transaction.hide(contactFansFragment);
                transaction.hide(meetManageFragment);
                transaction.show(inComeInfoFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 1:
                transaction.hide(inComeInfoFragment);
                transaction.hide(meetManageFragment);
                transaction.show(contactFansFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 2:
//                CheckLoginUtil.checkLogin(this);
                transaction.hide(contactFansFragment);
                transaction.hide(inComeInfoFragment);
                transaction.show(meetManageFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < Constant.mTitles.length; i++) {
            mTabEntities.add(new TabEntity(Constant.mTitles[i], Constant.mIconSelectIds[i], Constant.mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }


    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_NETWORK_STATE,
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        LogUtils.loge("走到此处------------------");
        if (requestCode == UPDATE_DIALOG_PERMISSION_REQUEST_CODE) {
            LogUtils.loge("强制更新-------------");
            mUpdateDialog.download();
        } else if (requestCode == FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE) {
            mForceUpdateDialog.download();
        }
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    /**
     * 若增加第三方推送免打扰（V3.2.0新增功能），则：
     * 1.添加下面逻辑使得 push 免打扰与先前的设置同步。
     * 2.设置界面 com.netease.nim.demo.main.activity.SettingsActivity 以及
     * 免打扰设置界面 com.netease.nim.demo.main.activity.NoDisturbActivity 也应添加 push 免打扰的逻辑
     * <p>
     * 注意：isPushDndValid 返回 false， 表示未设置过push 免打扰。
     */
    private void syncPushNoDisturb(StatusBarNotificationConfig staConfig) {

        boolean isNoDisbConfigExist = NIMClient.getService(MixPushService.class).isPushNoDisturbConfigExist();

        if (!isNoDisbConfigExist && staConfig.downTimeToggle) {
            NIMClient.getService(MixPushService.class).setPushNoDisturbConfig(staConfig.downTimeToggle,
                    staConfig.downTimeBegin, staConfig.downTimeEnd);
        }
    }

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            checkunReadMsg();
        }
    };

    @Override
    protected void onDestroy() {
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        registerObservers(false);
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void ReciveMessageEventBus(final EventBusMessage eventBusMessage) {
        switch (eventBusMessage.Message) {
            case 2:  //登录取消
                SwitchTo(0);
                tabLayout.setCurrentTab(0);
                break;
            case -11:
                if (eventBusMessage.getCheckUpdateInfoEntity().getIsForceUpdate() == 0) {
                    forceUpdateDialog(eventBusMessage.getCheckUpdateInfoEntity());
                } else if (eventBusMessage.getCheckUpdateInfoEntity().getIsForceUpdate() == 1){  //非强制更新
                    updateDialog(eventBusMessage.getCheckUpdateInfoEntity());
                }
                break;

            case 1:  //登录成功
                LogUtils.loge("ssssssss接收到登录已成功的消息---------------");
                requestBankInfo();  //更新银行卡信息
                requestBalance();   //更新余额信息
                tabLayout.setCurrentTab(0);
                inComeInfoFragment.getData();
                contactFansFragment.loadData();
                meetManageFragment.loadData();
                break;
        }
    }

    /**
     * 请求银行卡信息
     */
    private void requestBankInfo() {
        NetworkAPIFactoryImpl.getDealAPI().bankCardList(new OnAPIListener<BankCardBean>() {
            @Override
            public void onSuccess(BankCardBean bankCardBeen) {
                if (TextUtils.isEmpty(bankCardBeen.getCardNo()) || TextUtils.isEmpty(bankCardBeen.getBankUsername())) {
                    LogUtils.loge("银行卡列表失败----------------------------------------------");
//                    SharePrefUtil.getInstance().saveCardNo("");
                } else {
                    LogUtils.loge("银行卡列表----------------成功");
                    SharePrefUtil.getInstance().saveCardNo(bankCardBeen.getCardNo());
                }

            }

            @Override
            public void onError(Throwable ex) {
//                SharePrefUtil.getInstance().saveCardNo("");
            }
        });
    }

    private void requestBalance() {
        NetworkAPIFactoryImpl.getDealAPI().balance(new OnAPIListener<AssetDetailsBean>() {
            @Override
            public void onSuccess(AssetDetailsBean bean) {
                LogUtils.loge("余额请求成功:" + bean.toString());
                if (bean.getIs_setpwd() != -100) {
                    SharePrefUtil.getInstance().saveAssetInfo(bean);
                }
                if (!TextUtils.isEmpty(bean.getHead_url_tail()) && !TextUtils.isEmpty(bean.getNick_name())) {
                    SharePrefUtil.getInstance().putUserNickName(bean.getNick_name());
                    SharePrefUtil.getInstance().putUserPhotoUrl(bean.getHead_url_tail());
                }

                // SharePrefUtil.getInstance().saveAssetInfo(bean);
                if (!TextUtils.isEmpty(SharePrefUtil.getInstance().getUserNickName()) && isSaveWangYi == false) {
//                    updateWangYiInfo();      //修改网易头像和昵称
                    isSaveWangYi = true;
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("余额请求失败:" + ex.getMessage());
            }
        });
    }

    //修改网易头像和昵称
    private void updateWangYiInfo() {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.Name, SharePrefUtil.getInstance().getUserNickName());
        fields.put(UserInfoFieldEnum.AVATAR, SharePrefUtil.getInstance().getUserPhotoUrl());
        LogUtils.loge("网易云修改名字昵称" + SharePrefUtil.getInstance().getUserNickName() +
                SharePrefUtil.getInstance().getUserPhotoUrl());
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {
                        LogUtils.loge(i + "网易云修改名字昵称");
                    }
                });
    }


    private UpdateDialog mUpdateDialog;
    private ForceUpdateDialog mForceUpdateDialog;

    /**
     * 强制更新
     */
    public void forceUpdateDialog(CheckUpdateInfoEntity mCheckUpdateInfo) {
        mForceUpdateDialog = new ForceUpdateDialog(MainActivity.this);
        mForceUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                .setFileName(getResources().getString(R.string.app_name))
                .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/XingXiang").show();
    }

    /**
     * 非强制更新
     */
    public void updateDialog(CheckUpdateInfoEntity mCheckUpdateInfo) {
        mUpdateDialog = new UpdateDialog(this);
        mUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                .setFileName(getResources().getString(R.string.app_name))
                .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/XingXiang")
                .setShowProgress(true)
                .setIconResId(R.mipmap.ic_launcher)
                .setAppName(mCheckUpdateInfo.getAppName()).show();
    }
}
