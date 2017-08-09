package com.yundian.celebrity.wxapi;

import android.content.Context;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.yundian.celebrity.R;
import com.yundian.celebrity.utils.DisplayUtil;

/**
 * Created by Administrator on 2017/3/20.
 */
public class RadiusMarkerView extends MarkerView {
    private LineChart mLineChart;
    private Context context;
    private TextView currentPrice;
//    private TextView time;
    private LinearLayout markerView;
//    private ViewParent parent;

    public RadiusMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        currentPrice = (TextView) findViewById(R.id.tv_currentPrice);
//        time = (TextView) findViewById(R.id.time);
        markerView = (LinearLayout) findViewById(R.id.ll_marker_line);
        this.context = context;
    }

    public RadiusMarkerView(LineChart mLineChart, Context context, int ly_marker_view) {
        this(context,ly_marker_view);
        this.mLineChart=mLineChart;
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        if (entry.getData() == null) {
            markerView.setVisibility(INVISIBLE);
        } else {
            markerView.setVisibility(VISIBLE);
            currentPrice.setText(entry.getVal() + "");
//            time.setText(entry.getData() + "");
        }

//        parent = getParent();

    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() /2);
    }

    @Override
    public int getYOffset(float ypos) {
        int px = DisplayUtil.dip2px(10);
        return -getHeight()-px;
    }
}
