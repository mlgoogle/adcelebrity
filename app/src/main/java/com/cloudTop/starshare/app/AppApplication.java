package com.cloudTop.starshare.app;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.contact.core.query.PinYin;
import com.netease.nim.uikit.custom.DefalutUserInfoProvider;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.ServerAddresses;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.cloudTop.starshare.BuildConfig;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.base.baseapp.BaseApplication;
import com.cloudTop.starshare.bean.CheckUpdateInfoEntity;
import com.cloudTop.starshare.bean.EventBusMessage;
import com.cloudTop.starshare.bean.LoginReturnInfo;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.Host;
import com.cloudTop.starshare.networkapi.NetworkAPIConfig;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.networkapi.socketapi.SocketReqeust.SocketAPINettyBootstrap;
import com.cloudTop.starshare.ui.wangyi.DemoCache;
import com.cloudTop.starshare.ui.wangyi.PrivatizationConfig;
import com.cloudTop.starshare.ui.wangyi.common.util.crash.AppCrashHandler;
import com.cloudTop.starshare.ui.wangyi.common.util.sys.SystemUtil;
import com.cloudTop.starshare.ui.wangyi.config.ExtraOptions;
import com.cloudTop.starshare.ui.wangyi.config.preference.Preferences;
import com.cloudTop.starshare.ui.wangyi.config.preference.UserPreferences;
import com.cloudTop.starshare.ui.wangyi.contact.ContactHelper;
import com.cloudTop.starshare.ui.wangyi.event.DemoOnlineStateContentProvider;
import com.cloudTop.starshare.ui.wangyi.event.OnlineStateEventManager;
import com.cloudTop.starshare.ui.wangyi.session.SessionHelper;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.MD5Util;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.utils.Utils;
import com.cloudTop.starshare.widget.emoji.IImageLoader;
import com.cloudTop.starshare.widget.emoji.LQREmotionKit;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * APPLICATION 17682310986
 */
