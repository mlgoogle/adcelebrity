package com.cloudTop.starshare.ui.main.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.base.BaseFragment;
import com.cloudTop.starshare.bean.EventBusMessage;
import com.cloudTop.starshare.bean.OrderListReturnBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.activity.AddMeetTypeActivity;
import com.cloudTop.starshare.ui.main.adapter.MeetTypeAdapter;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.widget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 约见类型
 */

public class MeetingFansTypeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private NormalTitleBar ntTitle;
    //    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeLayout;
    private TextView addType;

    RecyclerView mRecyclerView;
    List<OrderListReturnBean> list = new ArrayList<>();
    MeetTypeAdapter meetTypeAdapter;
    private boolean flag = true;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_fans_meet_type;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    protected void initView() {
        initFindById();
        initAdapter();
        getData();
        initListener();
    }

    public void getData() {
        String starCode = SharePrefUtil.getInstance().getStarcode();
        NetworkAPIFactoryImpl.getDealAPI().orderList(starCode, new OnAPIListener<List<OrderListReturnBean>>() {
            @Override
            public void onError(Throwable ex) {
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<OrderListReturnBean> orderListReturnBeen) {
                if (list.size() > 0) {
                    list.clear();
                }
//                list.addAll(orderListReturnBeen);
                getHaveOrderList(orderListReturnBeen);
            }
        });
    }


    private void getHaveOrderList(final List<OrderListReturnBean> orderListReturnBeen) {
        NetworkAPIFactoryImpl.getDealAPI().haveOrderType(SharePrefUtil.getInstance().getStarcode(), new OnAPIListener<List<OrderListReturnBean>>() {
            @Override
            public void onError(Throwable ex) {

                if(ex instanceof NullPointerException&&ex.getMessage().contains("starlists")){
                    list.clear();
//                    meetTypeAdapter.loadMoreEnd();
//                    meetTypeAdapter.loadMoreFail();
                    meetTypeAdapter.setNewData(list);
                    swipeLayout.setRefreshing(false);

                }else{
                    LogUtils.loge("拥有明星类型失败----------------------");
                    meetTypeAdapter.loadMoreEnd();
                    swipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onSuccess(List<OrderListReturnBean> beanList) {
                LogUtils.loge("拥有明星类型成功----------------------");
                swipeLayout.setRefreshing(false);
                if (orderListReturnBeen == null || orderListReturnBeen.size() == 0) {
                    return;
                }
                //orderListReturnBeen:全部约见类型
                for (OrderListReturnBean orderListReturnBean : orderListReturnBeen) {
                    //beanList:当前明星拥有的约见类型
                    for (OrderListReturnBean listReturnBean : beanList) {
                        if (listReturnBean.getMid() == orderListReturnBean.getMid()) {
                            listReturnBean.setShowpic_url(orderListReturnBean.getShowpic_url());
                            listReturnBean.setPrice(orderListReturnBean.getPrice());
                        }
                    }
                }

                meetTypeAdapter.setNewData(beanList);
                if (list.size() > 0) {
                    list.clear();
//                    list.addAll(beanList);
                    list=beanList;
                }
            }
        });
    }


    private void initFindById() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        addType = (TextView) rootView.findViewById(R.id.tv_add_type);

        if (flag) {
            EventBus.getDefault().register(this); // EventBus注册广播()
            flag = false;//更改标记,使其不会再进行多次注册
        }
    }

    private void initListener() {
        addType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddMeetTypeActivity.class);
            }
        });

//        meetTypeAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtils.showShort("长按删除");
//                adapter.getData().remove(position);
//                adapter.notifyItemRemoved(position);
//                return false;
//            }
//        });

//        meetTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtils.showShort("模拟进入发布朋友圈");
//                startActivity(PublishStateActivity.class);
//            }
//        });

    }


    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        meetTypeAdapter = new MeetTypeAdapter(R.layout.item_meet_type, list);

//        meetTypeAdapter.loadMoreEnd(false);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {  //复用
//            @Override
//            public int getSpanSize(int position) {
//                return 1;
//            }
//        });
        mRecyclerView.setAdapter(meetTypeAdapter);
        mRecyclerView.setLayoutManager(manager);
    }

//    private List<WithDrawCashHistoryBean> generateData() {
//
//        List<WithDrawCashHistoryBean> beanList = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            WithDrawCashHistoryBean bean = new WithDrawCashHistoryBean();
//            bean.setBank("模拟类型:" + i);
//            beanList.add(bean);
//        }
//        return beanList;
//    }

    @Override
    public void onRefresh() {
        getData();
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void ReciveMessageEventBus(final EventBusMessage eventBusMessage) {
        switch (eventBusMessage.Message) {
            case -77:  //修改类型后
                LogUtils.loge("从修改类型界面过来------------------");
                getData();
                break;
        }
    }

}
