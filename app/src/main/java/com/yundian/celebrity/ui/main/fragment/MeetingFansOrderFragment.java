package com.yundian.celebrity.ui.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.app.Constant;
import com.yundian.celebrity.base.BaseFragment;
import com.yundian.celebrity.bean.EventBusMessage;
import com.yundian.celebrity.bean.MeetOrderListBean;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.ui.main.activity.MeetingFansDetailActivity;
import com.yundian.celebrity.ui.main.adapter.MeetingFansOrderAdapter;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.SharePrefUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 约见订单
 */
public class MeetingFansOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private SwipeRefreshLayout swipeLayout;
    private MeetingFansOrderAdapter meetingFansOrderAdapter;
    private static final int REQUEST_COUNT = 10;
    private List<MeetOrderListBean> dataList = new ArrayList<>();
    private int mCurrentCounter = 0;
    private RecyclerView mRecyclerView;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_meet_order;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this); // EventBus注册广播()
        initFindById();
        initAdapter();
        getData(false, 1, REQUEST_COUNT);
    }


    private void initFindById() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
    }

    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        meetingFansOrderAdapter = new MeetingFansOrderAdapter(R.layout.adapter_fans_meet_order_item, dataList);

        meetingFansOrderAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(meetingFansOrderAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCurrentCounter = meetingFansOrderAdapter.getData().size();
        meetingFansOrderAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) mRecyclerView.getParent());
        meetingFansOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MeetOrderListBean bean = meetingFansOrderAdapter.getData().get(position);
                Intent intent = new Intent(getActivity(), MeetingFansDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.MEET_TYPE_DETAIL, bean);
                intent.putExtra(Constant.MEET_TYPE, bundle);
                startActivity(intent);
            }
        });
//        meetingFansOrderAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    public void onRefresh() {
        meetingFansOrderAdapter.setEnableLoadMore(false);
        getData(false, 1, REQUEST_COUNT);
    }

    @Override
    public void onLoadMoreRequested() {
        LogUtils.loge("上拉加载-------");
        swipeLayout.setEnabled(false);
        getData(true, mCurrentCounter + 1, REQUEST_COUNT);
        swipeLayout.setEnabled(true);
    }


    public void getData(final boolean isLoadMore, int start, int count) {
        String starCode = SharePrefUtil.getInstance().getStarcode();
        NetworkAPIFactoryImpl.getDealAPI().meetOrderList(starCode, start, count, new OnAPIListener<List<MeetOrderListBean>>() {
            @Override
            public void onError(Throwable ex) {
                if (isLoadMore) {
                    meetingFansOrderAdapter.loadMoreEnd();
                } else {
                    swipeLayout.setRefreshing(false);  //下拉刷新,应该显示空白页
                    meetingFansOrderAdapter.setEnableLoadMore(true);
                }
            }

            @Override
            public void onSuccess(List<MeetOrderListBean> meetOrderListBeen) {
//                 #'1-待确认 2-已拒绝 3-已完成 4-已同意；',
                if (meetOrderListBeen == null || meetOrderListBeen.size() == 0) {
                    meetingFansOrderAdapter.loadMoreEnd();  //没有更多数据     显示"没有更多数据"
                    LogUtils.loge("-------------显示没有更多数据");
                    return;
                }
                //有数据了
                if (isLoadMore) {   //上拉加载--成功获取数据
                    meetingFansOrderAdapter.addData(meetOrderListBeen);
                    mCurrentCounter = meetingFansOrderAdapter.getData().size();
                    meetingFansOrderAdapter.loadMoreComplete();
                } else {  //下拉刷新  成功获取数据
                    meetingFansOrderAdapter.setNewData(meetOrderListBeen);
//                    meetingFansOrderAdapter.disableLoadMoreIfNotFullPage();
                    if(meetOrderListBeen.size()<REQUEST_COUNT){
                        meetingFansOrderAdapter.setEnableLoadMore(false);
                    }else{
                        meetingFansOrderAdapter.setEnableLoadMore(true);
                    }
                    mCurrentCounter = meetOrderListBeen.size();
                    swipeLayout.setRefreshing(false);

//                    meetingFansOrderAdapter.setEnableLoadMore(true);
                }
            }
        });
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void ReciveMessageEventBus(final EventBusMessage eventBusMessage) {
        LogUtils.logd("从修改类型界面过来");
        switch (eventBusMessage.Message) {
            case -76:  //修改类型后
                LogUtils.loge("从修改类型界面过来------------------");
                getData(false, 1, REQUEST_COUNT);
                break;
        }
    }
}
