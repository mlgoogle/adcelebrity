package com.yundian.celebrity.ui.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.app.AppConfig;
import com.yundian.celebrity.base.BaseFragment;
import com.yundian.celebrity.bean.FansAskBean;
import com.yundian.celebrity.event.VideoMessageEvent;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.ui.main.activity.PlayActivity;
import com.yundian.celebrity.ui.main.activity.RecordVideoActivity1;
import com.yundian.celebrity.ui.main.adapter.VideoAskAdapter;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.SharePrefUtil;
import com.yundian.celebrity.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频问答
 */

public class VideoAskFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeLayout;

    public static final String FANS_ASK_BUNDLE="FansAskBeanBundle";
    public static final String POSITION="position";
    public static final String DURATION="duration";
    public static final String FANS_ASK_BEAN="FansAskBean";

    private VideoAskAdapter videoAskAdapter;
    private List<FansAskBean> dataList = new ArrayList<>();
    private int mCurrentCounter = 1;
    private static final int REQUEST_COUNT = 10;
    public List<FansAskBean> listBeans;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_voice_custom;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView() {

        EventBus.getDefault().register(this);

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
        videoAskAdapter = new VideoAskAdapter(R.layout.adapter_video_custom, dataList, new VideoAskAdapter.OnAdapterCallBack() {
            @Override
            public void onGoRecordVideo(FansAskBean item,int position) {
                Intent intent = new Intent(getActivity(),RecordVideoActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(FANS_ASK_BEAN,item);
                intent.putExtra(FANS_ASK_BUNDLE,bundle);
                intent.putExtra(POSITION,position);

                if(item.getC_type()==0){
                    intent.putExtra(DURATION,15);
                }else if(item.getC_type()==1){
                    intent.putExtra(DURATION,30);
                }else if(item.getC_type()==2){
                    intent.putExtra(DURATION,45);
                }else if(item.getC_type()==3){
                    intent.putExtra(DURATION,60);
                }

                ToastUtils.showShort("dianjirecord");
                startActivity(intent);
            }

            @Override
            public void onLookAnswerVideo(FansAskBean item) {
//                video_url = fansAskBean.getVideo_url();
                //用户提问视频
                if(item.getSanswer()!=null&& !TextUtils.isEmpty(item.getSanswer())){
                    Intent intent = new Intent(getActivity(),PlayActivity.class);
                    intent.putExtra("playUrl", AppConfig.QI_NIU_PIC_ADRESS+item.getSanswer());
                    startActivity(intent);
                }else{
                    ToastUtils.showShort("视频url为空");
                }
            }

            @Override
            public void onLookAskVideo(FansAskBean item) {
                if(item.getVideo_url()!=null&& !TextUtils.isEmpty(item.getVideo_url())){
                    Intent intent = new Intent(getActivity(),PlayActivity.class);
                    intent.putExtra("playUrl",AppConfig.QI_NIU_PIC_ADRESS+item.getVideo_url());
                    startActivity(intent);
                }else{
                    ToastUtils.showShort("视频url为空");
                }
            }
        });
        videoAskAdapter.setOnLoadMoreListener(this, mRecyclerView);

        mRecyclerView.setAdapter(videoAskAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCurrentCounter = videoAskAdapter.getData().size();
        videoAskAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) mRecyclerView.getParent());

        videoAskAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Object haveStarUsersBean = videoAskAdapter.getData().get(position);
//                LogUtils.logd("Faccid"+haveStarUsersBean.getFaccid()+"");

            }
        });
//        fansTalkAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    public void onRefresh() {
        videoAskAdapter.setEnableLoadMore(false);
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
        int atype=1;//<0-文字 1-视频 2-语音>
//        int pType=1;
        int pType=2;//全部
        NetworkAPIFactoryImpl.getDealAPI().fanAskList(starCode,start,count,atype,pType, new OnAPIListener<List<FansAskBean>>() {


            @Override
            public void onSuccess(List<FansAskBean> listBeans) {
                if (listBeans == null || listBeans.size() == 0) {
                    videoAskAdapter.loadMoreEnd(true);  //显示"没有更多数据"
                    return;
                }
                if (isLoadMore) {   //上拉加载--成功获取数据
                    videoAskAdapter.addData(listBeans);
                    mCurrentCounter = videoAskAdapter.getData().size();
                    videoAskAdapter.loadMoreComplete();
                } else {  //下拉刷新  成功获取数据
                    videoAskAdapter.setNewData(listBeans);
                    VideoAskFragment.this.listBeans=listBeans;
                    mCurrentCounter = listBeans.size();
                    swipeLayout.setRefreshing(false);
//                    fansTalkAdapter.disableLoadMoreIfNotFullPage();

                    if(listBeans.size()<REQUEST_COUNT){
                        videoAskAdapter.setEnableLoadMore(false);
                    }else{
                        videoAskAdapter.setEnableLoadMore(true);
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                if (isLoadMore) {
                    videoAskAdapter.loadMoreEnd();
                } else {
                    swipeLayout.setRefreshing(false);  //下拉刷新,应该显示空白页
                    videoAskAdapter.setEnableLoadMore(true);
                }
                LogUtils.loge("定制语音列表失败-----------");
            }
        });


//        for (int i = 0; i < 10; i++) {
//            FansAskBean e = new FansAskBean();
//            e.setAnswer_t(0);
//            dataList.add(e);
//        }

//        String starCode = SharePrefUtil.getInstance().getStarcode();
//        NetworkAPIFactoryImpl.getDealAPI().fanAskList(start, count, new OnAPIListener<List<HaveStarUsersBean>>() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReciveMessageEventBus(final VideoMessageEvent eventBusMessage) {
        if(listBeans!=null){
            listBeans.get(eventBusMessage.getPosition()).setAnswer_t(-1);
            listBeans.get(eventBusMessage.getPosition()).setSanswer(eventBusMessage.getUrl());
            videoAskAdapter.notifyDataSetChanged();
        }

    }

}
