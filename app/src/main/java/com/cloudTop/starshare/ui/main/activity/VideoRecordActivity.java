package com.cloudTop.starshare.ui.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.AppConfig;
import com.cloudTop.starshare.base.BasePlayActivity;
import com.cloudTop.starshare.bean.FansAskBean;
import com.cloudTop.starshare.bean.UptokenBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.fragment.CustomAudioFragment;
import com.cloudTop.starshare.ui.main.fragment.VideoAskFragment;
import com.cloudTop.starshare.utils.ImageLoaderUtils;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.widget.photobutton.CaptureLayout;
import com.cloudTop.starshare.widget.photobutton.lisenter.CaptureLisenter;
import com.cloudTop.starshare.widget.photobutton.lisenter.TypeLisenter;
import com.cloudTop.starshare.widget.videorecorder.Config;
import com.cloudTop.starshare.widget.videorecorder.CustomProgressDialog;
import com.cloudTop.starshare.widget.videorecorder.FocusIndicator;
import com.cloudTop.starshare.widget.videorecorder.RecordSettings;
import com.cloudTop.starshare.widget.videorecorder.ToastUtils;
import com.cloudTop.starshare.widget.videorecorder.render.GLRenderer;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.pili.droid.shortvideo.PLAudioEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLCameraSetting;
import com.qiniu.pili.droid.shortvideo.PLErrorCode;
import com.qiniu.pili.droid.shortvideo.PLFaceBeautySetting;
import com.qiniu.pili.droid.shortvideo.PLFocusListener;
import com.qiniu.pili.droid.shortvideo.PLMicrophoneSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordStateListener;
import com.qiniu.pili.droid.shortvideo.PLShortVideoRecorder;
import com.qiniu.pili.droid.shortvideo.PLShortVideoTrimmer;
import com.qiniu.pili.droid.shortvideo.PLShortVideoUploader;
import com.qiniu.pili.droid.shortvideo.PLUploadProgressListener;
import com.qiniu.pili.droid.shortvideo.PLUploadResultListener;
import com.qiniu.pili.droid.shortvideo.PLUploadSetting;
import com.qiniu.pili.droid.shortvideo.PLVideoEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLVideoFrame;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

import static android.view.View.GONE;
import static com.cloudTop.starshare.ui.main.fragment.VideoAskFragment.FANS_ASK_BUNDLE;


public class VideoRecordActivity extends BasePlayActivity implements PLRecordStateListener, PLVideoSaveListener, PLFocusListener, PLUploadProgressListener, PLUploadResultListener {
    private static final String PREVIEW = "preview";
    private static final String PLAYBACK = "play_back";
    protected static final String FRAMEPATH = "framePath";
    protected static final String FRAMENAME = "frameName";
    protected static final String VIDEOPATH = "videoPath";
    protected static final String VIDEONAME = "videoName";
    private static final String TAG = "VideoRecordActivity";
    /**
     * NOTICE: KIWI needs extra cost
     */
    private static final boolean USE_KIWI = false;

    private PLShortVideoRecorder mShortVideoRecorder;

//    private CustomProgressDialog mProcessingDialog;

    private View mSwitchCameraBtn;
//    private View mSwitchFlashBtn;
    private FocusIndicator mFocusIndicator;
//    private SeekBar mAdjustBrightnessSeekBar;

    private boolean mFlashEnabled;
    private String mRecordErrorMsg;
//    private boolean mIsEditVideo = false;

    private GestureDetector mGestureDetector;

    private PLCameraSetting mCameraSetting;

//    private KiwiTrackWrapper mKiwiTrackWrapper;

    private int mFocusIndicatorX;
    private int mFocusIndicatorY;

    private boolean isRecording;

    public int textureId;


    private int CAMERA_STATE = -1;
    private static final int STATE_IDLE = 0x010;
    private static final int STATE_RUNNING = 0x020;
    private static final int STATE_WAIT = 0x030;

    private boolean takePictureing = false;
    private boolean stopping = false;
    private boolean isBorrow = false;
    private boolean isTooShort;
    private MediaPlayer mMediaPlayer;

    private ViewGroup rootView;

