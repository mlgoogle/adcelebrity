package com.cloudTop.starshare.ui.main.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.AppConfig;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.FansAskBean;
import com.cloudTop.starshare.bean.RequestResultBean;
import com.cloudTop.starshare.bean.UptokenBean;
import com.cloudTop.starshare.event.AudioMessageEvent;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.fragment.CustomAudioFragment;
import com.cloudTop.starshare.utils.FormatUtil;
import com.cloudTop.starshare.utils.ImageLoaderUtils;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.TimeUtil;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.widget.AudioRecordButton;
import com.cloudTop.starshare.widget.EasySwitchButton;
import com.cloudTop.starshare.widget.NormalTitleBar;
import com.cloudTop.starshare.widget.audioplayer.MyAudioPlayer;
import com.cloudTop.starshare.widget.audiorecorder.AndroidAACProcessor;
import com.cloudTop.starshare.widget.photobutton.lisenter.CaptureLisenter;
import com.liulishuo.engzo.lingorecorder.LingoRecorder;
import com.liulishuo.engzo.lingorecorder.processor.WavProcessor;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.Bind;
import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * 定制语音
 * Created by sll on 2017/7/13.
 */

public class RecordAudioActivity1 extends BaseActivity implements View.OnClickListener, EasySwitchButton.OnOpenedListener {

    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;

    @Bind(R.id.tv_name)
    TextView tvName;

    @Bind(R.id.tv_custom_time)
    TextView tvCustomTime;

    @Bind(R.id.tv_ask_time)
    TextView tvAskTime;

    @Bind(R.id.tv_ask_question)
    TextView tvAskQuestion;

    @Bind(R.id.time_display)
    Chronometer chronometer;

    @Bind(R.id.mic_icon)
    ImageView micIcon;

    @Bind(R.id.iv_head)
    ImageView ivHead;


    @Bind(R.id.rl_audio_review)
    RelativeLayout audioReview;

    @Bind(R.id.tv_second)
    TextView audioSecond;

    @Bind(R.id.iv_cancel)
    ImageView cancel;

    @Bind(R.id.esb_button)
    EasySwitchButton switchButton;

    public static final String TIMER = "timer";


    private UploadManager uploadManager;

    private int layout_width;
    private int layout_height;
    private int button_size;
    private AudioRecordButton capture_button;
    private RelativeLayout audioLayout;
    private TextView info;


    private LingoRecorder lingoRecorder;
    private String filePath;
    private String aacFilePath;
    private MyAudioPlayer myAudioPlayer;
    private FansAskBean fansAskBean;

    public static final String SAVE_NAME_AAC = File.separator + "test.aac";
    public static final String SAVE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
    public static final String SAVE_NAME_WAV = File.separator + "test.wav";
    private AudioManager audioManager;
    private boolean isOpened=true;
    private int position;
    private int duration;

    @Override
    public int getLayoutId() {
        return R.layout.activity_record_audio;
    }

    @Override
    public void initPresenter() {

    }

    Zone zone = Zone.zone0;
    boolean isCancel;
    public static final String WAV = "wav";
    public static final String AAC = "aac";

    @Override
    public void initView() {


        Bundle bundle = getIntent().getBundleExtra(CustomAudioFragment.FANS_ASK_BUNDLE);
        position = getIntent().getIntExtra(CustomAudioFragment.POSITION,-1);
        if (bundle != null) {
            fansAskBean = (FansAskBean) bundle.getParcelable(CustomAudioFragment.FANS_ASK_BEAN);
            tvName.setText(fansAskBean.getNickName());


            if(fansAskBean.getC_type()==0){
                tvCustomTime.setText("15s定制");
                duration=15;
            }else if(fansAskBean.getC_type()==1){
                tvCustomTime.setText("30s定制");
                duration=30;
            }else if(fansAskBean.getC_type()==2){
                tvCustomTime.setText("45s定制");
                duration=45;
            }
            else if(fansAskBean.getC_type()==3){
                tvCustomTime.setText("60s定制");
                duration=60;
            }

            String askTime = TimeUtil.formatData(TimeUtil.dateFormatYMD, fansAskBean.getAsk_t());

            tvAskTime.setText(askTime);
            tvAskQuestion.setText(fansAskBean.getUask());
            ImageLoaderUtils.displayUrl(this, ivHead, fansAskBean.getHeadUrl());
        }
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, AudioManager.FLAG_PLAY_SOUND);
        getData();

