package com.cloudTop.starshare.ui.main.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.WithDrawCashHistoryBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.adapter.CashHistoryAdapter;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.widget.NormalTitleBar;

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
    private long exitNow;
    private static final int REQUEST_COUNT = 10;
    private List<WithDrawCashHistoryBean> dataList = new ArrayList<>();
    private int mCurrentCounter = 1;

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
        //初始化recycelview
        initAdapter();
        //子线程里获取数据
        getData(false, 1, 10);
        setResult(RESULT_OK);
    }

    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        cashHistoryAdapter = new CashHistoryAdapter(R.layout.adapter_cash_history_item, dataList);
        cashHistoryAdapter.setOnLoadMoreListener(this, mRecyclerView);
        cashHistoryAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setAdapter(cashHistoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCurrentCounter = cashHistoryAdapter.getData().size();
        cashHistoryAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) mRecyclerView.getParent());

        cashHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort("条目被点击+：" + position);
            }
        });
    }

    @Override
    public void onRefresh() {
        cashHistoryAdapter.setEnableLoadMore(false);
        getData(false, 1, REQUEST_COUNT);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeLayout.setEnabled(false);
        getData(true, mCurrentCounter + 1, REQUEST_COUNT);
        swipeLayout.setEnabled(true);
    }

    private void getData(final boolean isLoadMore, int start, int count) {
        int status = 0;
//        if (!isLoadMore) {
//            cashHistoryAdapter.setEnableLoadMore(false);  //设置是否可以加载更多
//        } else {
//            swipeLayout.setEnabled(false);   //设置是否可以刷新
//        }
        NetworkAPIFactoryImpl.getDealAPI().cashList(status, start, count, new OnAPIListener<List<WithDrawCashHistoryBean>>() {
            @Override
            public void onSuccess(List<WithDrawCashHistoryBean> listBeans) {
                if (listBeans == null || listBeans.size() == 0) {
                    cashHistoryAdapter.loadMoreEnd(true);  //没有更多数据     显示"没有更多数据"
//                        if (isLoadMore){
//                            cashHistoryAdapter.loadMoreEnd(true);  //没有更多数据     显示"没有更多数据"
//                        }else{
//                            //下拉刷新   没有数据   空白图
//                        }
                    return;
                }
                //有数据了
                if (isLoadMore) {   //上拉加载--成功获取数据
                    cashHistoryAdapter.addData(listBeans);
                    mCurrentCounter = cashHistoryAdapter.getData().size();
                    cashHistoryAdapter.loadMoreComplete();
                } else {  //下拉刷新  成功获取数据
                    cashHistoryAdapter.setNewData(listBeans);
                    mCurrentCounter = listBeans.size();
                    swipeLayout.setRefreshing(false);
                    cashHistoryAdapter.setEnableLoadMore(true);
                }
            }

            @Override
            public void onError(Throwable ex) {
                if (isLoadMore) {
                    cashHistoryAdapter.loadMoreEnd();
                    LogUtils.loge("isLoadMore-------------------没有数据");
                } else {
                    swipeLayout.setRefreshing(false);  //下拉刷新,应该显示空白页
                    LogUtils.loge("setRefreshing-------------------没有数据");
                    cashHistoryAdapter.setEnableLoadMore(true);
                }
                LogUtils.loge("提现记录失败---------------");
            }
        });
    }
}