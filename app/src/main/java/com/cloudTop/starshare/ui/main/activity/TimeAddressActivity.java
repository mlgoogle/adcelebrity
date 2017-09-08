package com.cloudTop.starshare.ui.main.activity;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.EventBusMessage;
import com.cloudTop.starshare.bean.SubmitAddressTimeInfo;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.utils.DisplayUtil;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.TimeUtil;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.utils.timeselectutils.AddressPickTask;
import com.cloudTop.starshare.utils.timeselectutils.City;
import com.cloudTop.starshare.utils.timeselectutils.County;
import com.cloudTop.starshare.utils.timeselectutils.DatePicker;
import com.cloudTop.starshare.utils.timeselectutils.Province;
import com.cloudTop.starshare.widget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

import static android.media.CamcorderProfile.get;
import static com.cloudTop.starshare.R.id.tv_end_time;
import static com.cloudTop.starshare.R.id.tv_start_time;
import static com.cloudTop.starshare.utils.TimeUtil.getStringByFormat;

/**
 * 时间地址管理
 * Created by wuruirui on 2017/7/28.
 * #10
 */

public class TimeAddressActivity extends BaseActivity {
    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.ll_meet_address)
    LinearLayout llMeetAddress;
    @Bind(R.id.tv_address)
    TextView tvAddresss;
    @Bind(tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.ll_start_time)
    LinearLayout llStartTime;
    @Bind(tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.ll_end_time)
    LinearLayout llEndTime;
    @Bind(R.id.tv_confirm)
    TextView tvConfirm;
//    private Calendar calEndYear;
//    private Calendar calTomorrow;
    private Date endYearTime;
    private Date tomorrowDate;
    //    private int current_end_year;
