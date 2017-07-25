package com.yundian.celebrity.ui.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.IncomeReturnBean;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.TimeUtil;
import com.yundian.celebrity.wxapi.LineMarkerView;

import java.util.ArrayList;
import java.util.List;

/**
 * K线图
 */
public class ChartFragment extends FrameLayout {

    private View chatView;
    private Context context;
    private int colorLine;
    private int colorText;
    private LineMarkerView mvLine;
    private LineChart mLineChart;
    private List<IncomeReturnBean> lineEntities;

    public ChartFragment(Context context) {
        super(context);
        this.context = context;
        initChart();
    }

    public ChartFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChart();
    }

    public ChartFragment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChart();
    }

    private void initChart() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (chatView == null) {
            chatView = View.inflate(getContext(), R.layout.fragment_chart, null);
        }
        addView(chatView, params);
        mLineChart = (LineChart) chatView.findViewById(R.id.chart);
        initChartData();
    }

    public void initChartData() {
        int colorHomeBg = context.getResources().getColor(R.color.color_fafafa); //背景色
        int colorDivide = context.getResources().getColor(R.color.color_e5e5e5);//分割线
        colorText = context.getResources().getColor(R.color.color_FB9938);
        //条目
        colorLine = context.getResources().getColor(R.color.color_FB9938);
        mLineChart.setNoDataTextDescription("当前时间段没有收益数据");
        mLineChart.setDescription("");//描述信息
        mLineChart.setDrawGridBackground(false); //是否显示表格颜色
        mLineChart.setBackgroundColor(colorHomeBg);
        mLineChart.setGridBackgroundColor(colorHomeBg);
        mLineChart.setScaleYEnabled(true);  //Y轴激活
        mLineChart.setPinchZoom(false); //设置x轴和y轴能否同时缩放。默认是否
        mLineChart.setNoDataText("");
//        mLineChart.setAutoScaleMinMaxEnabled(true);
        mLineChart.setDragEnabled(true); //可以拖拽
        mLineChart.setScaleEnabled(false);  //放大缩小
        mLineChart.getLegend().setEnabled(false);//图例
        mLineChart.setTouchEnabled(true);
        mLineChart.animateX(1000);

        mLineChart.setDrawBorders(false);  //设置图表内格子外的边框是否显示
//        mLineChart.setBorderColor(colorDivide);   //上面的边框颜色

        mLineChart.setDescriptionColor(colorText);
        mLineChart.setDescriptionTextSize(16);

//        mLineChart.requestDisallowInterceptTouchEvent(true);
//        mLineChart.setExtraLeftOffset(50);
        mLineChart.setExtraRightOffset(10);
        XAxis xAxis = mLineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(colorLine);
        xAxis.setTextColor(Color.rgb(102, 102, 102));
        xAxis.setTextSize(14);
//        xAxis.setSpaceBetweenLabels(4);// 轴刻度间的宽度，默认值是4
        xAxis.resetLabelsToSkip();

        xAxis.setEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setLabelCount(6, false);

        leftAxis.setGridColor(colorDivide);
        leftAxis.setTextColor(colorLine);
        leftAxis.setTextSize(14);


//        leftAxis.setDrawAxisLine(true); //是否绘制坐标轴的线，即含有坐标的那条线，默认是true

        //rightAxis.setAxisMinValue(0);此方法虽然可以设置最小值为0，但是起点都会从0开始
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawLabels(true);  //显示刻度

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);
        refreshMarkerView();
        initListener();
    }

    private void initListener() {
        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (lineEntities == null) {
                    return;
                }
                entry.setData(lineEntities.get(entry.getXIndex()).getOrderdate());
                mvLine.refreshContent(entry, highlight);
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    public void refreshMarkerView() {
        mvLine = new LineMarkerView(getContext(), R.layout.ly_marker_line);
        mLineChart.setMarkerView(mvLine);
    }


    public void loadChartData(List<IncomeReturnBean> dataList) {
        this.lineEntities = dataList;
        mLineChart.resetTracking();
        if (dataList == null) {
            mLineChart.clear();
            mLineChart.invalidate();
            return;
        }
        int count = dataList.size();
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            String dateX = TimeUtil.getMonthAndDayWithPoint(dataList.get(i).getOrderdate());
            LogUtils.loge("处理后的dateX:" + dateX);
            xValues.add(dateX);
        }

        // y轴的数据
        List<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float value = (float) (dataList.get(i).getOrder_count());
            yValues.add(new Entry(value, i));
        }
        // y轴的数据集合
        LineDataSet lineDataSet = generateLineDataSet(yValues, colorLine, "");
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);


        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        mLineChart.setData(lineData);
        refreshMarkerView();

        mLineChart.invalidate();
    }

    private LineDataSet generateLineDataSet(List<Entry> entries, int color, String label) {
        LineDataSet set = new LineDataSet(entries, label);
        set.setColor(color);
        set.setLineWidth(1.0f);
        set.setDrawCubic(false);//圆滑曲线
        set.setDrawCircles(true);//设置有圆点
        set.setDrawFilled(false);  //设置包括的范围区域填充颜色
//        set.setFillColor(colorText);
        set.setCubicIntensity(0.2f);
//        set.setDrawCircleHole(false);
        set.setDrawValues(true);
        set.setCircleColorHole(Color.WHITE);
        set.setValueTextColor(colorText);
        set.setValueTextSize(11);
        set.setCircleSize(5f);// 显示的圆形大小
        set.setCircleColor(colorText);// 圆形的颜色
        set.setDrawCircleHole(true);
        set.setHighlightEnabled(true);
        set.setHighLightColor(Color.rgb(204, 204, 204)); // 高亮的线的颜色
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set.setHighLightColor(R.color.color_666666);
        return set;
    }
}