        lingoRecorder = new LingoRecorder();
        myAudioPlayer = new MyAudioPlayer();
        myAudioPlayer.setOnCompleteListener(mCompletionListener);

        filePath = SAVE_PATH + SAVE_NAME_WAV;
        aacFilePath = SAVE_PATH + SAVE_NAME_AAC;
        lingoRecorder.put(WAV, new WavProcessor(filePath));
        lingoRecorder.put(AAC, new AndroidAACProcessor(aacFilePath));


        //get width
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        if (this.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
            layout_width = outMetrics.widthPixels;
        } else {
            layout_width = outMetrics.widthPixels / 2;
        }
        button_size = (int) (layout_width / 4.5f);
        layout_height = button_size + (button_size / 5) * 2 + 100;

        button_size = (int) (layout_width / 4.5f);
        capture_button = new AudioRecordButton(this, button_size,R.drawable.audio);
        FrameLayout.LayoutParams btn_capture_param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        btn_capture_param.addRule(CENTER_IN_PARENT, TRUE);
        btn_capture_param.gravity = Gravity.BOTTOM;
//        btn_capture_param.setMargins(0, 152, 0, 0);
        capture_button.setLayoutParams(btn_capture_param);
        capture_button.setDuration(duration * 1000);

        FrameLayout container = (FrameLayout) findViewById(R.id.picture_button);
        container.addView(capture_button);

        ntTitle.setTitleText("定制语音");
        ntTitle.setRightTitleVisibility(true);
        ntTitle.setRightTitle("确认提交");
        ntTitle.setRightTitleColor(R.color.color_8C0808);
        ntTitle.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File targetFile = new File(filePath);
                if (hasRecorded && targetFile.exists()) {
                    ToastUtils.showShort("提交");
                    publishState();

                } else {
                    ToastUtils.showShort("文件为空");
                }

            }
        });
        ntTitle.setClickable(false);

        audioLayout = (RelativeLayout) findViewById(R.id.audio_layout);


//        capture_button = (CaptureButton)findViewById(R.id.picture_button);
        info = (TextView) findViewById(R.id.tv_info);
//        micIcon = (ImageView) findViewById(R.id.mic_icon);
        capture_button.setCaptureLisenter(new CaptureLisenter() {
            @Override
            public void takePictures() {

            }

            @Override
            public void recordShort(long time) {

            }

            @Override
            public void recordStart() {

                startAnim(true);
                startRecord();
//                ret = true;
            }

            @Override
            public void recordEnd(long time) {
                stopAnim();
                if (isCancel) {
                    hasRecorded = false;
                    isCancel = false;
                    stopRecordUnSave(false);
                    Toast.makeText(RecordAudioActivity1.this, "取消保存", Toast.LENGTH_SHORT).show();
                    //如果是录制结束抬手
                } else {
                    stopRecordWithSave(time);


                }
            }

            @Override
            public void recordZoom(float zoom) {
                if (zoom > 80) {
                    moveAnim();
                    isCancel = true;
                } else {
                    isCancel = false;
                    startAnim(false);
                }
            }

            @Override
            public void recordError() {

            }
        });