    private GLRenderer glRenderer;
    private MediaPlayer mediaPlayer;
    private String filePath;
    private FrameLayout container;
    private ProgressBar progressBar;
    private PLShortVideoUploader mVideoUploadManager;
    private boolean mIsUpload = false;
    private CustomProgressDialog mProcessingDialog;
    private CaptureLayout mCaptureLayout;
    private UploadManager uploadManager;
    private String bitmapPath;
    private String imageName;
    private String uptoken;
    private ImageView ivHead;
    private RelativeLayout rlAskContent;
    private TextView tvName;
    private TextView tvQuestionContent;
    private FansAskBean fansAskBean;
    private ImageView ivQuestion;
    private MyHandler mHandler ;
    private int successCount;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Window window = getWindow();

        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setHideVirtualKey(window);
            }
        });

        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getBundleExtra(FANS_ASK_BUNDLE);
        if (bundle != null) {
            fansAskBean = (FansAskBean) bundle.getParcelable(CustomAudioFragment.FANS_ASK_BEAN);
        }
        LayoutInflater inflater = getLayoutInflater();  //调用Activity的getLayoutInflater)
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_record, null);

        setContentView(rootView);
        getToken();

        mHandler = new MyHandler(this);
        //预览区

        container = (FrameLayout) findViewById(R.id.glsurface_container);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        ivQuestion = (ImageView) findViewById(R.id.iv_question);

        rlAskContent = (RelativeLayout) findViewById(R.id.rl_ask_content);
        ivHead = (ImageView) findViewById(R.id.iv_head);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvQuestionContent = (TextView) findViewById(R.id.tv_question_content);
        ImageLoaderUtils.displayUrl(this,ivHead,fansAskBean.getHeadUrl());
        tvName.setText(fansAskBean.getNickName());
        tvQuestionContent.setText(fansAskBean.getUask());

        mCaptureLayout = (CaptureLayout) findViewById(R.id.layout_capture);

        int duration = getIntent().getIntExtra(VideoAskFragment.DURATION, 15);


        mCaptureLayout.setDuration(duration * 1000);

        mCaptureLayout.setEnabled(false);


        mCaptureLayout.setCaptureLisenter(new CaptureLisenter() {
            @Override
            public void takePictures() {
                //不使用拍照功能
            }

            @Override
            public void recordShort(long time) {
                if (CAMERA_STATE != STATE_RUNNING && stopping) {
                    return;
                }
                stopping = false;
                mCaptureLayout.setTextWithAnimation("录制时间过短");
                mShortVideoRecorder.endSection();
                isTooShort = true;
                mCaptureLayout.isRecord(false);
                CAMERA_STATE = STATE_WAIT;

                isBorrow = false;
            }

            @Override
            public void recordStart() {
                if (CAMERA_STATE != STATE_IDLE && stopping) {
                    return;
                }

                mCaptureLayout.isRecord(true);
                isBorrow = true;
                CAMERA_STATE = STATE_RUNNING;

                if (isTooShort) {
                    mShortVideoRecorder.deleteLastSection();
                    isRecording = false;
                    isTooShort = false;
                }

                if (!isRecording && mShortVideoRecorder.beginSection()) {
                    //进度条不为0了
                    isRecording = !isRecording;
//                        更新按钮的状态
//                    updateRecordingBtns(true);
                } else {
                    ToastUtils.s(VideoRecordActivity.this, "无法开始视频段录制");
                    Log.i("CJT", "startRecorder error");
                    mCaptureLayout.isRecord(false);
                    CAMERA_STATE = STATE_WAIT;
                    stopping = false;
                    isBorrow = false;
                }
            }

            @Override
            public void recordEnd(long time) {
                Log.i(TAG, "recordEnd: ");
                if (isRecording) {
                    boolean b = mShortVideoRecorder.endSection();
                }
            }

            @Override
            public void recordZoom(float zoom) {
                Log.i(TAG, "recordZoom: ");
            }

            //在button检查当前录音机有没有被占用了,如果占用了走到这里,已注释
            @Override
            public void recordError() {
                //错误回调
                Log.i(TAG, "recordError: ");
            }
        });
        mCaptureLayout.setTypeLisenter(new TypeLisenter() {
            @Override
            public void cancel() {
                // TODO: 2017/8/22


                if (View.GONE == container.findViewWithTag(PREVIEW).getVisibility()) {
                    container.findViewWithTag(PREVIEW).setVisibility(View.VISIBLE);
                }
                if (container.findViewWithTag(PLAYBACK) != null) {
                    container.removeView(container.findViewWithTag(PLAYBACK));
                }

//                container.removeAllViews();
//                GLSurfaceView preview = new GLSurfaceView(VideoRecordActivity.this);
//                preview.setTag("preview");
//                container.addView(preview);

//                play_back.setVisibility(View.GONE);
//                preview.setVisibility(View.VISIBLE);

                Log.i(TAG, "cancel: ");
                if (!mShortVideoRecorder.deleteLastSection()) {
                    ToastUtils.s(VideoRecordActivity.this, "回删视频段失败");
                }

                //录像layout重新初始化一下
                Log.i("CJT", "startRecorder error");
                mCaptureLayout.isRecord(false);
                CAMERA_STATE = STATE_WAIT;
                stopping = false;
                isBorrow = false;

                //flag改为可录像状态
                isRecording = !isRecording;

                releaseMediaPlayer();

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void confirm() {

                // TODO: 2017/8/22  
//                play_back.setVisibility(View.GONE);
//                preview.setVisibility(View.VISIBLE);

                if (GONE == container.findViewWithTag(PREVIEW).getVisibility()) {
                    container.findViewWithTag(PREVIEW).setVisibility(View.VISIBLE);

                }
                if (container.findViewWithTag(PLAYBACK) != null) {
                    container.removeView(container.findViewWithTag(PLAYBACK));
                }

                if (!mShortVideoRecorder.deleteLastSection()) {
                    ToastUtils.s(VideoRecordActivity.this, "回删视频段失败");
                }

                mCaptureLayout.isRecord(false);
                CAMERA_STATE = STATE_WAIT;
                stopping = false;
                isBorrow = false;

                isRecording = false;

                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    showChooseDialog();
                }
                progressBar.setVisibility(View.GONE);
                releaseMediaPlayer();
            }
        });

        ivQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rlAskContent.getVisibility()==View.VISIBLE){
                    rlAskContent.setVisibility(View.GONE);
                }else{
                    rlAskContent.setVisibility(View.VISIBLE);
                }
            }
        });

        mSwitchCameraBtn = findViewById(R.id.switch_camera);
