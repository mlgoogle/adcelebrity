package com.cloudTop.starshare.ui.main.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.SessionCustomization;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.base.BaseFragment;
import com.cloudTop.starshare.bean.HaveStarUsersBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.adapter.FansTalkAdapter;
import com.cloudTop.starshare.ui.wangyi.session.activity.P2PMessageActivity;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝聊天
 */

public class FansTalkFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeLayout;

    private FansTalkAdapter fansTalkAdapter;
    private List<HaveStarUsersBean> dataList = new ArrayList<>();
    private int mCurrentCounter = 1;
    private static final int REQUEST_COUNT = 10;
    private List<RecentContact> recentContacts=new ArrayList<>();


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

        getRecentContacts();
        registerObservers(true);
    }

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
//            checkunReadMsg();

//            int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
//            ToastUtils.showShort(unreadNum);

            if(recentContacts!=null&&!recentContacts.isEmpty()){
                FansTalkFragment.this.recentContacts = recentContacts;
                // TODO: 2017/8/3
                fansTalkAdapter.setRecentContacts(recentContacts);


            }
        }
    };


//    private void onRecentContactChanged(List<RecentContact> recentContacts) {
//        int index;
//        for (RecentContact r : recentContacts) {
//            index = -1;
//            for (int i = 0; i < dataList.size(); i++) {
//                if (r.getContactId().equals(dataList.get(i).getFaccid())
//                       ) {
//                    index = i;
//                    break;
//                }
//            }
//
//            if (index >= 0) {
//                dataList.remove(index);
//            }
//            dataList.add(r);
//        }
//        refreshMessages(true);
//    }

    public void getRecentContacts(){
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）
//                        ToastUtils.showShort();
                        LogUtils.loge(recents+"");
                        if(recents!=null&&!recents.isEmpty()){

                            FansTalkFragment.this.recentContacts = recents;
                            fansTalkAdapter.setRecentContacts(recentContacts);
                        }
                    }
                });
    }


    private void initFindById() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
    }

    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        fansTalkAdapter = new FansTalkAdapter(R.layout.adapter_fans_talk_item, dataList,recentContacts);
        fansTalkAdapter.setOnLoadMoreListener(this, mRecyclerView);

        mRecyclerView.setAdapter(fansTalkAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCurrentCounter = fansTalkAdapter.getData().size();
        fansTalkAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) mRecyclerView.getParent());

        fansTalkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HaveStarUsersBean haveStarUsersBean = fansTalkAdapter.getData().get(position);
                LogUtils.logd("Faccid"+haveStarUsersBean.getFaccid()+"");

                SessionCustomization customization = NimUIKit.getCommonP2PSessionCustomization();
                P2PMessageActivity.start(getActivity(), haveStarUsersBean.getFaccid(), haveStarUsersBean.getStarcode(),
                        haveStarUsersBean.getNickname(), customization, null);
            }
        });
//        fansTalkAdapter.bindToRecyclerView(mRecyclerView);
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

    public void getData(final boolean isLoadMore, int start, int count) {
        String starCode = SharePrefUtil.getInstance().getStarcode();
        NetworkAPIFactoryImpl.getDealAPI().fansList(starCode, start, count, new OnAPIListener<List<HaveStarUsersBean>>() {
            @Override
            public void onSuccess(List<HaveStarUsersBean> listBeans) {
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
//                    fansTalkAdapter.disableLoadMoreIfNotFullPage();

                    if(listBeans.size()<REQUEST_COUNT){
                        fansTalkAdapter.setEnableLoadMore(false);
                    }else{
                        fansTalkAdapter.setEnableLoadMore(true);
                    }
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
                LogUtils.loge("粉丝列表失败-----------");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(getUserVisibleHint());
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            LogUtils.loge("粉丝聊天界面:onHiddenChanged-----------------------------刷新首页" + isVisible());
        } else {
            LogUtils.loge("bu可见------------------刷新");
        }
        super.onHiddenChanged(hidden);

    }

}
