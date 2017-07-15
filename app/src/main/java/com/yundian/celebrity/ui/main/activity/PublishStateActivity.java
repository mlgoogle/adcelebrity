package com.yundian.celebrity.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.session.constant.Extras;
import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.ui.view.ValidationWatcher;
import com.yundian.celebrity.utils.ImageLoaderUtils;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.netease.nimlib.sdk.msg.constant.SystemMessageStatus.init;

/**
 * 发布状态
 * Created by sll on 2017/7/13.
 */

public class PublishStateActivity extends BaseActivity {

    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.feedbackContent)
    EditText feedbackContent;
    @Bind(R.id.feedbackContentNumber)
    TextView feedbackContentNumber;  //指示器
    @Bind(R.id.iv_add_pic)
    ImageView ivAddPic;
    @Bind(R.id.iv_clear)
    ImageView ivClear;
    private String pathUrl = "";


    @Override
    public int getLayoutId() {
        return R.layout.activity_publish_state;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ntTitle.setRightTitleVisibility(true);
        ntTitle.setRightTitle("发布");
        ntTitle.setRightTitleColor(R.color.color_8C0808);

        ntTitle.setLeftTitle("取消");
        ntTitle.setTvLeftVisiable(true);
        ntTitle.setLeftImagSrc(0);  //去掉返回图标

        initListener();

        feedbackContent.addTextChangedListener(mValidationWatcher);
    }

    private void initListener() {
        ntTitle.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(feedbackContent.getText().toString().trim()) &&
                        TextUtils.isEmpty(pathUrl)) {
                    TextUtils.isEmpty("发布内容不能为空");
                } else {
                    TextUtils.isEmpty("fabu");
                }
            }
        });

    }

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            String importAbilityWords = String.valueOf(200 - editable.toString().trim().length());
            feedbackContentNumber.setText(String.format(getString(R.string.remainder_input_count), importAbilityWords));

//            if (!TextUtils.isEmpty(editable.toString().trim())) {
//                mFeedbackSubmit.setEnabled(true);
//            } else {
//                mFeedbackSubmit.setEnabled(false);
//            }
        }
    };

    @OnClick({R.id.iv_add_pic, R.id.iv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_pic:
                ToastUtils.showShort("添加图片...");
                showSelector(R.string.add_pic, 100);
                break;
            case R.id.iv_clear:
                ivAddPic.setImageResource(R.drawable.about_logo);
                ivClear.setVisibility(View.GONE);
                pathUrl = "";
                break;
        }
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, final int requestCode) {
        PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
        option.titleResId = titleId;
        option.multiSelect = false;
        option.crop = true;
        option.cropOutputImageWidth = 720;
        option.cropOutputImageHeight = 720;
        PickImageHelper.pickImage(PublishStateActivity.this, requestCode, option);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.logd("jieshoudao 拍摄的回调:" + requestCode);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            String path = data.getStringExtra(Extras.EXTRA_FILE_PATH);
            updatePic(path);
        }
    }

    /**
     * 更新头像
     */
    private void updatePic(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file == null) {
            return;
        }
        LogUtils.loge("获取到上传的图片的地址:" + path);
        ImageLoaderUtils.displaySmallPhotoRound(mContext, ivAddPic, path);
        pathUrl = path;
        ivClear.setVisibility(View.VISIBLE);
    }

}
