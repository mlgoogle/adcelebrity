package com.yundian.celebrity.ui.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseFragment;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import com.yundian.celebrity.ui.main.activity.InComeDetailActivity;
import com.yundian.celebrity.ui.main.adapter.InComeInfoAdapter;
import com.yundian.celebrity.utils.DisplayUtil;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.TimeUtil;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.utils.timeselectutils.DatePicker;
import com.yundian.celebrity.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 收益详情
 */

public class InComeInfoFragment extends BaseFragment {

    private static int mCurrentCounter = 1;
    private static final int REQUEST_COUNT = 10;
    private int start_year = 2017;
    //    private int end_month;
    private int start_month = 1;
    //    private int end_day;
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
    //    private FrameLayout chartView;
//    private ScrollView scrollView;
//        private SwipeRefreshLayout swipeLayout;


    RecyclerView mRecyclerView;
    List<WithDrawCashHistoryBean> list;
    InComeInfoAdapter inComeInfoAdapter;
    private View head_view;
    private NormalTitleBar nt_title;

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
        initData();  //初始化adapter

        getDateTime();
        addChart();

        initListener();

//        getData(false, 1);
    }


    private void addChart() {
        ChartFragment chartFragment = new ChartFragment(getContext());
//        chartView.addView(chartFragment);
        container.addView(chartFragment);
    }

    private void getDateTime() {
        Calendar c1 = Calendar.getInstance();
        current_end_year = c1.get(Calendar.YEAR);
        current_end_month = c1.get(Calendar.MONTH) + 1;
        current_end_day = c1.get(Calendar.DAY_OF_MONTH);

        after_year7 = current_end_year;
        after_month7 = current_end_month;
        after_day7 = current_end_day;

        tv_end_time.setText(current_end_year + "-" + current_end_month + "-" + current_end_day);

        Date dateBefore = TimeUtil.getDateBefore(new Date(), 6);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(dateBefore);
        start_year = c2.get(Calendar.YEAR);
        start_month = c2.get(Calendar.MONTH) + 1;
        start_day = c2.get(Calendar.DAY_OF_MONTH);
        tv_start_time.setText(start_year + "-" + start_month + "-" + start_day);
    }


    private void initFindById() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        nt_title = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
        nt_title.setTitleText("一周交易数据");
        nt_title.setTvLeftVisiable(false);
        nt_title.setRightTitleVisibility(true);
        nt_title.setRightTitle("提现");

    }

    private void initData() {
        list = generateData();
        inComeInfoAdapter = new InComeInfoAdapter(R.layout.item_income_type, list);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        head_view = getActivity().getLayoutInflater().
                inflate(R.layout.layout_income_head_view, (ViewGroup) mRecyclerView.getParent(), false);
        initHeadView();

        inComeInfoAdapter.addHeaderView(head_view);

        mRecyclerView.setAdapter(inComeInfoAdapter);
        // important! setLayoutManager should be called after setAdapter

        mRecyclerView.setLayoutManager(manager);

        inComeInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort("点击了自条目" + position);
                startActivity(InComeDetailActivity.class);
            }
        });
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

//    private void initAdapter() {
//        historyEntrustAdapter = new HistoryEntrustAdapter(getActivity());
//        lRecyclerViewAdapter = new LRecyclerViewAdapter(historyEntrustAdapter);
//        lrv.setAdapter(lRecyclerViewAdapter);
//        lrv.setLayoutManager(new LinearLayoutManager(getContext()));
//        lrv.setPullRefreshEnabled(false);
//        lrv.setNoMore(false);
//        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
//                .setHeight(R.dimen.dp_0_5)
//                .setPadding(R.dimen.dp_25)
//                .setColorResource(R.color.color_dcdcdc)
//                .build();
//        //mRecyclerView.setHasFixedSize(true);
//        lrv.addItemDecoration(divider);
//        lrv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        lrv.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                getData(true, mCurrentCounter + 1);
//            }
//        });
//    }
//
//    private void getData(final boolean isLoadMore, int start) {
//        NetworkAPIFactoryImpl.getInformationAPI().todayEntrust(start, REQUEST_COUNT, AppConstant.HISTORY_ENTRUST_OPCODE, new OnAPIListener<List<TodayEntrustReturnBean>>() {
//            @Override
//            public void onError(Throwable ex) {
//                if (lrv != null) {
//                    lrv.setNoMore(true);
//                    if (!isLoadMore) {
//                        list.clear();
//                        historyEntrustAdapter.clear();
//                        lrv.refreshComplete(REQUEST_COUNT);
//                        showErrorView(parentView, R.drawable.error_view_comment, "当前没有相关数据");
//                    }
//                }
//            }
//
//            @Override
//            public void onSuccess(List<TodayEntrustReturnBean> todayEntrustReturnBeen) {
//                if (todayEntrustReturnBeen == null) {
//                    lrv.setNoMore(true);
//                    return;
//                }
//                if (isLoadMore) {
//                    closeErrorView();
//                    loadList.clear();
//                    loadList = todayEntrustReturnBeen;
//                    loadMoreData();
//                } else {
//                    list.clear();
//                    list = todayEntrustReturnBeen;
//                    showData();
//                }
//            }
//        });
//    }
//
//    public void showData() {
//        if (list.size() == 0) {
//            showErrorView(parentView, R.drawable.error_view_comment, "当前没有相关数据");
//            return;
//        } else {
//            closeErrorView();
//        }
//        historyEntrustAdapter.clear();
//        mCurrentCounter = list.size();
//        lRecyclerViewAdapter.notifyDataSetChanged();
//        historyEntrustAdapter.addAll(list);
//        lrv.refreshComplete(REQUEST_COUNT);
//    }
//
//    private void loadMoreData() {
//        if (loadList == null || list.size() == 0) {
//            lrv.setNoMore(true);
//        } else {
//            list.addAll(loadList);
//            historyEntrustAdapter.addAll(loadList);
//            mCurrentCounter += loadList.size();
//            lrv.refreshComplete(REQUEST_COUNT);
//        }
//    }

    private void initListener() {
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

    private void onYearMonthEndTime() {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(DisplayUtil.dip2px(20));

        //设置结束的时间必须是开始时间 7天之后的
        picker.setRangeEnd(after_year7, after_month7, after_day7);  //结束  最大
        picker.setRangeStart(start_year, start_month, start_day);    //开始日期
        picker.setSelectedItem(after_year7, after_month7, after_day7);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tv_end_time.setText(year + "-" + month + "-" + day);

                //调用获取数据
                //getData();
            }
        });
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
        picker.show();
    }

    public void onYearMonthStartTime() {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(DisplayUtil.dip2px(20));
        //设置开始的时间必须是7天之内的
        picker.setRangeStart(2017, 1, 1);
        picker.setSelectedItem(start_year, start_month, start_day);
        picker.setRangeEnd(current_end_year, current_end_month, current_end_day);  //结束  最大
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
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


            }
        });
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
        picker.show();
    }


}