public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        testProcress();
    }

    private void testProcress() {
        String processName = getProcessName(this);
//        initWangYiIM();
        LogUtils.loge("------------processName:" + processName);
        if (processName != null) {
            if (processName.equals("com.cloudTop.starshare")) {
                //Fabric.with(this, new Crashlytics());
                //初始化logger
                LogUtils.logInit(BuildConfig.LOG_DEBUG);
//                initWangYiIM();
                // TODO: 2017/8/2  
                checkNet();
                initNetworkAPIConfig();
                registerToWx();   //注册微信
                UMShareAPI.get(this);//初始化友盟
                Config.DEBUG = true;
                initEmoji();
                if (LeakCanary.isInAnalyzerProcess(this)) {
                    return;
                }
                LeakCanary.install(this);
            } else if (processName.equals("com.cloudTop.starshare:core")) {

            } else if (processName.equals("com.cloudTop.starshare:cosine")) {

            } else if (processName.equals("com.cloudTop.starshare:pushservice")) {

            }
        }
        initWangYiIM();
    }

    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return "";
        }

        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == android.os.Process.myPid()
                    && !TextUtils.isEmpty(runningAppProcess.processName)) {
                return runningAppProcess.processName;
            }
        }
        return "";
    }

    private void initEmoji() {
        LQREmotionKit.init(this,
                Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "emoji",
                new IImageLoader() {
                    @Override
                    public void displayImage(Context context, String path, ImageView imageView) {
                        Glide.with(context)
                                .load(path)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(imageView);
                    }
                });
    }


    private void initWangYiIM() {
        DemoCache.setContext(this);
        NIMClient.init(this, getLoginInfo(), getOptions());
        ExtraOptions.provide();
        AppCrashHandler.getInstance(this);
        if (inMainProcess()) {
            // init pinyin
            PinYin.init(this);
            PinYin.validate();

            // 初始化UIKit模块
            initUIKit();

            // 注册通知消息过滤器
            registerIMMessageFilter();

            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

            // 注册网络通话来电
            //registerAVChatIncomingCallObserver(true);

            // 注册白板会话
            //registerRTSIncomingObserver(true);

            // 注册语言变化监听
            registerLocaleReceiver(true);

            OnlineStateEventManager.init();
        }
    }


    private void initNetworkAPIConfig() {
        NetworkAPIConfig networkAPIConfig = new NetworkAPIConfig();
        networkAPIConfig.setContext(getApplicationContext());
        networkAPIConfig.setSocketServerIp(Host.getSocketServerIp());
        networkAPIConfig.setSocketServerPort(Host.getSocketServerPort());
        NetworkAPIFactoryImpl.initConfig(networkAPIConfig);
    }

    public static String getAndroidId() {
        return MD5Util.MD5(Utils.getUniquePsuedoID());
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        initStatusBarNotificationConfig(options);

        // 配置保存图片，文件，log等数据的目录
        options.sdkStorageRootPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

        // 用户信息提供者
        options.userInfoProvider = new DefalutUserInfoProvider(this);

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        // 在线多端同步未读数
        options.sessionReadAck = true;

        // 云信私有化配置项
        configServerAddress(options);

        return options;
    }

    private void initUIKit() {
        // 初始化，使用 uikit 默认的用户信息提供者
        NimUIKit.init(this);

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        //NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // 会话窗口的定制初始化。
        SessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        // NimUIKit.CustomPushContentProvider(new DemoPushContentProvider());

        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter() {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (UserPreferences.getMsgIgnore() && message.getAttachment() != null) {
                    if (message.getAttachment() instanceof UpdateTeamAttachment) {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet()) {
                            if (field.getKey() == TeamFieldEnum.ICON) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
    }



    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };



    private void registerLocaleReceiver(boolean register) {
        if (register) {
            updateLocale();
            IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(localeReceiver, filter);
        } else {
            unregisterReceiver(localeReceiver);
        }
    }

    private void updateLocale() {
        NimStrings strings = new NimStrings();
        strings.status_bar_multi_messages_incoming = getString(R.string.nim_status_bar_multi_messages_incoming);
        strings.status_bar_image_message = getString(R.string.nim_status_bar_image_message);
        strings.status_bar_audio_message = getString(R.string.nim_status_bar_audio_message);
        strings.status_bar_custom_message = getString(R.string.nim_status_bar_custom_message);
        strings.status_bar_file_message = getString(R.string.nim_status_bar_file_message);
        strings.status_bar_location_message = getString(R.string.nim_status_bar_location_message);
        strings.status_bar_notification_message = getString(R.string.nim_status_bar_notification_message);
        strings.status_bar_ticker_text = getString(R.string.nim_status_bar_ticker_text);
        strings.status_bar_unsupported_message = getString(R.string.nim_status_bar_unsupported_message);
        strings.status_bar_video_message = getString(R.string.nim_status_bar_video_message);
        strings.status_bar_hidden_message_content = getString(R.string.nim_status_bar_hidden_msg_content);
        NIMClient.updateStrings(strings);
    }

    private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                updateLocale();
            }
        }
    };

    private void configServerAddress(final SDKOptions options) {
        String appKey = PrivatizationConfig.getAppKey();
        if (!TextUtils.isEmpty(appKey)) {
            options.appKey = appKey;
        }

        ServerAddresses serverConfig = PrivatizationConfig.getServerAddresses();
        if (serverConfig != null) {
            options.serverConfig = serverConfig;
        }
    }

    private void initStatusBarNotificationConfig(SDKOptions options) {
        // load 应用的状态栏配置
        StatusBarNotificationConfig config = loadStatusBarNotificationConfig();

        // load 用户的 StatusBarNotificationConfig 设置项
        StatusBarNotificationConfig userConfig = UserPreferences.getStatusConfig();
        if (userConfig == null) {
            userConfig = config;
        } else {
            // 新增的 UserPreferences 存储项更新，兼容 3.4 及以前版本
            // 新增 notificationColor 存储，兼容3.6以前版本
            // APP默认 StatusBarNotificationConfig 配置修改后，使其生效
            userConfig.notificationEntrance = config.notificationEntrance;
            userConfig.notificationFolded = config.notificationFolded;
            userConfig.notificationColor = getResources().getColor(R.color.color_blue_3a9efb);
        }
        // 持久化生效
        UserPreferences.setStatusConfig(userConfig);
        // SDK statusBarNotificationConfig 生效
        options.statusBarNotificationConfig = userConfig;
    }

    // 这里开发者可以自定义该应用初始的 StatusBarNotificationConfig
    private StatusBarNotificationConfig loadStatusBarNotificationConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        //config.notificationEntrance = StarCommunicationBookActivity.class;
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        config.notificationColor = getResources().getColor(R.color.color_blue_3a9efb);
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";

        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        // save cache，留做切换账号备用
        DemoCache.setNotificationConfig(config);
        return config;
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    public static IWXAPI api;

    private void registerToWx() {
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        api.registerApp(Constant.APP_ID);
    }

    {
        //友盟分享对应appid.要修改成自己的
        PlatformConfig.setWeixin("wx9dc39aec13ee3158", "a12a88f2c4596b2726dd4ba7623bc27e");
        PlatformConfig.setQQZone("1106159779", "iYph54WUxtMM3Enu");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }

    private void logout() {
        SharePrefUtil.getInstance().clearUserInfo();
        SharePrefUtil.getInstance().clearUserLoginInfo();
        Preferences.saveUserToken("");
//        LogoutHelper.logout();   临时关闭
//        SocketAPINettyBootstrap.getInstance().closeChannel();
//      ChatRoomHelper.logout();
//        DemoCache.clear();
//        LoginSyncDataStatusObserver.getInstance().reset();
//        DropManager.getInstance().destroy();
//        DataCacheManager.clearDataCache();  //清空缓存
    }

    private void judgeIsLogin() {
        if (!TextUtils.isEmpty(SharePrefUtil.getInstance().getToken())) {
            LogUtils.loge("已经登录,开始校验token---------------------------------");
            NetworkAPIFactoryImpl.getUserAPI().loginWithToken(SharePrefUtil.getInstance().getTokenTime(), new OnAPIListener<LoginReturnInfo>() {
                @Override
                public void onError(Throwable ex) {
                    ex.printStackTrace();
                    LogUtils.loge("----------------------登录失败.token已经失效");
                    logout();
                }

                @Override
                public void onSuccess(LoginReturnInfo loginReturnEntity) {
                    LogUtils.loge("------------------======token登录成功，保存信息" + loginReturnEntity.toString());
                    if (loginReturnEntity.getResult() == 1) {
                        NetworkAPIFactoryImpl.getUserAPI().saveDevice(loginReturnEntity.getUserinfo().getId(), new OnAPIListener<Object>() {
                            @Override
                            public void onError(Throwable ex) {

                            }

                            @Override
                            public void onSuccess(Object o) {
                                LogUtils.logd("上传设备id和类型成功:" + o.toString());
                            }
                        });
                        //服务器问题,先token登录不保存信息
                        //SharePrefUtil.getInstance().saveLoginUserInfo(loginReturnEntity);
                        if (!TextUtils.isEmpty(loginReturnEntity.getToken())) {
//                        SharePrefUtil.getInstance().setToken(loginReturnEntity.getToken());
                            SharePrefUtil.getInstance().saveLoginUserInfo(loginReturnEntity);
                        }
                        EventBus.getDefault().postSticky(new EventBusMessage(1));  //登录成功消息
                    } else {
                        LogUtils.loge("----------------------登录失败.token已经失效");
                        logout();
                    }
                }
            });

        } else {
            LogUtils.logd("token为空-------------------");
        }
    }

    private void checkNet() {
        LogUtils.logd("检测网络-------------------");
        SocketAPINettyBootstrap.getInstance().setOnConnectListener(new SocketAPINettyBootstrap.OnConnectListener() {
            @Override
            public void onExist() {
                LogUtils.logd("检测到链接存在-------------------");
            }

            @Override
            public void onSuccess() {
                LogUtils.logd("检测到连接成功-------------------");
                //token交易暂时关闭
                judgeIsLogin();
                checkUpdate();
            }

            @Override
            public void onFailure(boolean tag) {
                LogUtils.logd("检测到连接失败--------------");
                if (tag) {
                    if (!TextUtils.isEmpty(SharePrefUtil.getInstance().getToken())) {
                        LogUtils.logd("检测到连接失败----logout----------");
                        logout();
                    }
                    // connectionError();
                    //logout();
                }
            }
        });
    }


    private void checkUpdate() {
        LogUtils.loge("检查更新----------");
        NetworkAPIFactoryImpl.getUserAPI().update(new OnAPIListener<CheckUpdateInfoEntity>() {
            @Override
            public void onError(Throwable ex) {
                ex.printStackTrace();
                LogUtils.loge("检查更新失败-------------");
            }

            @Override
            public void onSuccess(CheckUpdateInfoEntity checkUpdateInfoEntity) {
                SharePrefUtil.getInstance().setVersion(checkUpdateInfoEntity.getNewAppVersionName());
                LogUtils.loge("checkUpdateInfoEntity:" + checkUpdateInfoEntity.toString());
                if (checkUpdateInfoEntity != null && checkUpdateInfoEntity.getNewAppVersionCode() > getVersionCode()) {
                    EventBusMessage msg = new EventBusMessage(-11);
                    msg.setCheckUpdateInfoEntity(checkUpdateInfoEntity);  //发送广播
                    EventBus.getDefault().postSticky(msg);
                } else {
                    LogUtils.loge("--最新版本");
                }
            }
        });
    }

    /**
     * 获取当前应用版本号
     */
    private int getVersionCode() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
