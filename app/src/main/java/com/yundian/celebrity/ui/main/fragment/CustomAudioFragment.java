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
import com.yundian.celebrity.ui.main.activity.RecordAudioActivity;
import com.yundian.celebrity.ui.main.adapter.CustomAudioAdapter;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝聊天
 */

public class CustomAudioFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeLayout;

    private CustomAudioAdapter customAudioAdapter;
    private List dataList = new ArrayList<>();
    private int mCurrentCounter = 1;
    private static final int REQUEST_COUNT = 10;



    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_voice_custom;
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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
    }

    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        customAudioAdapter = new CustomAudioAdapter(R.layout.adapter_voice_custom, dataList, new CustomAudioAdapter.OnAdapterCallBack() {
            @Override
            public void onGoRecord() {
                ToastUtils.showShort("dianjirecord");
                startActivity(RecordAudioActivity.class);

            }
        });
        customAudioAdapter.setOnLoadMoreListener(this, mRecyclerView);

        mRecyclerView.setAdapter(customAudioAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCurrentCounter = customAudioAdapter.getData().size();
        customAudioAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) mRecyclerView.getParent());

        customAudioAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Object haveStarUsersBean = customAudioAdapter.getData().get(position);
//                LogUtils.logd("Faccid"+haveStarUsersBean.getFaccid()+"");

            }
        });
//        fansTalkAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    public void onRefresh() {
        customAudioAdapter.setEnableLoadMore(false);
        getData(false, 1, REQUEST_COUNT);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeLayout.setEnabled(false);
        getData(true, mCurrentCounter + 1, REQUEST_COUNT);
        swipeLayout.setEnabled(true);
    }

    public void getData(final boolean isLoadMore, int start, int count) {

        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            objects.add(new Object());
        }
        customAudioAdapter.setNewData(objects);
//        String starCode = SharePrefUtil.getInstance().getStarcode();
//        NetworkAPIFactoryImpl.getDealAPI().fanAskList(start,count, new OnAPIListener<List<HaveStarUsersBean>>() {
//            @Override
//            public void onSuccess(List<HaveStarUsersBean> listBeans) {
//                if (listBeans == null || listBeans.size() == 0) {
//                    customAudioAdapter.loadMoreEnd(true);  //显示"没有更多数据"
//                    return;
//                }
//                if (isLoadMore) {   //上拉加载--成功获取数据
//                    customAudioAdapter.addData(listBeans);
//                    mCurrentCounter = customAudioAdapter.getData().size();
//                    customAudioAdapter.loadMoreComplete();
//                } else {  //下拉刷新  成功获取数据
//                    customAudioAdapter.setNewData(listBeans);
//                    mCurrentCounter = listBeans.size();
//                    swipeLayout.setRefreshing(false);
////                    fansTalkAdapter.disableLoadMoreIfNotFullPage();
//
//                    if(listBeans.size()<REQUEST_COUNT){
//                        customAudioAdapter.setEnableLoadMore(false);
//                    }else{
//                        customAudioAdapter.setEnableLoadMore(true);
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex) {
//                if (isLoadMore) {
//                    customAudioAdapter.loadMoreEnd();
//                } else {
//                    swipeLayout.setRefreshing(false);  //下拉刷新,应该显示空白页
//                    customAudioAdapter.setEnableLoadMore(true);
//                }
//                LogUtils.loge("定制语音列表失败-----------");
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(getUserVisibleHint());
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            LogUtils.loge("定制语音:onHiddenChanged-----------------------------刷新首页" + isVisible());
        } else {
            LogUtils.loge("bu可见------------------刷新");
        }
        super.onHiddenChanged(hidden);

    }

}
