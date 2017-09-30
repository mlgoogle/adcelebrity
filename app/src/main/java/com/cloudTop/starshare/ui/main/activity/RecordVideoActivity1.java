package com.cloudTop.starshare.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.AppConfig;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.FansAskBean;
import com.cloudTop.starshare.bean.RequestResultBean;
import com.cloudTop.starshare.event.VideoMessageEvent;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.fragment.CustomAudioFragment;
import com.cloudTop.starshare.ui.main.fragment.VideoAskFragment;
import com.cloudTop.starshare.utils.ImageLoaderUtils;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.utils.TimeUtil;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.widget.EasySwitchButton;
import com.cloudTop.starshare.widget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;

import static com.cloudTop.starshare.ui.main.fragment.VideoAskFragment.FANS_ASK_BEAN;
import static com.cloudTop.starshare.ui.main.fragment.VideoAskFragment.FANS_ASK_BUNDLE;
import static com.cloudTop.starshare.ui.main.fragment.VideoAskFragment.SELF_WATCH_TYPE;


/**
 * 定制视频
 * Created by sll on 2017/7/13.
 */

public class RecordVideoActivity1 extends BaseActivity {

    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.btn_video_record)
    ImageView videoRecord;

    @Bind(R.id.rl_frame)
    RelativeLayout rl_frame;

    @Bind(R.id.rl_video_watch)
    RelativeLayout rl_video_watch;

    @Bind(R.id.esb_button)
    EasySwitchButton esbButton;

    @Bind(R.id.cancel)
    ImageView cancel;

    @Bind(R.id.tv_name)
    TextView tvName;

    @Bind(R.id.tv_ask_video)
    TextView tvAskVideo;

    @Bind(R.id.tv_ask_time)
    TextView tvAskTime;

    @Bind(R.id.tv_custom_time)
    TextView tvCustomTime;

    @Bind(R.id.tv_ask_question)
    TextView tvAskQuestion;

    @Bind(R.id.iv_head)
    ImageView ivHead;

    @Bind(R.id.frame)
    ImageView img_video;

    public static final String TIMER = "timer";

    private String pathUrl = "";
    private UploadManager uploadManager;


//    private String filePath;
    private FansAskBean fansAskBean;
    private String framePath;
    private String videoPath;
    private String video_url;
    public boolean isOpened=true;
    private String videoName;
    private String frameName;
    private int position;
    private int duration;

    @Override
    public int getLayoutId() {
        return R.layout.activity_record_video;
    }

    @Override
    public void initPresenter() {

    }

    Zone zone = Zone.zone0;

    @Override
    public void initView() {

        Bundle bundle = getIntent().getBundleExtra(FANS_ASK_BUNDLE);
        position = getIntent().getIntExtra(VideoAskFragment.POSITION,-1);
        duration = getIntent().getIntExtra(VideoAskFragment.DURATION,-1);

        if (bundle != null) {
            fansAskBean = (FansAskBean) bundle.getParcelable(CustomAudioFragment.FANS_ASK_BEAN);
            tvName.setText(fansAskBean.getNickName());
            video_url = fansAskBean.getVideo_url();

            if(TextUtils.isEmpty(video_url)){
                tvAskVideo.setVisibility(View.INVISIBLE);
            }

            if (fansAskBean.getC_type() == 0) {
                tvCustomTime.setText("15s定制");
            } else if (fansAskBean.getC_type() == 1) {
                tvCustomTime.setText("30s定制");
            } else if (fansAskBean.getC_type() == 2) {
                tvCustomTime.setText("45s定制");
            }
            else if (fansAskBean.getC_type() == 3) {
                tvCustomTime.setText("60s定制");
            }

            String askTime = TimeUtil.formatData(TimeUtil.dateFormatYMD, fansAskBean.getAsk_t());

            tvAskTime.setText(askTime);
            tvAskQuestion.setText(fansAskBean.getUask());
            ImageLoaderUtils.displayUrl(this, ivHead, fansAskBean.getHeadUrl());
        }

        ntTitle.setTitleText("视频问答");
        ntTitle.setRightTitleVisibility(true);
        ntTitle.setRightTitle("确认提交");
        ntTitle.setRightTitleColor(R.color.color_8C0808);
        ntTitle.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoPath!=null&&framePath!=null){
                    File targetFile = new File(videoPath);
                    File frameFile = new File(framePath);
                    if (hasRecorded && targetFile.exists()&& frameFile.exists()) {
                        ToastUtils.showShort("提交");

                        doSendContent();
                    } else {
                        ToastUtils.showShort("文件为空");
                    }
                }else{
                    ToastUtils.showShort("文件为空");
                }
            }
        });



        initListener();

        setZone();


    }

    boolean hasRecorded = false;





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


    private void initListener() {
        rl_video_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordVideoActivity1.this,PlayActivity.class);
                intent.putExtra("playUrl",videoPath);
                intent.putExtra(VideoAskFragment.VIDEO_TYPE,SELF_WATCH_TYPE);
                Bundle bundle = new Bundle();