//    private int current_end_month;
//    private int current_end_day;
//    private Calendar c1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_time_address_manager;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ntTitle.setTitleText(getString(R.string.time_address_manager));
        initDateTime();
        initTimeView();
    }

    private void initDateTime() {
        Date currentDate = new Date();
        tomorrowDate = TimeUtil.getDateAfter(currentDate, 1);

//        Calendar calTomorrow = Calendar.getInstance();
//        calTomorrow.setTime(tomorrowDate);
        Calendar calTomorrow=getCalendar(tomorrowDate);
        int tomorrow_end_year = calTomorrow.get(Calendar.YEAR);

//        Calendar calEndYear = Calendar.getInstance();
//        calEndYear.clear();
//        calEndYear.set(Calendar.YEAR, tomorrow_end_year);
//        calEndYear.roll(Calendar.DAY_OF_YEAR, -1);

        Calendar calEndYear=getEndYearCalendar(tomorrow_end_year);
        endYearTime = calEndYear.getTime();
    }

    private void initTimeView() {
        String tomorrowTime = TimeUtil.getStringByFormat(tomorrowDate, TimeUtil.dateFormatYMD);
        tvStartTime.setText(tomorrowTime);
        tvEndTime.setText(tomorrowTime);
    }

    @OnClick({R.id.ll_meet_address,R.id.ll_start_time,  R.id.ll_end_time,R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_meet_address:
                editAddress();
                break;
            case R.id.ll_start_time:
                editMeetStartTime();
                break;
            case R.id.ll_end_time:
                editMeetEndTime();
                break;
            case R.id.tv_confirm:
                submit();
                break;
        }
    }
    //执行上传
    private void submit() {
//        String s = feedbackContent.getText().toString();  //内容
//        String starCode = SharePrefUtil.getInstance().getStarcode();
        String endTime = tvEndTime.getText().toString().trim();
        String startTime = tvStartTime.getText().toString().trim();
        String address = tvAddresss.getText().toString().trim();

        NetworkAPIFactoryImpl.getDealAPI().submitAddressTimeInfo(address, startTime, endTime, new OnAPIListener<SubmitAddressTimeInfo>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("发布失败");
                ToastUtils.showShort("修改失败");
            }

            @Override
            public void onSuccess(SubmitAddressTimeInfo submitAddressTimeInfo) {

//                if (requestResultBean.getCircle_id() != -1) {
//                    EventBus.getDefault().post(new EventBusMessage(-65));
//                    finish();
//                }
                if(submitAddressTimeInfo.getResult().equals(""+1)){
                    ToastUtils.showShort("修改成功");
                }else{
                    ToastUtils.showShort("修改异常");
                }

            }
        });
    }


    private void editMeetEndTime() {
//        String endTime = tvEndTime.getText().toString().trim();
        String startTime = tvStartTime.getText().toString().trim();

        Date startDate;
        Calendar calStart = null;
        if (!TextUtils.isEmpty(startTime)) {
            //如果约会开始时间textView不为空,拿到约会开始时间的calender对象
            startDate = TimeUtil.getDateByFormat(startTime, TimeUtil.dateFormatYMD);
//            calStart = Calendar.getInstance();
//            calStart.clear();
//            calStart.setTime(startDate);
            calStart = getCalendar(startDate);
//            int start_year = calStart.get(Calendar.YEAR);
        }

        final DatePicker endPicker = new DatePicker(this);
//        LogUtils.loge(startPicker.hashCode()+"");
        endPicker.setCanceledOnTouchOutside(true);
        endPicker.setUseWeight(true);
        endPicker.setTopPadding(DisplayUtil.dip2px(20));

        int startYear=calStart.get(Calendar.YEAR);
        int startMonth=calStart.get(Calendar.MONTH)+1;
        int startDay=calStart.get(Calendar.DAY_OF_MONTH);


//        Calendar calEndYear = getCalendar(endYearTime);
        Calendar calEndYear=getEndYearCalendar(startYear);
        int endYear=calEndYear.get(Calendar.YEAR);
        int endMonth=calEndYear.get(Calendar.MONTH)+1;
        int endDay=calEndYear.get(Calendar.DAY_OF_MONTH);

        //范围最开始时间必须比约会开始时间晚
        endPicker.setRangeStart(startYear, startMonth, startDay);
        //明天那天的年底
        endPicker.setRangeEnd(endYear, endMonth, endDay);
//        endPicker.setSelectedItem(startYear, startMonth, startDay);
        endPicker.setSelectedItem(endPickYear==0?startYear:endPickYear, endPickMonth==0?startMonth:endPickMonth, endPickDay==0?startDay:endPickDay);

        endPicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                LogUtils.loge(year + "-" + month + "-" + day);

                String meetEndTime = String.format(getString(R.string.meeting_time), year, month, day);

                Date startDate = TimeUtil.getDateByFormat(tvStartTime.getText().toString().trim(), TimeUtil.dateFormatYMD);
                Date endDate = TimeUtil.getDateByFormat(meetEndTime, TimeUtil.dateFormatYMD);

                if(endDate.getTime()<startDate.getTime()){
                    tvStartTime.setText(meetEndTime);
                }

                tvEndTime.setText(meetEndTime);

                endPickYear=Integer.parseInt(year);
                endPickMonth=Integer.parseInt(month);
                endPickDay=Integer.parseInt(day);

            }
        });
        endPicker.show();
    }
    private int currentPickYear;
    private int currentPickMonth;
    private int currentPickDay;

    private int endPickYear;
    private int endPickMonth;
    private int endPickDay;
    private void editMeetStartTime() {
//        String startTime = tvStartTime.getText().toString().trim();
//        if (!TextUtils.isEmpty(startTime)) {
//
//        }

        final DatePicker startPicker = new DatePicker(this);
//        LogUtils.loge(startPicker.hashCode()+"");
        startPicker.setCanceledOnTouchOutside(true);
        startPicker.setUseWeight(true);
        startPicker.setTopPadding(DisplayUtil.dip2px(20));


        Calendar calTomorrow = getCalendar(tomorrowDate);
        int startYear = calTomorrow.get(Calendar.YEAR);
        int startMonth = calTomorrow.get(Calendar.MONTH)+1;
        int startDay = calTomorrow.get(Calendar.DAY_OF_MONTH);

        Calendar calEndYear = getEndYearCalendar(startYear);
        int endYear=calEndYear.get(Calendar.YEAR);
        int endYearMonth=calEndYear.get(Calendar.MONTH)+1;
        int endYearDay=calEndYear.get(Calendar.DAY_OF_MONTH);

        startPicker.setRangeStart(startYear, startMonth, startDay);  //结束  最大
        startPicker.setRangeEnd(endYear, endYearMonth, endYearDay);
        startPicker.setSelectedItem(currentPickYear==0?startYear:currentPickYear,currentPickMonth==0? startMonth:currentPickMonth, currentPickDay==0? startDay:currentPickDay);
//        String startTime = tvStartTime.getText().toString().trim();
//        Date dateByFormat = TimeUtil.getDateByFormat(startTime, TimeUtil.dateFormatYMD);
//        Calendar calendar = getCalendar(dateByFormat);
//        startPicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        startPicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                LogUtils.loge(year + "-" + month + "-" + day);
//                start_year = Integer.valueOf(year).intValue();
//                start_month = Integer.valueOf(month).intValue();
//                start_day = Integer.valueOf(day).intValue();
//                tv_start_time.setText(year + "-" + month + "-" + day);
//
                String meetStartTime = String.format(getString(R.string.meeting_time), year, month, day);


                Date endDate = TimeUtil.getDateByFormat(tvEndTime.getText().toString().trim(), TimeUtil.dateFormatYMD);
                Date startDate = TimeUtil.getDateByFormat(meetStartTime, TimeUtil.dateFormatYMD);
                if(endDate.getTime()<startDate.getTime()){
                    tvEndTime.setText(meetStartTime);
//                    Calendar c=getCalendar(endDate);
//
//                    endPickYear=c.get(Calendar.YEAR);
//                    endPickMonth=c.get(Calendar.MONTH);
//                    endPickDay=c.get(Calendar.DAY_OF_MONTH);

                    endPickYear=Integer.valueOf(year);
                    endPickMonth=Integer.valueOf(month);
                    endPickDay=Integer.valueOf(day);

                }
                tvStartTime.setText(meetStartTime);

                currentPickYear=Integer.parseInt(year);
                currentPickMonth=Integer.parseInt(month);
                currentPickDay=Integer.parseInt(day);

//                String dateString = year + "-" + month + "-" + day;
//                Date dateAfter7 = TimeUtil.getDateByFormat(dateString, "yyyy-MM-dd");
//                Date dateBefore = TimeUtil.getDateAfter(dateAfter7, 6);  //7天之后
//                Calendar c2 = Calendar.getInstance();
//                c2.setTime(dateBefore);
//                after_year7 = c2.get(Calendar.YEAR);
//                after_month7 = c2.get(Calendar.MONTH) + 1;
//                after_day7 = c2.get(Calendar.DAY_OF_MONTH);
//                tv_end_time.setText(after_year7 + "-" + after_month7 + "-" + after_day7);
//
//                getData();
//                isStartDateChanged=true;
            }
        });
        startPicker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                startPicker.setTitleText(year + "-" + startPicker.getSelectedMonth() + "-" + startPicker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                startPicker.setTitleText(startPicker.getSelectedYear() + "-" + month + "-" + startPicker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                startPicker.setTitleText(startPicker.getSelectedYear() + "-" + startPicker.getSelectedMonth() + "-" + day);
            }
        });
        startPicker.show();
    }

    @NonNull
    private Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