//        ntTitle.setLeftTitle("取消");
//        ntTitle.setTvLeftVisiable(true);
//        ntTitle.setLeftImagSrc(0);  //去掉返回图标

        initListener();

        setZone();


    }

    private void getData() {


    }

    boolean hasRecorded = false;

    public void stopRecordWithSave(long time) {
        if (lingoRecorder.isAvailable()) {
            if (lingoRecorder.isRecording()) {
                lingoRecorder.stop();
                hasRecorded = true;
                audioReview.setVisibility(View.VISIBLE);


//                getLongTime(filePath);
                long duration = time / 1000;
                if(duration==0){
                    duration=1;
                }
                audioSecond.setText(duration + "s");

//                    }
//                }).start();


//                Toast.makeText(DemoActivity.this, "文件以保存至：" + path, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int duration = (int) msg.obj;

                    audioSecond.setText(duration + "");
                    break;
            }
        }
    };

    /**
     * 获取视频/音频的时长
     *
     * @param filePath 文件路劲
     * @return 时长
     */
    private void getLongTime(final String filePath) {

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {

                try {
                    int duration = 0;
                    FileInputStream inputStream = new FileInputStream(filePath);


                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(filePath); //在获取前，设置文件路径（应该只能是本地路径）
//                    String widthString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                    String durationStr =retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    retriever.release(); //释放
                    if (!TextUtils.isEmpty(durationStr)) {
                        duration = Integer.valueOf(durationStr);
//                        Message obtain = Message.obtain();
//                        obtain.what = 1;
//                        obtain.obj = duration;
//                        handler.sendMessage(obtain);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }

//            }
//        });
//        thread.start();
    }


    public void stopRecordUnSave(boolean isTimeShort) {
        if (lingoRecorder.isAvailable()) {
            if (lingoRecorder.isRecording()) {
                lingoRecorder.stop();
//                File targetFile = new File("/sdcard/test.wav");

                File targetFile = new File(filePath);
                if (targetFile.exists()) {
                    //不保存直接删掉
                    targetFile.delete();

                    audioReview.setVisibility(View.GONE);
                    if (isTimeShort) {
                        Toast.makeText(RecordAudioActivity1.this, "时间太短,已删", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(RecordAudioActivity1.this, "文件已删除", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    private static final int REQUEST_CODE_PERMISSION = 10010;

    //检查权限
    private boolean checkRecordPermission() {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    new AlertDialog.Builder(this)
                            .setTitle("检查权限")
                            .setMessage("需要权限")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_PERMISSION);
                                    }
                                }
                            }).show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_PERMISSION);
                }
            }
            return false;
        }
        return true;
    }

    public void startRecord() {
        if (!checkRecordPermission()) {
            return;
        }
        // isAvailable 为 false 为录音正在处理时，需要保护避免在这个时候操作录音器
//        if (lingoRecorder.isAvailable()) {
//            if (lingoRecorder.isRecording()) {
//                lingoRecorder.stop();
//            } else {
//                // need get permission
//                lingoRecorder.start("/sdcard/test2.wav");
//                resultView.setText("");
//                recordBtn.setText("stop");
//            }
//        }

        if (lingoRecorder.isAvailable()) {
            if (!lingoRecorder.isRecording()) {
                // need get permission

                //不执行这个,这种方式转码的processor可能会block掉record的线程,导致录音不全
//                lingoRecorder.start("/sdcard/test2.wav");
                //执行这个,这种方式是优化过得,分别在两条线程里玩
                lingoRecorder.start();
//                resultView.setText("");
//                recordBtn.setText("stop");

//            }
            }
        }
    }

    private void startAnim(boolean isStart) {
        audioLayout.setVisibility(View.VISIBLE);
        info.setText("手指上滑取消录制");
//        recordBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.mic_pressed_bg));
        micIcon.setBackgroundDrawable(null);
        micIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.audio_tip));
        if (isStart) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setFormat("%S");
            chronometer.start();
        }
    }

    private void stopAnim() {
        audioLayout.setVisibility(View.GONE);
//        recordBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.mic_bg));
        chronometer.stop();
    }

    private void moveAnim() {
        info.setText("松开手指取消录制");
        micIcon.setBackgroundDrawable(null);
        micIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.cancel_audio));
    }

    private void setZone() {
        if ("华东".equals(AppConfig.AREA)) {
            zone = Zone.zone0;
        } else if ("华南".equals(AppConfig.AREA)) {
            zone = Zone.zone2;
        } else if ("华北".equals(AppConfig.AREA)) {
            zone = Zone.zone1;
        }

        Configuration config = new Configuration.Builder()
                .zone(Zone.zone2) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();

        uploadManager = new UploadManager(config);
//        uploadManager = new UploadManager();
    }

    // TODO: 2017/8/29
    private void initListener() {
        audioReview.setOnClickListener(this);
        switchButton.setOnCheckChangedListener(this);
        cancel.setOnClickListener(this);
//        ntTitle.setOnRightTextListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(feedbackContent.getText().toString().trim()) &&
//                        TextUtils.isEmpty(pathUrl)) {
//                    ToastUtils.showShort("发布内容不能为空");
//                } else {
//                    publishState();//发布状态
//                }
//            }
//        });

    }