//                bundle.putParcelable(FANS_ASK_BEAN,fansAskBean);
                intent.putExtra(FANS_ASK_BUNDLE,bundle);

                startActivity(intent);
            }
        });

        videoRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecordVideoActivity1.this,VideoRecordActivity.class);
                intent.putExtra(VideoAskFragment.DURATION,duration);

                Bundle bundle = new Bundle();
                bundle.putParcelable(FANS_ASK_BEAN,fansAskBean);
                intent.putExtra(FANS_ASK_BUNDLE,bundle);
                intent.putExtra(VideoAskFragment.DURATION,duration);
                startActivityForResult(intent, 170);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hasRecorded=false;
                videoPath=null;
                framePath=null;

                rl_frame.setVisibility(View.GONE);
            }
        });

        tvAskVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(video_url)){

                    Intent intent = new Intent(RecordVideoActivity1.this,PlayActivity.class);
                    intent.putExtra("playUrl",AppConfig.QI_NIU_PIC_ADRESS+video_url);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(FANS_ASK_BEAN,fansAskBean);
                    intent.putExtra(FANS_ASK_BUNDLE,bundle);

                    startActivity(intent);
                }else{
                    ToastUtils.showShort("视频为空");
                }
            }
        });

        esbButton.setOnCheckChangedListener(new EasySwitchButton.OnOpenedListener() {


            @Override
            public void onChecked(View v, boolean isOpened) {
                RecordVideoActivity1.this.isOpened=isOpened;
            }
        });

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 170 && resultCode == RESULT_OK) {
            rl_frame.setVisibility(View.VISIBLE);

            framePath = intent.getStringExtra(VideoRecordActivity.FRAMEPATH);
            videoPath = intent.getStringExtra(VideoRecordActivity.VIDEOPATH);
            videoName = intent.getStringExtra(VideoRecordActivity.VIDEONAME);
            frameName = intent.getStringExtra(VideoRecordActivity.FRAMENAME);
            LogUtils.logd("接收到返回的内容:本地缩略图:"+framePath+"本地视频:"+videoPath+"线上缩略图:"+frameName+"线上视频:"+videoName);
            File frameFile = new File(framePath);
            ImageLoaderUtils.display(this, img_video, frameFile);
            hasRecorded=true;

        }
    }

//    private void publishState() {
//
//
//        if (!TextUtils.isEmpty(pathUrl)) {
//            startProgressDialog();
//            NetworkAPIFactoryImpl.getUserAPI().getQiNiuToken(new OnAPIListener<UptokenBean>() {
//                @Override
//                public void onError(Throwable ex) {
//
//                }
//
//                @Override
//                public void onSuccess(final UptokenBean uptokenBean) {
//                    LogUtils.loge("请求到的http数据:" + uptokenBean);
////                    final QiNiuImageToken tokenEntity = JSON.parseObject(uptokenBean, QiNiuImageToken.class);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            LogUtils.loge("生成的图片名称是:" + FormatUtil.createImageName());
//                            String extName = getExtName(pathUrl, '.');
//
//                            uploadManager.put(pathUrl, FormatUtil.createImageName() + extName, uptokenBean.getUptoken(),
//                                    new UpCompletionHandler() {
//                                        @Override
//                                        public void complete(String key, ResponseInfo info, JSONObject response) {
//                                            //res包含hash、key等信息，具体字段取决于上传策略的设置
//                                            if (info.isOK()) {
//                                                Log.i("qiniu", "Upload Success");
//
//                                                //拿到上传的图片地址,请求自己的服务器
////                                                String imageUrl = Constant.QI_NIU_BASE_URL + key;
//                                                //星享要拼接区域
//                                                String imageUrl = key;
//                                                LogUtils.loge("获取的图片地址:" + imageUrl);
//                                                doSendContent(imageUrl);
//                                            } else {
//                                                Log.i("qiniu", "Upload Fail");
//                                                stopProgressDialog();
//                                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
//                                            }
//                                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
//
//                                        }
//                                    }, null);
//                        }
//                    });
//                }
//            });
//
//        } else {
//            ToastUtils.showShort("请选择一张图片");
//        }
//    }

    private String getExtName(String s, char split) {
        int i = s.lastIndexOf(split);
        int leg = s.length();
        String s1 = i > 0 ? (i + 1) == leg ? " " : s.substring(i, s.length()) : " ";
        return s1;
    }

    //上传到自己的服务器里
    private void doSendContent() {

        String starCode = SharePrefUtil.getInstance().getStarcode();
        LogUtils.logd("上传视频到服务器,视频:"+videoName+"缩略图:"+frameName);
        // TODO: 2017/8/29
        NetworkAPIFactoryImpl.getDealAPI().publishVideoAnswer(fansAskBean.getId(),isOpened?1:0, videoName,frameName, new OnAPIListener<RequestResultBean>() {
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

                if(requestResultBean.getResult()==0){
                    ToastUtils.showShort("发布成功");
                    EventBus.getDefault().post(new VideoMessageEvent(position,videoName,frameName));
                    finish();

                }
            }
        });
    }


}
