package com.yundian.celebrity.ui.main.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.bean.MoneyDetailListBean;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.ui.main.adapter.TestAdapter;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by sll on 2017/5/24.
 */

public class CashHistoryActivity_Test extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private TestAdapter testAdapter;
    private long exitNow;
    private static final int REQUEST_COUNT = 10;
    private List<MoneyDetailListBean> dataList = new ArrayList<>();
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

        initAdapter();

        getData(false, 1, 10);
    }

    private void initAdapter() {

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        testAdapter = new TestAdapter(R.layout.adapter_cash_history_item, dataList);
        testAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(testAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCurrentCounter = testAdapter.getData().size();

        testAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) mRecyclerView.getParent());

        testAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort("条目被点击+：" + position);
            }
        });
    }

    @Override
    public void onRefresh() {
        testAdapter.setEnableLoadMore(false);
        getData(false, 1, REQUEST_COUNT);
    }

    @Override
    public void onLoadMoreRequested() {
        LogUtils.loge("上拉加载-------");
        swipeLayout.setEnabled(false);
        getData(true, mCurrentCounter + 1, REQUEST_COUNT);
        swipeLayout.setEnabled(true);
    }

    private void getData(final boolean isLoadMore, int start, int count) {
        String time = "";
        NetworkAPIFactoryImpl.getDealAPI().moneyList(time, 0, count, start, new OnAPIListener<List<MoneyDetailListBean>>() {
            @Override
            public void onSuccess(List<MoneyDetailListBean> listBeans) {
                if (listBeans == null || listBeans.size() == 0) {
                    testAdapter.loadMoreEnd();  //没有更多数据     显示"没有更多数据"
//                        if (isLoadMore){
//                            cashHistoryAdapter.loadMoreEnd(true);  //没有更多数据     显示"没有更多数据"
//                        }else{
//                            //下拉刷新   没有数据   空白图
//                        }
                    LogUtils.loge("-------------显示没有更多数据");
                    return;
                }
                //有数据了
                if (isLoadMore) {   //上拉加载--成功获取数据
                    testAdapter.addData(listBeans);
                    mCurrentCounter = testAdapter.getData().size();
                    testAdapter.loadMoreComplete();
                } else {  //下拉刷新  成功获取数据
                    testAdapter.setNewData(listBeans);
                    mCurrentCounter = listBeans.size();
                    swipeLayout.setRefreshing(false);
                    testAdapter.setEnableLoadMore(true);
                }

            }

            @Override
            public void onError(Throwable ex) {
                if (isLoadMore) {
                    testAdapter.loadMoreEnd();
                    LogUtils.loge("isLoadMore-------------------没有数据");
                } else {
                    swipeLayout.setRefreshing(false);  //下拉刷新,应该显示空白页
                    LogUtils.loge("setRefreshing-------------------没有数据");
                    testAdapter.setEnableLoadMore(true);
                }
                LogUtils.loge("提现记录失败---------------");
            }
        });
    }
}