//        mSwitchFlashBtn = findViewById(R.id.switch_flash);
        mFocusIndicator = (FocusIndicator) findViewById(R.id.focus_indicator);
//        mAdjustBrightnessSeekBar = (SeekBar) findViewById(R.id.adjust_brightness);

        //进行处理的对话框
//        mProcessingDialog = new CustomProgressDialog(this);
//        mProcessingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                mShortVideoRecorder.cancelConcat();
//            }
//        });
        //video的录像器
        mShortVideoRecorder = new PLShortVideoRecorder();
        //录像状态的监听
        mShortVideoRecorder.setRecordStateListener(this);
        //焦点的监听
        mShortVideoRecorder.setFocusListener(this);


        int previewSizeRatio = getIntent().getIntExtra("PreviewSizeRatio", 0);
        int previewSizeLevel = getIntent().getIntExtra("PreviewSizeLevel", 0);
        int encodingSizeLevel = getIntent().getIntExtra("EncodingSizeLevel", 0);
        int encodingBitrateLevel = getIntent().getIntExtra("EncodingBitrateLevel", 0);

        //camera的设置
        mCameraSetting = new PLCameraSetting();
        //设置camera用哪个吧
        PLCameraSetting.CAMERA_FACING_ID facingId = chooseCameraFacingId();

        mCameraSetting.setCameraId(facingId);
        //把camera拍到的内容显示在preview上的一些设置
        mCameraSetting.setCameraPreviewSizeRatio(getPreviewSizeRatio(0));
        mCameraSetting.setCameraPreviewSizeLevel(getPreviewSizeLevel(6));

        //麦克风的设置
        PLMicrophoneSetting microphoneSetting = new PLMicrophoneSetting();

        //video编码的设置
        PLVideoEncodeSetting videoEncodeSetting = new PLVideoEncodeSetting(this);
        videoEncodeSetting.setEncodingSizeLevel(getEncodingSizeLevel(17));
        videoEncodeSetting.setEncodingBitrate(getEncodingBitrateLevel(6));

        //音频的设置
        PLAudioEncodeSetting audioEncodeSetting = new PLAudioEncodeSetting();

        //record录像的设置
        PLRecordSetting recordSetting = new PLRecordSetting();
        recordSetting.setMaxRecordDuration(duration*1000);
        recordSetting.setVideoCacheDir(Config.VIDEO_STORAGE_DIR);

        String fileName = createFileName() + "record.mp4";
        recordSetting.setVideoFilepath(Config.RECORD_FILE_PATH + fileName);

        //美颜的设置
        PLFaceBeautySetting faceBeautySetting = new PLFaceBeautySetting(1.0f, 0.5f, 0.5f);

        GLSurfaceView preview = new GLSurfaceView(this);
        preview.setTag(PREVIEW);
        FrameLayout.LayoutParams previewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        preview.setLayoutParams(previewParams);
        preview.setEGLContextClientVersion(2);
        container.addView(preview);

