package com.yundian.celebrity.ui.main.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseFragment;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.ui.main.adapter.FansTalkAdapter;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;


/**
 * 粉丝聊天
 */

public class FansTalkFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private NormalTitleBar ntTitle;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeLayout;

    private FansTalkAdapter fansTalkAdapter;
    private List<WithDrawCashHistoryBean> dataList = new ArrayList<>();
    private int mCurrentCounter = 1;
    private static final int REQUEST_COUNT = 10;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_fans_talk;
    }

    @Override
    public void initPresenter() {
    }



    @Override
    protected void initView() {
        initFindById();
        initAdapter();
        getData(false, 1, 10);
    }

    private void initFindById() {
        ntTitle = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
    }

    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fansTalkAdapter = new FansTalkAdapter(R.layout.adapter_fans_talk_item, dataList);
        fansTalkAdapter.setOnLoadMoreListener(this, mRecyclerView);
        fansTalkAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setAdapter(fansTalkAdapter);
        mCurrentCounter = fansTalkAdapter.getData().size();
        fansTalkAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) mRecyclerView.getParent());
        fansTalkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort("条目被点击+：" + position);
            }
        });
    }

    @Override
    public void onRefresh() {
        fansTalkAdapter.setEnableLoadMore(false);
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
        NetworkAPIFactoryImpl.getDealAPI().cashList(status, start, count, new OnAPIListener<List<WithDrawCashHistoryBean>>() {
            @Override
            public void onSuccess(List<WithDrawCashHistoryBean> listBeans) {
                if (listBeans == null || listBeans.size() == 0) {
                    fansTalkAdapter.loadMoreEnd(true);  //显示"没有更多数据"
                    return;
                }
                if (isLoadMore) {   //上拉加载--成功获取数据
                    fansTalkAdapter.addData(listBeans);
                    mCurrentCounter = fansTalkAdapter.getData().size();
                    fansTalkAdapter.loadMoreComplete();
                } else {  //下拉刷新  成功获取数据
                    fansTalkAdapter.setNewData(listBeans);
                    mCurrentCounter = listBeans.size();
                    swipeLayout.setRefreshing(false);
                    fansTalkAdapter.setEnableLoadMore(true);
                }
            }

            @Override
            public void onError(Throwable ex) {
                if (isLoadMore) {
                    fansTalkAdapter.loadMoreEnd();
                } else {
                    swipeLayout.setRefreshing(false);  //下拉刷新,应该显示空白页
                    fansTalkAdapter.setEnableLoadMore(true);
                }
                LogUtils.loge("提现记录失败---------------");
            }
        });
    }

}
