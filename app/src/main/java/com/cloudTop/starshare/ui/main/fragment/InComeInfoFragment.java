package com.cloudTop.starshare.ui.main.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.Constant;
import com.cloudTop.starshare.base.BaseFragment;
import com.cloudTop.starshare.bean.IncomeReturnBean;
import com.cloudTop.starshare.bean.WithDrawCashHistoryBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.activity.AddBankCardActvivity;
import com.cloudTop.starshare.ui.main.activity.CashActivity;
import com.cloudTop.starshare.ui.main.activity.InComeDetailActivity;
import com.cloudTop.starshare.ui.main.adapter.InComeInfoAdapter;
import com.cloudTop.starshare.utils.DisplayUtil;
import com.cloudTop.starshare.utils.FormatUtil;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.utils.TimeUtil;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.utils.timeselectutils.DatePicker;
import com.cloudTop.starshare.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 收益详情
 * #10
 */

public class InComeInfoFragment extends BaseFragment {

    private int start_year = 2017;
    private int start_month = 1;
    private int start_day = 1;
    private int current_end_year;
    private int current_end_month;
    private int current_end_day;
    private LinearLayout ll_start_time;
    private LinearLayout ll_end_time;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private int after_year7;
    private int after_month7;
    private int after_day7;
    private FrameLayout container;
    RecyclerView mRecyclerView;
    List<IncomeReturnBean> list = new ArrayList<>();
    InComeInfoAdapter inComeInfoAdapter;
    private View head_view;
    private NormalTitleBar nt_title;
    private ChartFragment chartFragment;
    private DatePicker picker;
    private DatePicker startPicker;
    int choosed_year7;
    int choosed_month7;
    int choosed_day7;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_income_info;
    }

    @Override
    public void initPresenter() {

    }


    @Override
    protected void initView() {
        initFindById();

        initAdapter();  //初始化adapter
        getDateTime();
        addChart();
        initListener();
        getData();
    }


    private void initFindById() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        nt_title = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
        nt_title.setTitleText("一周交易数据");
        nt_title.setTvLeftVisiable(false);
        nt_title.setRightTitleVisibility(true);
        nt_title.setRightTitle("提现");

    }


    private void addChart() {
        chartFragment = new ChartFragment(getContext());
        container.addView(chartFragment);
    }

    private void getDateTime() {
        Date yesterdayDate = TimeUtil.getDateBefore(new Date(), 1);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(yesterdayDate);
        current_end_year = c1.get(Calendar.YEAR);
        current_end_month = c1.get(Calendar.MONTH) + 1;
        current_end_day = c1.get(Calendar.DAY_OF_MONTH);

        after_year7 = current_end_year;
        after_month7 = current_end_month;

//        choosed_year7=after_year7;
//        choosed_month7=after_month7;

        String after_month7_tes = FormatUtil.formatDayOrMmonth(current_end_month);
        after_day7 = current_end_day;
//        choosed_day7=after_day7;
        String after_day7_tes = FormatUtil.formatDayOrMmonth(current_end_day);
        tv_end_time.setText(current_end_year + "-" + after_month7_tes + "-" + after_day7_tes);

        Date dateBefore = TimeUtil.getDateBefore(new Date(), 7);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(dateBefore);
        start_year = c2.get(Calendar.YEAR);
        start_month = c2.get(Calendar.MONTH) + 1;

        String start_month_tes = FormatUtil.formatDayOrMmonth(start_month);
        start_day = c2.get(Calendar.DAY_OF_MONTH);
        String start_day_tes = FormatUtil.formatDayOrMmonth(start_day);
        tv_start_time.setText(start_year + "-" + start_month_tes + "-" + start_day_tes);   //7天之前时间
    }


    private void initAdapter() {
//        list = generateData();
        inComeInfoAdapter = new InComeInfoAdapter(R.layout.item_income_type, list);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
//        inComeInfoAdapter.setEnableLoadMore(false);
        mRecyclerView.setAdapter(inComeInfoAdapter);
        mRecyclerView.setLayoutManager(manager);

        inComeInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IncomeReturnBean bean = inComeInfoAdapter.getData().get(position);
                Intent intent = new Intent(getActivity(), InComeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.INCOME_INFO, bean);
                intent.putExtra(Constant.INCOME, bundle);
                startActivity(intent);
            }
        });

        head_view = getActivity().getLayoutInflater().
                inflate(R.layout.layout_income_head_view, (ViewGroup) mRecyclerView.getParent(), false);
        initHeadView();

        inComeInfoAdapter.addHeaderView(head_view);
    }

    private void initHeadView() {
        container = (FrameLayout) head_view.findViewById(R.id.container);
        ll_start_time = (LinearLayout) head_view.findViewById(R.id.ll_start_time);
        ll_end_time = (LinearLayout) head_view.findViewById(R.id.ll_end_time);
        tv_start_time = (TextView) head_view.findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) head_view.findViewById(R.id.tv_end_time);
    }

    private List<WithDrawCashHistoryBean> generateData() {
        List<WithDrawCashHistoryBean> beanList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            WithDrawCashHistoryBean bean = new WithDrawCashHistoryBean();
            bean.setBank("模拟类型:" + i);
            beanList.add(bean);
        }
        return beanList;
    }


    private void initListener() {
        nt_title.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先判断登录  登录后请求银行卡,然后再处理
                String cardNo = SharePrefUtil.getInstance().getCardNo();
                LogUtils.loge("-----------------cardNo:"+cardNo);
                if (TextUtils.isEmpty(cardNo)) {
                    showBindBankDialog();
                } else {
                    startActivity(CashActivity.class);
                }
            }
        });

        ll_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYearMonthStartTime();
            }
        });

        ll_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYearMonthEndTime();
            }
        });
    }

    private void showBindBankDialog() {
        final Dialog mDetailDialog = new Dialog(getActivity(), R.style.custom_dialog);
        mDetailDialog.setContentView(R.layout.dialog_bind_bank_card);
        final Button startIdentity = (Button) mDetailDialog.findViewById(R.id.btn_start_identity);
        ImageView closeImg = (ImageView) mDetailDialog.findViewById(R.id.iv_dialog_close);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailDialog.dismiss();
            }
        });
        mDetailDialog.setCancelable(false);
        startIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddBankCardActvivity.class);
                mDetailDialog.dismiss();
            }
        });
        mDetailDialog.show();
    }

    private void onYearMonthEndTime() {

            picker = new DatePicker(getActivity());
            LogUtils.loge(picker.hashCode()+"");
            picker.setCanceledOnTouchOutside(true);
            picker.setUseWeight(true);
            picker.setTopPadding(DisplayUtil.dip2px(20));
            picker.setOnWheelListener(new DatePicker.OnWheelListener() {
                @Override
                public void onYearWheeled(int index, String year) {
                    picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
                }

                @Override
                public void onMonthWheeled(int index, String month) {
                    picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
                }

                @Override
                public void onDayWheeled(int index, String day) {
                    picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
                }
            });


            picker.setRangeStart(start_year, start_month, start_day);
        //设置结束的时间必须是开始时间 7天之后的
            picker.setRangeEnd(after_year7, after_month7, after_day7);  //结束  最大
              //开始日期
            picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String year, String month, String day) {
                    tv_end_time.setText(year + "-" + month + "-" + day);

//                    LogUtils.loge(year + "-" + month + "-" + day);
                    choosed_year7 = Integer.valueOf(year).intValue();
                    choosed_month7 = Integer.valueOf(month).intValue();
                    choosed_day7 = Integer.valueOf(day).intValue();

                    getData();
                }
            });
        if(isStartDateChanged){
            picker.setSelectedItem(after_year7,after_month7,after_day7);
            isStartDateChanged=false;
        }else{
            picker.setSelectedItem(choosed_year7,choosed_month7,choosed_day7);
        }
        picker.show();
    }
    private boolean isStartDateChanged=false;
    public void onYearMonthStartTime() {


        startPicker = new DatePicker(getActivity());
        LogUtils.loge(startPicker.hashCode()+"");
        startPicker.setCanceledOnTouchOutside(true);
        startPicker.setUseWeight(true);
        startPicker.setTopPadding(DisplayUtil.dip2px(20));
        //设置开始的时间必须是7天之内的
        startPicker.setRangeStart(2017, 1, 1);

        startPicker.setRangeEnd(current_end_year, current_end_month, current_end_day);  //结束  最大
        startPicker.setSelectedItem(start_year, start_month, start_day);
        startPicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                LogUtils.loge(year + "-" + month + "-" + day);
                start_year = Integer.valueOf(year).intValue();
                start_month = Integer.valueOf(month).intValue();
                start_day = Integer.valueOf(day).intValue();
                tv_start_time.setText(year + "-" + month + "-" + day);

                String dateString = year + "-" + month + "-" + day;
                Date dateAfter7 = TimeUtil.getDateByFormat(dateString, "yyyy-MM-dd");
                Date dateBefore = TimeUtil.getDateAfter(dateAfter7, 6);  //7天之后
                Calendar c2 = Calendar.getInstance();
                c2.setTime(dateBefore);
                after_year7 = c2.get(Calendar.YEAR);
                after_month7 = c2.get(Calendar.MONTH) + 1;
                after_day7 = c2.get(Calendar.DAY_OF_MONTH);
                tv_end_time.setText(after_year7 + "-" + after_month7 + "-" + after_day7);

                getData(start_year,start_month,start_day,after_year7,after_month7,after_day7);
                isStartDateChanged=true;

                choosed_day7=0;
                choosed_month7=0;
                choosed_year7=0;
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

    public void getData(int start_year,int start_month,int start_day,int after_year7,int after_month7,int after_day7) {
        String starCode = SharePrefUtil.getInstance().getStarcode();
        int endTime = Integer.parseInt(after_year7 + FormatUtil.formatDayOrMmonth(after_month7) + FormatUtil.formatDayOrMmonth(after_day7));
        int starTime = Integer.parseInt(start_year + FormatUtil.formatDayOrMmonth(start_month) + FormatUtil.formatDayOrMmonth(start_day));
        NetworkAPIFactoryImpl.getDealAPI().requestIncome(starCode, starTime, endTime, new OnAPIListener<List<IncomeReturnBean>>() {
            @Override
            public void onError(Throwable ex) {
                if(ex instanceof  NullPointerException){
                    ToastUtils.showShort("数据为空");
                    list.clear();
                    inComeInfoAdapter.getData().clear();
                    inComeInfoAdapter.notifyDataSetChanged();
                    chartFragment.loadChartData(null);
                }else{
                    LogUtils.loge("收益请求失败----------------");
                    list.clear();
//                inComeInfoAdapter.loadMoreEnd();
//                inComeInfoAdapter.loadMoreEnd(true);
                    inComeInfoAdapter.getData().clear();
//                inComeInfoAdapter.loadMoreEnd();
                    inComeInfoAdapter.notifyDataSetChanged();
//                inComeInfoAdapter.loadMoreComplete();
                    chartFragment.loadChartData(null);
                }
//                inComeInfoAdapter.setNewData(incomeReturnBeen);
            }

            @Override
            public void onSuccess(List<IncomeReturnBean> incomeReturnBeen) {
                LogUtils.loge("收益请求成功----------------");

                if (incomeReturnBeen == null || incomeReturnBeen.size() == 0){
                 return;
                }
                inComeInfoAdapter.setNewData(incomeReturnBeen);
                chartFragment.loadChartData(incomeReturnBeen);
//                getMonthAndDayWithPoint
            }
        });
    }

    public void getData() {
        String starCode = SharePrefUtil.getInstance().getStarcode();
        choosed_year7=choosed_year7==0?after_year7:choosed_year7;
        choosed_month7=choosed_month7==0?after_month7:choosed_month7;
        choosed_day7=choosed_day7==0?after_day7:choosed_day7;

        int endTime = Integer.parseInt(choosed_year7 + FormatUtil.formatDayOrMmonth(choosed_month7) + FormatUtil.formatDayOrMmonth(choosed_day7));
        int starTime = Integer.parseInt(start_year + FormatUtil.formatDayOrMmonth(start_month) + FormatUtil.formatDayOrMmonth(start_day));
        NetworkAPIFactoryImpl.getDealAPI().requestIncome(starCode, starTime, endTime, new OnAPIListener<List<IncomeReturnBean>>() {
            @Override
            public void onError(Throwable ex) {
                if(ex instanceof  NullPointerException){
                    ToastUtils.showShort("数据为空");
                    list.clear();
                    List<IncomeReturnBean> data = inComeInfoAdapter.getData();
                    data.clear();
                    inComeInfoAdapter.notifyDataSetChanged();
                    chartFragment.loadChartData(null);
                }else{
                    LogUtils.loge("收益请求失败----------------");
                    list.clear();
//                inComeInfoAdapter.loadMoreEnd();
//                inComeInfoAdapter.loadMoreEnd(true);
                    inComeInfoAdapter.getData().clear();
//                inComeInfoAdapter.loadMoreEnd();
                    inComeInfoAdapter.notifyDataSetChanged();
//                inComeInfoAdapter.loadMoreComplete();
                    chartFragment.loadChartData(null);
                }
//                inComeInfoAdapter.setNewData(incomeReturnBeen);
            }

            @Override
            public void onSuccess(List<IncomeReturnBean> incomeReturnBeen) {
                LogUtils.loge("收益请求成功----------------");

                if (incomeReturnBeen == null || incomeReturnBeen.size() == 0){
                 return;
                }
                inComeInfoAdapter.setNewData(incomeReturnBeen);
                chartFragment.loadChartData(incomeReturnBeen);
//                getMonthAndDayWithPoint
            }
        });
    }

}