//        //全部设置给recorder
        mShortVideoRecorder.prepare(preview, mCameraSetting, microphoneSetting, videoEncodeSetting,
                audioEncodeSetting, USE_KIWI ? null : faceBeautySetting, recordSetting);
        mShortVideoRecorder.setFlashEnabled(false);

//        if (USE_KIWI) {
//            //不用美颜库暂时注释掉
//            StickerConfigMgr.setSelectedStickerConfig(null);
//
//            mKiwiTrackWrapper = new KiwiTrackWrapper(this, mCameraSetting.getCameraId());
//            mKiwiTrackWrapper.onCreate(this);
//
////            mControlView = (KwControlView) findViewById(R.id.kiwi_control_layout);
////            mControlView.setOnEventListener(mKiwiTrackWrapper.initUIEventListener());
////            mControlView.setVisibility(VISIBLE);
//
//            mShortVideoRecorder.setVideoFilterListener(new PLVideoFilterListener() {
//
//                private int surfaceWidth;
//                private int surfaceHeight;
//                private boolean isTrackerOnSurfaceChangedCalled;
//
//                @Override
//                public void onSurfaceCreated() {
//                    mKiwiTrackWrapper.onSurfaceCreated(VideoRecordActivity.this);
//                }
//
//                @Override
//                public void onSurfaceChanged(int width, int height) {
//                    surfaceWidth = width;
//                    surfaceHeight = height;
//                }
//
//                @Override
//                public void onSurfaceDestroy() {
//                    mKiwiTrackWrapper.onSurfaceDestroyed();
//                }
//
//                @Override
//                public int onDrawFrame(int texId, int texWidth, int texHeight, long l) {
//                    if (!isTrackerOnSurfaceChangedCalled) {
//                        isTrackerOnSurfaceChangedCalled = true;
//                        mKiwiTrackWrapper.onSurfaceChanged(surfaceWidth, surfaceHeight, texWidth, texHeight);
//                    }
//                    VideoRecordActivity.this.textureId = texId;
//                    return mKiwiTrackWrapper.onDrawFrame(texId, texWidth, texHeight);
//                }
//            });
//        }
        //手势识别
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                mFocusIndicatorX = (int) e.getX() - mFocusIndicator.getWidth() / 2;
                mFocusIndicatorY = (int) e.getY() - mFocusIndicator.getHeight() / 2;
                //手动的对焦
                mShortVideoRecorder.manualFocus(mFocusIndicator.getWidth(), mFocusIndicator.getHeight(), (int) e.getX(), (int) e.getY());
                return false;
            }
        });
        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                交给手势识别处理
                mGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });


        // TODO: 2017/8/25
        PLUploadSetting uploadSetting = new PLUploadSetting()
                .setChunkSize(2*256*1024) ;          //分片上传时，每片的大小，默认256K
//                .setPutThreshhold(putthreshhold)   // 启用分片上传阀值，默认512K
//                .setConnectTimeout(connectTimeout) // 链接超时，默认10秒
//                .setResponseTimeout(responseTimeout) // 服务器响应超时，默认60秒
//                .setZone(plUploadZone);


        mVideoUploadManager = new PLShortVideoUploader(getApplicationContext(), uploadSetting);
        mVideoUploadManager.setUploadProgressListener(this);
        mVideoUploadManager.setUploadResultListener(this);
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void playMedia() {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            } else {
                mMediaPlayer.reset();
            }

            String filePath = "/storage/emulated/0/ShortVideo/record.mp4";
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            mMediaPlayer.setDataSource(fis.getFD());

            //用同一个SurfaceView
            SurfaceTexture surfaceTexture = new SurfaceTexture(textureId);
            Surface surface = new Surface(surfaceTexture);

            mMediaPlayer.setSurface(surface);
            surface.release();
            mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer
                    .OnVideoSizeChangedListener() {
                @Override
                public void
                onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                                            updateVideoViewSize(mMediaPlayer.getVideoWidth(), mMediaPlayer
//                                                    .getVideoHeight());
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    Log.d("", "");
                }
            });
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //更新录像按钮
    private void updateRecordingBtns(boolean isRecording) {
        mSwitchCameraBtn.setEnabled(isRecording);
//        mRecordBtn.setActivated(isRecording);
//        progressButton.setActivated(isRecording);
    }

