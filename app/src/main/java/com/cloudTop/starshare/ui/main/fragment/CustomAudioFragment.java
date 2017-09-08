package com.cloudTop.starshare.ui.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.AppConfig;
import com.cloudTop.starshare.base.BaseFragment;
import com.cloudTop.starshare.bean.FansAskBean;
import com.cloudTop.starshare.event.AudioMessageEvent;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.activity.RecordAudioActivity1;
import com.cloudTop.starshare.ui.main.adapter.CustomAudioAdapter;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.widget.audioplayer.MyAudioPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 粉丝聊天
 */

public class CustomAudioFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private static CustomAudioAdapter.ImageViewDelegate imageViewDelegate;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeLayout;

    private CustomAudioAdapter customAudioAdapter;
    private List<FansAskBean> dataList = new ArrayList<>();
    public List<FansAskBean> listBeans;
    private int mCurrentCounter = 1;
    private static final int REQUEST_COUNT = 10;
    public static final String FANS_ASK_BUNDLE="FansAskBeanBundle";
    public static final String POSITION="position";
    public static final String FANS_ASK_BEAN="FansAskBean";
    private MyAudioPlayer myAudioPlayer;
//    public ImageView currentPlayIvPlay;

    IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            myAudioPlayer.stop();
            imageViewDelegate.onCompletionAnimation();


        }
    };
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

//        myAudioPlayer=new MyAudioPlayer();
//        myAudioPlayer.setOnCompleteListener(mCompletionListener);
    }


    private void initFindById() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
    }
    private int currentPlayingPosition=-1;
//    private AnimationDrawable voiceBackground;
    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        customAudioAdapter = new CustomAudioAdapter(R.layout.adapter_voice_custom, dataList, new CustomAudioAdapter.OnAdapterCallBack() {


            @Override
            public void onGoRecord(FansAskBean item,int position) {
                ToastUtils.showShort("dianjirecord");
                Intent intent = new Intent(getActivity(),RecordAudioActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(FANS_ASK_BEAN,item);
                intent.putExtra(FANS_ASK_BUNDLE,bundle);
                intent.putExtra(POSITION,position);

//                startActivityForResult(intent,120);
                startActivity(intent);
            }

            @Override
            public void onListenAnswer(FansAskBean item,int position,CustomAudioAdapter.ImageViewDelegate imageViewDelegate) {


                if(myAudioPlayer==null){
                    myAudioPlayer=new MyAudioPlayer();
                    myAudioPlayer.setOnCompleteListener(mCompletionListener);
                }

                if(myAudioPlayer!=null&&(currentPlayingPosition!=position||myAudioPlayer.isMediaPlayerStop())&&!myAudioPlayer.isMediaPlayerPrepared()){
                    if(CustomAudioFragment.imageViewDelegate!=null){
                        CustomAudioFragment.imageViewDelegate.onStopAnimation();
                    }
                    imageViewDelegate.onPlayAnimation();


                    myAudioPlayer.setDataSource(AppConfig.QI_NIU_PIC_ADRESS+item.getSanswer());

                    currentPlayingPosition = position;
                    CustomAudioFragment.imageViewDelegate=imageViewDelegate;
                }else if(myAudioPlayer!=null&&myAudioPlayer.isMediaPlayerPrepared()){
//                    if(CustomAudioFragment.imageViewDelegate!=null){
//                        CustomAudioFragment.imageViewDelegate.onStopAnimation();
//                    }
                    ToastUtils.showShort("prepared");
                }else if(myAudioPlayer!=null&&(currentPlayingPosition==position&&!myAudioPlayer.isMediaPlayerStop())){
                    imageViewDelegate.onStopAnimation();

                    myAudioPlayer.stop();
                    currentPlayingPosition=-1;
                    CustomAudioFragment.imageViewDelegate=imageViewDelegate;
                }

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

//        ArrayList<Object> objects = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            objects.add(new Object());
//        }
//        customAudioAdapter.setNewData(objects);
        String starCode = SharePrefUtil.getInstance().getStarcode();
        int atype=2;//<0-文字 1-视频 2-语音>
//        int pType=1;
        int pType=2;//全部
        NetworkAPIFactoryImpl.getDealAPI().fanAskList(starCode,start,count,atype,pType, new OnAPIListener<List<FansAskBean>>() {


            @Override
            public void onSuccess(List<FansAskBean> listBeans) {
                if (listBeans == null || listBeans.size() == 0) {
                    customAudioAdapter.loadMoreEnd(true);  //显示"没有更多数据"
                    return;
                }
                if (isLoadMore) {   //上拉加载--成功获取数据
                    customAudioAdapter.addData(listBeans);
                    mCurrentCounter = customAudioAdapter.getData().size();
                    customAudioAdapter.loadMoreComplete();
                } else {  //下拉刷新  成功获取数据
                    customAudioAdapter.setNewData(listBeans);
                    CustomAudioFragment.this.listBeans=listBeans;
                    mCurrentCounter = listBeans.size();
                    swipeLayout.setRefreshing(false);
//                    fansTalkAdapter.disableLoadMoreIfNotFullPage();

                    if(listBeans.size()<REQUEST_COUNT){
                        customAudioAdapter.setEnableLoadMore(false);
                    }else{
                        customAudioAdapter.setEnableLoadMore(true);
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                if (isLoadMore) {
                    customAudioAdapter.loadMoreEnd();
                } else {
                    swipeLayout.setRefreshing(false);  //下拉刷新,应该显示空白页
                    customAudioAdapter.setEnableLoadMore(true);
                }
                LogUtils.loge("定制语音列表失败-----------");
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
            LogUtils.loge("定制语音:onHiddenChanged-----------------------------刷新首页" + isVisible());
        } else {
            LogUtils.loge("bu可见------------------刷新");
        }
        super.onHiddenChanged(hidden);

    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReciveMessageEventBus(final AudioMessageEvent eventBusMessage) {
        if(listBeans!=null){
            listBeans.get(eventBusMessage.getPosition()).setAnswer_t(-1);
                    listBeans.get(eventBusMessage.getPosition()).setSanswer(eventBusMessage.getUrl());
            customAudioAdapter.notifyDataSetChanged();
                }

//        switch (eventBusMessage.position) {
//            case -71:
//               ToastUtils.showShort("收到消息");
//                if(listBeans!=null){
//                    listBeans.
//                }
//
//                customAudioAdapter.notifyDataSetChanged();
//                break;
//        }
    }
}