//        cal.clear();
        cal.setTime(date);
        return cal;
    }
    private Calendar getEndYearCalendar(int  year) {
        Calendar calEndYear = Calendar.getInstance();
//        calEndYear.clear();
        calEndYear.set(year,11,31);
//        calEndYear.set(Calendar.MONTH, 12);
//        calEndYear.set(Calendar.DAY_OF_MONTH, 31);
//        calEndYear.roll(Calendar.DAY_OF_YEAR, -1);
        Date time = calEndYear.getTime();
        return calEndYear;
    }
    private void editAddress() {
        LogUtils.loge("点击了");
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {

            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    tvAddresss.setText(province.getAreaName() + city.getAreaName());
                } else {
                    tvAddresss.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                }
            }
        });
        task.execute("浙江", "杭州", "上城区");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new EventBusMessage(-77));
    }


//    //地理位置选择器
//    public void onAddressPicker() {
//        LogUtils.loge("点击了");
//        AddressPickTask task = new AddressPickTask(this);
//        task.setHideProvince(false);
//        task.setHideCounty(false);
//        task.setCallback(new AddressPickTask.Callback() {
//            @Override
//            public void onAddressInitFailed() {
//
//            }
//
//            @Override
//            public void onAddressPicked(Province province, City city, County county) {
//                if (county == null) {
//                    textView4.setText(province.getAreaName() + city.getAreaName());
//                } else {
//                    textView4.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
//                }
//            }
//        });
//        task.execute("浙江", "杭州", "上城区");
//    }
}