//    /**
//     * TextureView resize
//     */
//    public void updateVideoViewSize(float videoWidth, float videoHeight) {
//        if (videoWidth > videoHeight) {
//            FrameLayout.LayoutParams videoViewParam;
//            int height = (int) ((videoHeight / videoWidth) * getWidth());
//            videoViewParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                    height);
//            videoViewParam.gravity = Gravity.CENTER;
////            videoViewParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//            mVideoView.setLayoutParams(videoViewParam);
//        }
//    }

    //手动的捕捉一个关键帧
//    public void onCaptureFrame(View v) {
//        mShortVideoRecorder.captureFrame(new PLCaptureFrameListener() {
//            @Override
//            public void onFrameCaptured(PLVideoFrame capturedFrame) {
//                if (capturedFrame == null) {
//                    Log.e(TAG, "capture frame failed");
//                    return;
//                }
//
//                Log.i(TAG, "captured frame width: " + capturedFrame.getWidth() + " height: " + capturedFrame.getHeight() + " timestamp: " + capturedFrame.getTimestampMs());
//                try {
//                    //转为bitmap保存起来
//                    FileOutputStream fos = new FileOutputStream(Config.CAPTURED_FRAME_FILE_PATH);
//                    capturedFrame.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.close();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtils.s(VideoRecordActivity.this, "截帧已保存到路径：" + Config.CAPTURED_FRAME_FILE_PATH);
//                        }
//                    });
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


    /**
     * 生命周期
     **/
    @Override
    protected void onResume() {
        super.onResume();
//        progressButton.setEnabled(false);
//        if (mKiwiTrackWrapper != null) {
//            mKiwiTrackWrapper.onResume(this);
//        }
        updateRecordingBtns(true);
        mCaptureLayout.setEnabled(true);
        mShortVideoRecorder.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mKiwiTrackWrapper != null) {
//            mKiwiTrackWrapper.onPause(this);
//        }
        updateRecordingBtns(false);
        mCaptureLayout.setEnabled(false);
        mShortVideoRecorder.pause();

//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.resetWithError();
//            mediaPlayer = null;
//        }
    }

    public void getToken() {
        NetworkAPIFactoryImpl.getUserAPI().getQiNiuToken(new OnAPIListener<UptokenBean>() {
            @Override
            public void onError(Throwable ex) {
                com.cloudTop.starshare.utils.ToastUtils.showShort("获取token失败");
            }

            @Override
            public void onSuccess(UptokenBean uptokenBean) {
                VideoRecordActivity.this.uptoken = uptokenBean.getUptoken();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mKiwiTrackWrapper != null) {
//            mKiwiTrackWrapper.onDestroy(this);
//        }
        mShortVideoRecorder.destroy();
        releaseMediaPlayer();
    }
   /* 设置生命周期结束*/


    //点击切换换一个摄像头
    public void onClickSwitchCamera(View v) {
//        if (mKiwiTrackWrapper != null) {
//            mKiwiTrackWrapper.switchCamera(mCameraSetting.getCameraId());
//        }
        mShortVideoRecorder.switchCamera();
    }

    //点击是否打开闪光灯
//    public void onClickSwitchFlash(View v) {
//        mFlashEnabled = !mFlashEnabled;
//        mShortVideoRecorder.setFlashEnabled(mFlashEnabled);
//        mSwitchFlashBtn.setActivated(mFlashEnabled);
//    }

    //准备开始录制的一些初始化回调
    @Override
    public void onReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mSwitchFlashBtn.setVisibility(mShortVideoRecorder.isFlashSupport() ? VISIBLE : GONE);
                mFlashEnabled = false;
//                mSwitchFlashBtn.setActivated(mFlashEnabled);
                mCaptureLayout.setEnabled(true);
//                progressButton.setEnabled(true);

                //调整亮度值
//                refreshSeekBar();
                ToastUtils.s(VideoRecordActivity.this, "可以开始拍摄咯");
            }
        });
    }

    //录制出现error
    @Override
    public void onError(int code) {
        if (code == PLErrorCode.ERROR_SETUP_CAMERA_FAILED) {
            mRecordErrorMsg = "摄像头配置错误";
        } else if (code == PLErrorCode.ERROR_SETUP_MICROPHONE_FAILED) {
            mRecordErrorMsg = "麦克风配置错误";
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.s(VideoRecordActivity.this, mRecordErrorMsg);
            }
        });
    }

    //拍摄的太短的回调
    @Override
    public void onDurationTooShort() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.s(VideoRecordActivity.this, "该视频段太短了");
            }
        });
    }

    //开始录制
    @Override
    public void onRecordStarted() {
        Log.i(TAG, "record start time: " + System.currentTimeMillis());
//        mSectionProgressBar.setCurrentState(SectionProgressBar.State.START);
    }

    @Override
    public void onRecordStopped() {
        Log.i(TAG, "record stop time: " + System.currentTimeMillis());

        Thread thread = Thread.currentThread();
        Log.i(TAG, "thread: " + thread);

//        mIsEditVideo = false;
        if (!isTooShort) {
            mShortVideoRecorder.concatSections(VideoRecordActivity.this);
        }
//        mSectionProgressBar.setCurrentState(SectionProgressBar.State.PAUSE);
    }


    //拍摄好一段后的回调
    @Override
    public void onSectionIncreased(long incDuration, long totalDuration, int sectionCount) {
        Log.i(TAG, "section increased incDuration: " + incDuration + " totalDuration: " + totalDuration + " sectionCount: " + sectionCount);

    }

    //删除前面一段视频的回删
    @Override
    public void onSectionDecreased(long decDuration, long totalDuration, int sectionCount) {
        Log.i(TAG, "section decreased decDuration: " + decDuration + " totalDuration: " + totalDuration + " sectionCount: " + sectionCount);
//        onSectionCountChanged(sectionCount, totalDuration);
//        mSectionProgressBar.removeLastBreakPoint();
    }

    //录制到最大时长的回调
    @Override
    public void onRecordCompleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.s(VideoRecordActivity.this, "已达到拍摄总时长");
            }
        });
    }

    //保存到本地,进度更新的回调
    @Override
    public void onProgressUpdate(float percentage) {
//        mProcessingDialog.setProgress((int) (100 * percentage));
    }

    //保存到本地失败的回调
    @Override
    public void onSaveVideoFailed(final int errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mProcessingDialog.dismiss();
                ToastUtils.s(VideoRecordActivity.this, "拼接视频段失败: " + errorCode);
            }
        });
    }


