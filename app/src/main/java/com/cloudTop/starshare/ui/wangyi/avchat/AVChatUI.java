package com.cloudTop.starshare.ui.wangyi.avchat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.ui.wangyi.DemoCache;
import com.cloudTop.starshare.ui.wangyi.avchat.activity.AVChatExitCode;
import com.cloudTop.starshare.ui.wangyi.avchat.constant.CallStateEnum;
import com.cloudTop.starshare.ui.wangyi.avchat.widgets.MultiSelectDialog;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 音视频管理器, 音视频相关功能管理
 * Created by hzxuwen on 2015/4/23.
 */
public class AVChatUI implements AVChatUIListener {
    // constant
    private static final String TAG = "AVChatUI";

    // data
    private Context context;
    private final AVChatListener aVChatListener;
    private String receiverId;
    private AVChatAudio avChatAudio;
    private AVChatVideo avChatVideo;
    private AVChatSurface avChatSurface;
    private String videoAccount; // 发送视频请求，onUserJoin回调的user account

    private CallStateEnum callingState = CallStateEnum.INVALID;

    private long timeBase = 0;

    // view
    private View root;

    // state
    public boolean canSwitchCamera = false;
    private boolean isClosedCamera = false;
    public AtomicBoolean isCallEstablish = new AtomicBoolean(false);

    private final String[] BASIC_PERMISSIONS = new String[]{Manifest.permission.CAMERA,};

    //是否在录制
    private boolean isRecording = false;

    // 检查存储
    private boolean recordWarning = false;

    List<Pair<String, Boolean>> recordList = new LinkedList<Pair<String, Boolean>>();

    public interface AVChatListener {
        void uiExit();
    }

    public AVChatUI(Context context, View root, AVChatListener listener) {
        this.context = context;
        this.root = root;
        this.aVChatListener = listener;
        configFromPreference(PreferenceManager.getDefaultSharedPreferences(context));
        updateAVChatOptionalConfig();
    }


    //Config from Preference
    private int videoCropRatio;
    private boolean videoAutoRotate;
    private int videoQuality;
    private boolean serverRecordAudio;
    private boolean serverRecordVideo;
    private boolean defaultFrontCamera;
    private boolean autoCallProximity;
    private int videoHwEncoderMode;
    private int videoHwDecoderMode;
    private boolean videoFpsReported;
    private int audioEffectAecMode;
    private int audioEffectNsMode;
    private int videoMaxBitrate;
    private int deviceDefaultRotation;
    private int deviceRotationOffset;
    private boolean audioHighQuality;
    private boolean audioDtx;

    private void configFromPreference(SharedPreferences preferences) {
        videoCropRatio = Integer.parseInt(preferences.getString(context.getString(R.string.nrtc_setting_vie_crop_ratio_key), "0"));
        videoAutoRotate = preferences.getBoolean(context.getString(R.string.nrtc_setting_vie_rotation_key), true);
        videoQuality = Integer.parseInt(preferences.getString(context.getString(R.string.nrtc_setting_vie_quality_key), 0 + ""));
        serverRecordAudio = preferences.getBoolean(context.getString(R.string.nrtc_setting_other_server_record_audio_key), false);
        serverRecordVideo = preferences.getBoolean(context.getString(R.string.nrtc_setting_other_server_record_video_key), false);
        defaultFrontCamera = preferences.getBoolean(context.getString(R.string.nrtc_setting_vie_default_front_camera_key), true);
        autoCallProximity = preferences.getBoolean(context.getString(R.string.nrtc_setting_voe_call_proximity_key), true);
        videoHwEncoderMode = Integer.parseInt(preferences.getString(context.getString(R.string.nrtc_setting_vie_hw_encoder_key), 0 + ""));
        videoHwDecoderMode = Integer.parseInt(preferences.getString(context.getString(R.string.nrtc_setting_vie_hw_decoder_key), 0 + ""));
        videoFpsReported = preferences.getBoolean(context.getString(R.string.nrtc_setting_vie_fps_reported_key), true);
        audioEffectAecMode = Integer.parseInt(preferences.getString(context.getString(R.string.nrtc_setting_voe_audio_aec_key), 2 + ""));
        audioEffectNsMode = Integer.parseInt(preferences.getString(context.getString(R.string.nrtc_setting_voe_audio_ns_key), 2 + ""));
        String value1 = preferences.getString(context.getString(R.string.nrtc_setting_vie_max_bitrate_key), 0 + "");
        videoMaxBitrate = Integer.parseInt(TextUtils.isDigitsOnly(value1) && !TextUtils.isEmpty(value1) ? value1 : 0 + "");
        String value2 = preferences.getString(context.getString(R.string.nrtc_setting_other_device_default_rotation_key), 0 + "");
        deviceDefaultRotation = Integer.parseInt(TextUtils.isDigitsOnly(value2) && !TextUtils.isEmpty(value2) ? value2 : 0 + "");
        String value3 = preferences.getString(context.getString(R.string.nrtc_setting_other_device_rotation_fixed_offset_key), 0 + "");
        deviceRotationOffset = Integer.parseInt(TextUtils.isDigitsOnly(value3) && !TextUtils.isEmpty(value3) ? value3 : 0 + "");
        audioHighQuality = preferences.getBoolean(context.getString(R.string.nrtc_setting_voe_high_quality_key), false);
        audioDtx = preferences.getBoolean(context.getString(R.string.nrtc_setting_voe_dtx_key), true);
    }


