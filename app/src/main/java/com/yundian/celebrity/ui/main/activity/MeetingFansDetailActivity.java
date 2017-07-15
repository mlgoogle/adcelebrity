package com.yundian.celebrity.ui.main.activity;
import android.widget.TextView;

import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.ui.view.RoundImageView;
import com.yundian.celebrity.widget.NormalTitleBar;

import butterknife.Bind;

/**
 * Created by sll on 2017/7/12.
 */

public class MeetingFansDetailActivity extends BaseActivity {
    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.headImage)
    RoundImageView headImage;
    @Bind(R.id.tv_petname)
    TextView tvPetname;
    @Bind(R.id.tv_meeting_detail_time)
    TextView tvMeetingDetailTime;
    @Bind(R.id.tv_meeting_detail_place)
    TextView tvMeetingDetailPlace;
    @Bind(R.id.tv_meeting_detail_type)
    TextView tvMeetingDetailType;
    @Bind(R.id.tv_meeting_detail_additional)
    TextView tvMeetingDetailAdditional;

    @Override
    public int getLayoutId() {
        return R.layout.activity_meet_fans_detail;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {






    }


}