//    private Handler handleProgress = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0://更新进度条
//
//                    if (mediaPlayer != null) {
//                        int position = mediaPlayer.getCurrentPosition();
//                        if (position >= 0) {
//                            progressBar.setProgress(position);
////                        String cur = UIUtils.getShowTime(position);
////                        currTimeText.setText(cur);
//                        }
//                        break;
//                    }
//            }
//        }
//    };

    //取消
    @Override
    public void onSaveVideoCanceled() {
//        mProcessingDialog.dismiss();
    }

    public long createFileName() {
        Random random = new Random();
        long number = System.currentTimeMillis() + random.nextInt(999999999);
        return number;
    }

    //保存成功
    @Override
    public void onSaveVideoSuccess(final String filePath) {

        this.filePath = filePath;
        Log.i(TAG, "concat sections success filePath: " + filePath);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: 2017/8/22  
//                play_back.setVisibility(View.VISIBLE);
//                preview.setVisibility(View.GONE);

                if (container.findViewWithTag(PLAYBACK) == null) {
                    GLSurfaceView play_back = new GLSurfaceView(VideoRecordActivity.this);

                    FrameLayout.LayoutParams playBackParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    play_back.setLayoutParams(playBackParams);

                    play_back.setTag(PLAYBACK);
                    play_back.setEGLContextClientVersion(2);
                    container.addView(play_back);
                }
                if (container.findViewWithTag(PREVIEW) != null && View.VISIBLE == container.findViewWithTag(PREVIEW).getVisibility()) {
                    //                    七牛还在用,不能释放
//                    container.removeView(container.findViewWithTag("preview"));
                    container.findViewWithTag(PREVIEW).setVisibility(View.GONE);
                }

                if (glRenderer == null) {
                    glRenderer = new GLRenderer(VideoRecordActivity.this);

                    glRenderer.setPlayerCallback(new GLRenderer.PlayerCallback() {
                        @Override
                        public void updateProgress() {
                            if (glRenderer.getMediaPlayer() != null) {
                                mHandler.sendEmptyMessage(0);
                            }
                        }

                        @Override
                        public void updateInfo() {

                        }

                        @Override
                        public void requestFinish() {

                        }
                    });
                    GLSurfaceView play_back = (GLSurfaceView) container.findViewWithTag(PLAYBACK);
                    play_back.setRenderer(glRenderer);
                    play_back.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                } else {
                    // TODO: 2017/8/22  
                    GLSurfaceView play_back = (GLSurfaceView) container.findViewWithTag(PLAYBACK);
                    play_back.setRenderer(glRenderer);
                    play_back.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                }

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(VideoRecordActivity.this, Uri.parse(filePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setOnVideoSizeChangedListener(glRenderer);

                    glRenderer.setMediaPlayer(mediaPlayer);
                    int duration = glRenderer.getMediaPlayer().getDuration();
                    progressBar.setMax(duration);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private PLCameraSetting.CAMERA_PREVIEW_SIZE_RATIO getPreviewSizeRatio(int position) {
        return RecordSettings.PREVIEW_SIZE_RATIO_ARRAY[position];
    }

    private PLCameraSetting.CAMERA_PREVIEW_SIZE_LEVEL getPreviewSizeLevel(int position) {
        return RecordSettings.PREVIEW_SIZE_LEVEL_ARRAY[position];
    }

    private PLVideoEncodeSetting.VIDEO_ENCODING_SIZE_LEVEL getEncodingSizeLevel(int position) {
        return RecordSettings.ENCODING_SIZE_LEVEL_ARRAY[position];
    }

    private int getEncodingBitrateLevel(int position) {
        return RecordSettings.ENCODING_BITRATE_LEVEL_ARRAY[position];
    }

    private PLCameraSetting.CAMERA_FACING_ID chooseCameraFacingId() {
        if (PLCameraSetting.hasCameraFacing(PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (PLCameraSetting.hasCameraFacing(PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }

    //弹出是否编辑的对话框
    private void showChooseDialog() {
        mProcessingDialog = new CustomProgressDialog(VideoRecordActivity.this);
        mProcessingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mVideoUploadManager.cancelUpload();
                mIsUpload = false;
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否上传视频");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                upLoadVideo();
                upLoadCover();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mIsEditVideo = false;
                Toast.makeText(VideoRecordActivity.this, "不上传", Toast.LENGTH_SHORT).show();



//                mShortVideoRecorder.concatSections(VideoRecordActivity.this);
            }
        });
        // TODO: 2017/8/16
        //把之前录的清空
        builder.setCancelable(false);
        builder.create().show();
    }

    private void upLoadVideo() {
        //进行处理的对话框
        mProcessingDialog.show();
//        NetworkAPIFactoryImpl.getUserAPI().getQiNiuToken(new OnAPIListener<UptokenBean>() {
//            @Override
//            public void onError(Throwable ex) {
//                ToastUtils.showShort("获取七牛token异常");
//            }
//
//            @Override
//            public void onSuccess(UptokenBean uptokenBean) {
//
//            }
//        });

        if (!mIsUpload) {
            if (!TextUtils.isEmpty(uptoken)) {
                Toast.makeText(VideoRecordActivity.this, "开始上传", Toast.LENGTH_SHORT).show();
                mVideoUploadManager.startUpload(filePath, uptoken);
                mIsUpload = true;
            }
        } else {
            Toast.makeText(VideoRecordActivity.this, "当前正在上传", Toast.LENGTH_SHORT).show();
        }

    }
    private class MyHandler extends Handler{
        private final WeakReference<VideoRecordActivity> mActivity;
        private MyHandler(VideoRecordActivity videoRecordActivity) {
            mActivity = new WeakReference<>(videoRecordActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoRecordActivity activity = mActivity.get();
            if(activity!=null){
                switch (msg.what){
                    case 0://更新进度条

                        if (mediaPlayer != null) {
                            int position = mediaPlayer.getCurrentPosition();
                            if (position >= 0) {
                                progressBar.setProgress(position);
//                        String cur = UIUtils.getShowTime(position);
//                        currTimeText.setText(cur);
                            }
                            break;
                        }

                    case 1 :
                        if (successCount==2){
                            mProcessingDialog.dismiss();
                            mIsUpload = false;

                            Intent intent = new Intent();
                            intent.putExtra(FRAMEPATH, bitmapPath);
                            intent.putExtra(FRAMENAME, imageName);
                            intent.putExtra(VIDEOPATH, filePath);
                            intent.putExtra(VIDEONAME, fileName);
                            LogUtils.logd("接收到返回的内容:本地缩略图:" + bitmapPath + "本地视频:" + filePath + "线上缩略图:" + imageName + "线上视频:" + fileName);
                            activity.setResult(RESULT_OK, intent);
                            activity.finish();
                            successCount=0;
                        }

                        break;
                }
            }
        }
    }
    private void upLoadCover() {
        if (uploadManager == null) {
            Configuration config = new Configuration.Builder()
                    .zone(Zone.zone2) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                    .build();
            uploadManager = new UploadManager(config);
        }

        final String fileName = createFileName() + "frame.jpg";
        bitmapPath = getBitmap(fileName);

        if (uptoken == null && TextUtils.isEmpty(uptoken)) {
            com.cloudTop.starshare.utils.ToastUtils.showShort("七牛token为空");
            return;
        }

        // TODO: 2017/8/25
        uploadManager.put(bitmapPath, fileName, uptoken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            Log.i("qiniu", "Upload Success");

                            //拿到上传的图片地址,请求自己的服务器
//                                                String imageUrl = Constant.QI_NIU_BASE_URL + key;
                            imageName = key;
                            LogUtils.loge("获取的图片地址:" + imageName);
                            successCount++;
                            mHandler.sendEmptyMessage(1);
//                            doSendContent(imageUrl);
                        } else {
                            Log.i("qiniu", "Upload Fail");

                            com.cloudTop.starshare.utils.ToastUtils.showShort("缩略图上传失败" + info.error);
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);

                    }
                }, null);
    }


    private String getBitmap(String fileName) {
        // TODO: 2017/8/15
        PLShortVideoTrimmer mShortVideoTrimmer = new PLShortVideoTrimmer(VideoRecordActivity.this, filePath, AppConfig.TRIM_STORAGE_DIR);

//        long srcDurationMs = mShortVideoTrimmer.getSrcDurationMs();
        int videoFrameCount = mShortVideoTrimmer.getVideoFrameCount(true);
        PLVideoFrame videoFrame = mShortVideoTrimmer.getVideoFrameByIndex(0, true);
        Bitmap bitmap = videoFrame.toBitmap();

        try {

            //转为bitmap保存起来
            FileOutputStream fos = new FileOutputStream(AppConfig.TRIM_STORAGE_DIR + "/" + fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    com.cloudTop.starshare.utils.ToastUtils.showShort("截帧已保存到路径：" + AppConfig.TRIM_STORAGE_DIR);
                }
            });
            bitmap.recycle();
//            bitmap=null;
            return AppConfig.TRIM_STORAGE_DIR + "/" + fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
//        Drawable drawable =new BitmapDrawable(bitmap);

    }


    @Override
    public void onManualFocusStart(boolean result) {
        if (result) {
            Log.i(TAG, "manual focus begin success");
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFocusIndicator.getLayoutParams();
            lp.leftMargin = mFocusIndicatorX;
            lp.topMargin = mFocusIndicatorY;
            mFocusIndicator.setLayoutParams(lp);
            mFocusIndicator.focus();
        } else {
            mFocusIndicator.focusCancel();
            Log.i(TAG, "manual focus not supported");
        }
    }

    @Override
    public void onManualFocusStop(boolean result) {
        Log.i(TAG, "manual focus end result: " + result);
        if (result) {
            mFocusIndicator.focusSuccess();
        } else {
            mFocusIndicator.focusFail();
        }
    }

    @Override
    public void onManualFocusCancel() {
        Log.i(TAG, "manual focus canceled");
        mFocusIndicator.focusCancel();
    }

    @Override
    public void onAutoFocusStart() {
        Log.i(TAG, "auto focus start");
    }

    @Override
    public void onAutoFocusStop() {
        Log.i(TAG, "auto focus stop");
    }

    @Override
    public void onUploadProgress(String fileName, double percent) {
        mProcessingDialog.setProgress((int) (100 * percent));
    }

    @Override
    public void onUploadVideoSuccess(String fileName) {
//        ToastUtils.s(VideoRecordActivity.this, "上传成功");
//        ToastUtils.l(this, "文件上传成功，" + fileName + "已复制到粘贴板");
        this.fileName=fileName;
        successCount++;
        mHandler.sendEmptyMessage(1);

//        mProcessingDialog.dismiss();
//        mIsUpload = false;
//
//        Intent intent = new Intent();
//        intent.putExtra(FRAMEPATH, bitmapPath);
//        intent.putExtra(FRAMENAME, imageName);
//        intent.putExtra(VIDEOPATH, filePath);
//        intent.putExtra(VIDEONAME, fileName);
//        LogUtils.logd("接收到返回的内容:本地缩略图:"+bitmapPath+"本地视频:"+filePath+"线上缩略图:"+imageName+"线上视频:"+fileName);
//        setResult(RESULT_OK, intent);
//        finish();
    }

    @Override
    public void onUploadVideoFailed(int i, String s) {
        ToastUtils.s(VideoRecordActivity.this, "上传失败");
        mProcessingDialog.dismiss();
        mIsUpload = false;
    }

    public void setHideVirtualKey(Window window){
        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT>=19){
            uiOptions |= 0x00001000;
        }else{
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }

}