    /**
     * 1, autoCallProximity: 语音通话时使用, 距离感应自动黑屏
     * 2, videoCropRatio: 制定固定的画面裁剪比例，发送端有效
     * 3, videoAutoRotate: 结合自己设备角度和对方设备角度自动旋转画面
     * 4, serverRecordAudio: 需要服务器录制语音, 同时需要 APP KEY 下面开通了服务器录制功能
     * 5, serverRecordVideo: 需要服务器录制视频, 同时需要 APP KEY 下面开通了服务器录制功能
     * 6, defaultFrontCamera: 默认是否使用前置摄像头
     * 7, videoQuality: 视频质量调整, 最高建议使用480P
     * 8, videoFpsReported: 是否开启视频绘制帧率汇报
     * 9, deviceDefaultRotation: 99.99%情况下你不需要设置这个参数, 当设备固定在水平方向时,并且设备不会移动, 这时是无法确定设备角度的,可以设置一个默认角度
     * 10, deviceRotationOffset: 99.99%情况下你不需要设置这个参数, 当你的设备传感器获取的角度永远偏移固定值时设置,用于修正旋转角度
     * 11, videoMaxBitrate: 视频最大码率设置, 100K ~ 5M. 如果没有特殊需求不要去设置,会影响SDK内部的调节机制
     * 12, audioEffectAecMode: 语音处理选择, 默认使用平台内置,当你发现平台内置不好用时可以设置到SDK内置
     * 13, audioEffectNsMode: 语音处理选择, 默认使用平台内置,当你发现平台内置不好用时可以设置到SDK内置
     * 14, videoHwEncoderMode: 视频编码类型, 默认情况下不用设置.
     * 15, videoHwDecoderMode: 视频解码类型, 默认情况下不用设置.
     * 16, audioHighQuality: 高清语音，采用更高的采样率来传输语音
     * 17, audioDtx: 非连续发送，当监测到人声非活跃状态时减少数据包的发送
     */
    private void updateAVChatOptionalConfig() {

    }


    /**
     * ******************************初始化******************************
     */

    /**
     * 初始化，包含初始化音频管理器， 视频管理器和视频界面绘制管理器。
     *
     * @return boolean
     */
    public boolean initiation() {
        AVChatProfile.getInstance().setAVChatting(true);
        avChatAudio = new AVChatAudio(context,root.findViewById(R.id.avchat_audio_layout), this, this);
        avChatVideo = new AVChatVideo(context, root.findViewById(R.id.avchat_video_layout), this, this);
        avChatSurface = new AVChatSurface(context, this, root.findViewById(R.id.avchat_surface_layout));

        return true;
    }

    /**
     * ******************************拨打和接听***************************
     */





    /**
     * 状态改变
     *
     * @param stateEnum
     */
    public void onCallStateChange(CallStateEnum stateEnum) {
        callingState = stateEnum;
        avChatSurface.onCallStateChange(stateEnum);
        avChatAudio.onCallStateChange(stateEnum);
        avChatVideo.onCallStateChange(stateEnum);
    }

    /**
     * 挂断
     *
     * @param type 音视频类型
     */
    private void hangUp(final int type) {
        closeSessions(type);
        AVChatSoundPlayer.instance().stop();
    }

    /**
     * 关闭本地音视频各项功能
     *
     * @param exitCode 音视频类型
     */
    public void closeSessions(int exitCode) {
        //not  user  hang up active  and warning tone is playing,so wait its end
        Log.i(TAG, "close session -> " + AVChatExitCode.getExitString(exitCode));
        if (avChatAudio != null)
            avChatAudio.closeSession(exitCode);
        if (avChatVideo != null)
            avChatVideo.closeSession(exitCode);
        showQuitToast(exitCode);
        isCallEstablish.set(false);
        canSwitchCamera = false;
        isClosedCamera = false;
        aVChatListener.uiExit();
    }

