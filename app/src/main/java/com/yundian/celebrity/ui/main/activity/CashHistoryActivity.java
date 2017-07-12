package com.yundian.celebrity.ui.main.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.ui.main.adapter.CashHistoryAdapter;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by sll on 2017/5/24.
 */

public class CashHistoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private CashHistoryAdapter cashHistoryAdapter;
    //    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private long exitNow;

    private static final int REQUEST_COUNT = 10;
    //    private static int mCurrentCounter = 1;
    private List<WithDrawCashHistoryBean> dataList = new ArrayList<>();
//    private List<WithDrawCashHistoryBean> refreshList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_money_bag_detail;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        ntTitle.setTitleText(getResources().getString(R.string.cash_history));
        ntTitle.setTvLeftVisiable(true);

        initAdapter();


        getData(false, 1, 10);
    }


    private void getData(final boolean isLoadMore, int start, int count) {
        int status = 0;
        if (!isLoadMore) {
            cashHistoryAdapter.setEnableLoadMore(false);
        } else {
            swipeLayout.setEnabled(false);
        }
        NetworkAPIFactoryImpl.getDealAPI().cashList(status, start, count, new OnAPIListener<List<WithDrawCashHistoryBean>>() {
            @Override
            public void onError(Throwable ex) {
                swipeLayout.setRefreshing(false);
                cashHistoryAdapter.loadMoreComplete();
                LogUtils.loge("提现记录失败---------------");

            }

            @Override
            public void onSuccess(List<WithDrawCashHistoryBean> listBeans) {
                swipeLayout.setRefreshing(false);

                if (isLoadMore) {
                    if (listBeans == null || listBeans.size() == 0) {
                        cashHistoryAdapter.loadMoreEnd(true);
                        return;
                    } else {
                        cashHistoryAdapter.loadMoreEnd(false);//true is gone,false is visible  没有加载完
                    }
                    swipeLayout.setEnabled(true);

                    dataList.clear();
                    dataList = listBeans;
                    cashHistoryAdapter.addData(dataList);
                    mCurrentCounter = cashHistoryAdapter.getData().size();
                    cashHistoryAdapter.loadMoreComplete();
                } else {
                    dataList.clear();
                    dataList = listBeans;
                    showData(listBeans);

                }
            }
        });
    }


    public void showData(List<WithDrawCashHistoryBean> listBeans) {
        //下拉刷新
        cashHistoryAdapter.setNewData(listBeans);
//        isErr = false;
        mCurrentCounter = listBeans.size();
        //swipeLayout.setRefreshing(false);
        cashHistoryAdapter.setEnableLoadMore(true);
    }


    private void initAdapter() {

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cashHistoryAdapter = new CashHistoryAdapter(R.layout.adapter_cash_history_item, dataList);
        cashHistoryAdapter.setOnLoadMoreListener(this, mRecyclerView);
        cashHistoryAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        pullToRefreshAdapter.setPreLoadNumber(3);
        mRecyclerView.setAdapter(cashHistoryAdapter);
        mCurrentCounter = cashHistoryAdapter.getData().size();


        cashHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort("条目被点击+：" + position);
            }
        });
    }

    private int mCurrentCounter = 1;

    @Override
    public void onRefresh() {
        getData(false, mCurrentCounter, REQUEST_COUNT);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeLayout.setEnabled(false);

        getData(false, mCurrentCounter + 1, REQUEST_COUNT);
    }
}
