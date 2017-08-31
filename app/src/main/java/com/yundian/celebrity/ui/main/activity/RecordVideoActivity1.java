package com.yundian.celebrity.ui.main.activity;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yundian.celebrity.R;
import com.yundian.celebrity.app.AppConfig;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.bean.EventBusMessage;
import com.yundian.celebrity.bean.RequestResultBean;
import com.yundian.celebrity.bean.UptokenBean;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.utils.FormatUtil;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.SharePrefUtil;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;


/**
 * 定制视频
 * Created by sll on 2017/7/13.
 */

public class RecordVideoActivity1 extends BaseActivity {

    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.btn_video_record)
    Button videoRecord;

    public static final String TIMER = "timer";

    private String pathUrl = "";
    private UploadManager uploadManager;




    private String filePath;

    @Override
    public int getLayoutId() {
        return R.layout.activity_record_video;
    }

    @Override
    public void initPresenter() {

    }
    Zone zone=Zone.zone0;

    @Override
    public void initView() {

        ntTitle.setTitleText("视频问答");
        ntTitle.setRightTitleVisibility(true);
        ntTitle.setRightTitle("确认提交");
        ntTitle.setRightTitleColor(R.color.color_8C0808);
        ntTitle.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File targetFile = new File(filePath);
                if(hasRecorded&&targetFile.exists()){
                    ToastUtils.showShort("提交");
                }else{
                    ToastUtils.showShort("文件为空");
                }

            }
        });
        ntTitle.setClickable(false);



        initListener();

        setZone();


    }
    boolean hasRecorded=false;


    public static final String SAVE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
    public static final String SAVE_NAME_WAV = File.separator+"test.wav";



    private void setZone() {
        if ("华东".equals(AppConfig.AREA)){
            zone= Zone.zone0;
        }else if("华南".equals(AppConfig.AREA)){
            zone=Zone.zone2;
        }else if("华北".equals(AppConfig.AREA)){
            zone=Zone.zone1;
        }

        Configuration config = new Configuration.Builder()
                .zone(Zone.zone2) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();

        uploadManager = new UploadManager(config);
//        uploadManager = new UploadManager();
    }

    // TODO: 2017/8/29
    private void initListener() {
        videoRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(VideoRecordActivity.class);
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

    private void publishState() {


        if(!TextUtils.isEmpty(pathUrl)){
            startProgressDialog();
            NetworkAPIFactoryImpl.getUserAPI().getQiNiuToken(new OnAPIListener<UptokenBean>() {
                @Override
                public void onError(Throwable ex) {

                }

                @Override
                public void onSuccess(final UptokenBean uptokenBean) {
                    LogUtils.loge("请求到的http数据:" + uptokenBean);
//                    final QiNiuImageToken tokenEntity = JSON.parseObject(uptokenBean, QiNiuImageToken.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.loge("生成的图片名称是:" + FormatUtil.createImageName());
                            String extName = getExtName(pathUrl, '.');

                            uploadManager.put(pathUrl, FormatUtil.createImageName()+extName, uptokenBean.getUptoken(),
                                    new UpCompletionHandler() {
                                        @Override
                                        public void complete(String key, ResponseInfo info, JSONObject response) {
                                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                                            if (info.isOK()) {
                                                Log.i("qiniu", "Upload Success");

                                                //拿到上传的图片地址,请求自己的服务器
//                                                String imageUrl = Constant.QI_NIU_BASE_URL + key;
                                                //星享要拼接区域
                                                String imageUrl =key;
                                                LogUtils.loge("获取的图片地址:" + imageUrl);
                                                doSendContent(imageUrl);
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

        }else{
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
    private void doSendContent(String imageUrl) {

        String starCode = SharePrefUtil.getInstance().getStarcode();
        // TODO: 2017/8/29
        NetworkAPIFactoryImpl.getDealAPI().publishState("", imageUrl, starCode, new OnAPIListener<RequestResultBean>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("发布失败");
                ToastUtils.showShort("发布失败");
                stopProgressDialog();

            }

            @Override
            public void onSuccess(RequestResultBean requestResultBean) {

                if (requestResultBean.getCircle_id() != -1) {
                    EventBus.getDefault().post(new EventBusMessage(-65));
                    ToastUtils.showShort("发布成功");

                    finish();
                    stopProgressDialog();

                }
            }
        });
    }





}