    /**
     * 给出结束的提醒
     *
     * @param code
     */
    public void showQuitToast(int code) {
        switch (code) {
            case AVChatExitCode.NET_CHANGE: // 网络切换
            case AVChatExitCode.NET_ERROR: // 网络异常
            case AVChatExitCode.CONFIG_ERROR: // 服务器返回数据错误
                Toast.makeText(context, R.string.avchat_net_error_then_quit, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.PEER_HANGUP:
            case AVChatExitCode.HANGUP:
                if (isCallEstablish.get()) {
                    Toast.makeText(context, R.string.avchat_call_finish, Toast.LENGTH_SHORT).show();
                }
                break;
            case AVChatExitCode.PEER_BUSY:
                Toast.makeText(context, R.string.avchat_peer_busy, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.PROTOCOL_INCOMPATIBLE_PEER_LOWER:
                Toast.makeText(context, R.string.avchat_peer_protocol_low_version, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.PROTOCOL_INCOMPATIBLE_SELF_LOWER:
                Toast.makeText(context, R.string.avchat_local_protocol_low_version, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.INVALIDE_CHANNELID:
                Toast.makeText(context, R.string.avchat_invalid_channel_id, Toast.LENGTH_SHORT).show();
                break;
            case AVChatExitCode.LOCAL_CALL_BUSY:
                Toast.makeText(context, R.string.avchat_local_call_busy, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * ******************************* 接听与拒绝操作 *********************************
     */

    /**
     * 拒绝来电
     */
    private void rejectInComingCall() {
        /**
         * 接收方拒绝通话
         * AVChatCallback 回调函数
         */
        closeSessions(AVChatExitCode.REJECT);
        AVChatSoundPlayer.instance().stop();
    }

    /**
     * 拒绝音视频切换
     */
    private void rejectAudioToVideo() {
    }

    /**
     * 接听来电
     */
    private void receiveInComingCall() {
        //接听，告知服务器，以便通知其他端

        if (callingState == CallStateEnum.INCOMING_AUDIO_CALLING) {
            onCallStateChange(CallStateEnum.AUDIO_CONNECTING);
        } else {
            onCallStateChange(CallStateEnum.VIDEO_CONNECTING);
        }
        AVChatSoundPlayer.instance().stop();
    }

    /*************************** AVChatUIListener ******************************/

    /**
     * 点击挂断或取消
     */
    @Override
    public void onHangUp() {
        if (isCallEstablish.get()) {
            hangUp(AVChatExitCode.HANGUP);
        } else {
            hangUp(AVChatExitCode.CANCEL);
        }
    }

    /**
     * 拒绝操作，根据当前状态来选择合适的操作
     */
    @Override
    public void onRefuse() {
        switch (callingState) {
            case INCOMING_AUDIO_CALLING:
            case AUDIO_CONNECTING:
                rejectInComingCall();
                break;
            case INCOMING_AUDIO_TO_VIDEO:
                rejectAudioToVideo();
                break;
            case INCOMING_VIDEO_CALLING:
            case VIDEO_CONNECTING: // 连接中点击拒绝
                rejectInComingCall();
                break;
            default:
                break;
        }
    }

    /**
     * 开启操作，根据当前状态来选择合适的操作
     */
    @Override
    public void onReceive() {
        switch (callingState) {
            case INCOMING_AUDIO_CALLING:
                receiveInComingCall();
                onCallStateChange(CallStateEnum.AUDIO_CONNECTING);
                break;
            case AUDIO_CONNECTING: // 连接中，继续点击开启 无反应
                break;
            case INCOMING_VIDEO_CALLING:
                receiveInComingCall();
                onCallStateChange(CallStateEnum.VIDEO_CONNECTING);
                break;
            case VIDEO_CONNECTING: // 连接中，继续点击开启 无反应
                break;
            case INCOMING_AUDIO_TO_VIDEO:
                receiveAudioToVideo();
            default:
                break;
        }
    }

    @Override
    public void toggleMute() {
        if (!isCallEstablish.get()) { // 连接未建立，在这里记录静音状态
            return;
        } else { // 连接已经建立
        }
    }

    @Override
    public void toggleSpeaker() {
    }

    @Override
    public void toggleRecord() {

        if (isRecording) {
            //停止录制
            isRecording = false;
            updateRecordTip();
            if (recordList.size() == 3) {
            }

        } else {
            //探测对话框
            final MultiSelectDialog selectDialog = new MultiSelectDialog(context);
            selectDialog.setTitle("选择录制内容");
            selectDialog.setMessage("录制的内容会被单独保存");
            selectDialog.setMessageTextColor(context.getResources().getColor(R.color.color_grey_999999));
            selectDialog.addItem("语音对话", false);
            if (CallStateEnum.isAudioMode(callingState)) {
                selectDialog.addItem("我的音频", false);
            } else {
                selectDialog.addItem("我的音视频", false);
            }
            if (CallStateEnum.isAudioMode(callingState)) {
                selectDialog.addItem("对方音频", false);
            } else {
                selectDialog.addItem("对方音视频", false);
            }
            selectDialog.addPositiveButton("开始录制", MultiSelectDialog.NO_TEXT_COLOR, MultiSelectDialog.NO_TEXT_SIZE,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isRecording = true;
                            updateRecordTip();
                            List<Pair<String, Boolean>> selectDialogList = selectDialog.getItemTextList();
                            if (selectDialogList.size() == 3) {
                            }
                            recordList.clear();
                            recordList.addAll(selectDialogList);
                            selectDialog.dismiss();
                        }
                    });
            selectDialog.addNegativeButton(context.getString(com.netease.nim.uikit.R.string.cancel), MultiSelectDialog.NO_TEXT_COLOR,
                    MultiSelectDialog.NO_TEXT_SIZE, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectDialog.dismiss();
                        }
                    });
            selectDialog.show();
        }
    }

    private void updateRecordTip() {
        if (CallStateEnum.isAudioMode(callingState)) {
            avChatAudio.showRecordView(isRecording, recordWarning);
        }
        if (CallStateEnum.isVideoMode(callingState)) {
            avChatVideo.showRecordView(isRecording, recordWarning);
        }
    }

    public void showRecordWarning() {
        recordWarning = true;
        updateRecordTip();
    }

    public void resetRecordTip() {
        recordWarning = false;
        isRecording = false;
        updateRecordTip();
    }

    @Override
    public void videoSwitchAudio() {
        /**
         * 请求视频切换到音频
         */
    }

    @Override
    public void audioSwitchVideo() {
        /**
         * 请求音频切换到视频
         */

    }

    @Override
    public void switchCamera() {
    }

    @Override
    public void closeCamera() {
        if (!isClosedCamera) {
            // 关闭摄像头

            isClosedCamera = true;
            avChatSurface.localVideoOff();
        } else {
            // 打开摄像头

            isClosedCamera = false;
            avChatSurface.localVideoOn();
        }

    }

    /**
     * 音频切换为视频的请求
     */
    public void incomingAudioToVideo() {
        onCallStateChange(CallStateEnum.INCOMING_AUDIO_TO_VIDEO);
    }

    /**
     * 同意音频切换为视频
     */
    private void receiveAudioToVideo() {

    }

    /**
     * 初始化大小图像
     *
     * @param largeAccount 对方的帐号
     */
    public void initAllSurfaceView(String largeAccount) {
        avChatSurface.initLargeSurfaceView(largeAccount);
        avChatSurface.initSmallSurfaceView(DemoCache.getAccount());
    }

    public void initLargeSurfaceView(String account) {
        avChatSurface.initLargeSurfaceView(account);
    }

    public void initSmallSurfaceView() {
        avChatSurface.initSmallSurfaceView(DemoCache.getAccount());
    }

    /**
     * 音频切换为视频
     */
    public void onAudioToVideo() {
        onCallStateChange(CallStateEnum.VIDEO);

    }

    /**
     * 视频切换为音频
     */
    public void onVideoToAudio() {

    }

    public void peerVideoOff() {
        avChatSurface.peerVideoOff();
    }

    public void peerVideoOn() {
        avChatSurface.peerVideoOn();
    }


    private boolean needRestoreLocalVideo = false;
    private boolean needRestoreLocalAudio = false;

    //恢复视频和语音发送
    public void resumeVideo() {
        if (needRestoreLocalVideo) {
            needRestoreLocalVideo = false;
        }

        if (needRestoreLocalAudio) {
            needRestoreLocalAudio = false;
        }

    }

    //关闭视频和语音发送. 
    public void pauseVideo() {
    }

    public boolean canSwitchCamera() {
        return canSwitchCamera;
    }

    public CallStateEnum getCallingState() {
        return callingState;
    }

    public String getVideoAccount() {
        return videoAccount;
    }

    public void setVideoAccount(String videoAccount) {
        this.videoAccount = videoAccount;
    }

    public String getAccount() {
        if (receiverId != null)
            return receiverId;
        return null;
    }

    public long getTimeBase() {
        return timeBase;
    }

    public void setTimeBase(long timeBase) {
        this.timeBase = timeBase;
    }

}