//    private UploadManager uploadManager = new UploadManager();

    private void publishState() {

        if (!TextUtils.isEmpty(aacFilePath)) {
            startProgressDialog();
            NetworkAPIFactoryImpl.getUserAPI().getQiNiuToken(new OnAPIListener<UptokenBean>() {
                @Override
                public void onError(Throwable ex) {
                    ToastUtils.showShort("上传音频失败");
                }

                @Override
                public void onSuccess(final UptokenBean uptokenBean) {
                    LogUtils.loge("请求到的http数据:" + uptokenBean);
//                    final QiNiuImageToken tokenEntity = JSON.parseObject(uptokenBean, QiNiuImageToken.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.loge("生成的音频名称是:" + FormatUtil.createImageName());
                            String extName = getExtName(aacFilePath, '.');

                            uploadManager.put(aacFilePath, FormatUtil.createImageName() + extName, uptokenBean.getUptoken(),
                                    new UpCompletionHandler() {
                                        @Override
                                        public void complete(String key, ResponseInfo info, JSONObject response) {
                                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                                            if (info.isOK()) {
                                                Log.i("qiniu", "Upload Success");

                                                //拿到上传的图片地址,请求自己的服务器
//                                                String imageUrl = Constant.QI_NIU_BASE_URL + key;
                                                //星享要拼接区域
//                                                String audioUrl = AppConfig.HUANAN_QI_NIU_PIC_ADRESS + key;
                                                String audioUrl =  key;
                                                LogUtils.loge("获取的音频地址:" + audioUrl);
                                                doSendContent(audioUrl);
                                            } else {
                                                Log.i("qiniu", "Upload Fail");
                                                stopProgressDialog();
                                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                            }
                                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);

                                        }
                                    }, null);
                        }
                    });
                }
            });

        } else {
            ToastUtils.showShort("请选择一张图片");
        }
    }

    private String getExtName(String s, char split) {
        int i = s.lastIndexOf(split);
        int leg = s.length();
        String s1 = i > 0 ? (i + 1) == leg ? " " : s.substring(i, s.length()) : " ";
        return s1;
    }

    //上传到自己的服务器里
    private void doSendContent(final String audioUrl) {

//        String starCode = SharePrefUtil.getInstance().getStarcode();
        // TODO: 2017/8/29
        NetworkAPIFactoryImpl.getDealAPI().publishAnswer(fansAskBean.getId(),isOpened?1:0,audioUrl, new OnAPIListener<RequestResultBean>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("发布失败");
                ToastUtils.showShort("发布失败");
                stopProgressDialog();

            }

            @Override
            public void onSuccess(RequestResultBean requestResultBean) {

//                if (requestResultBean.getCircle_id() != -1) {
//                    EventBus.getDefault().post(new EventBusMessage(-65));
//                    ToastUtils.showShort("发布成功");
//
//                    finish();
//                    stopProgressDialog();
//
//                }
                LogUtils.logd("isOpened:"+(isOpened?1:0));
                if(requestResultBean.getResult()==0){
                    ToastUtils.showShort("回答成功");
                    stopProgressDialog();
                    finish();

                    EventBus.getDefault().post(new AudioMessageEvent(position,audioUrl));

                }
            }
        });
    }

    IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            myAudioPlayer.stop();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_audio_review:
                if (!TextUtils.isEmpty(filePath)) {

                    boolean isMediaPlayerStop = myAudioPlayer.isMediaPlayerStop();
                    boolean isPrepared = !myAudioPlayer.isMediaPlayerPrepared();
                    if (myAudioPlayer != null && isMediaPlayerStop) {

                        myAudioPlayer.setDataSource(aacFilePath);
                    } else if (myAudioPlayer != null && (!myAudioPlayer.isMediaPlayerStop())) {
                        myAudioPlayer.stop();
                    }
                } else {
                    ToastUtils.showShort("filePath为空");
                }
                break;
            case R.id.iv_cancel:
                hasRecorded=false;
                audioReview.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onChecked(View v, boolean isOpened) {
        this.isOpened=isOpened;

        if (isOpened) {
            ToastUtils.showShort("公开");
        } else {
            ToastUtils.showShort("不公开");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myAudioPlayer != null){
            myAudioPlayer.releaseMedia();
            myAudioPlayer=null;
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (myAudioPlayer != null){
//            myAudioPlayer.releaseMedia();
//            myAudioPlayer=null;
//        }
//    }
}
