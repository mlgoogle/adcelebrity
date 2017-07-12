package com.yundian.celebrity.ui.main.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseFragment;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import com.yundian.celebrity.ui.main.adapter.CashHistoryAdapter;
import com.yundian.celebrity.ui.main.adapter.FansTalkAdapter;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;


/**
 * 粉丝聊天
 */

public class FansTalkFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

//    private LRecyclerView lrv;
//    private static final int REQUEST_COUNT = 10;
//    private CommentExpandAdapter mDataAdapter = null;
//    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
//    private static int mCurrentCounter = 1;
//    private List<BookingStarListBean> list = new ArrayList<>();
//    private List<BookingStarListBean> loadList = new ArrayList<>();
    private NormalTitleBar ntTitle;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeLayout;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_fans_talk;
    }

    @Override
    public void initPresenter() {
    }


    private FansTalkAdapter fansTalkAdapter;
    private long exitNow;
    private static final int REQUEST_COUNT = 10;
    private List<WithDrawCashHistoryBean> dataList = new ArrayList<>();


    @Override
    protected void initView() {
        initFindById();
//        initNewAdapter();
//        getData(false, 1, REQUEST_COUNT);
        initAdapter();
//        getData(false, 1, 10);
    }

    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fansTalkAdapter = new FansTalkAdapter(R.layout.adapter_fans_talk_item, dataList);
        fansTalkAdapter.setOnLoadMoreListener(this, mRecyclerView);
        fansTalkAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setAdapter(fansTalkAdapter);
        mCurrentCounter = fansTalkAdapter.getData().size();
        fansTalkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort("条目被点击+：" + position);
            }
        });
    }

    private void initFindById() {
        ntTitle = (NormalTitleBar) rootView.findViewById(R.id.nt_title);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
    }

    private int mCurrentCounter = 1;

    @Override
    public void onRefresh() {
//        getData(false, mCurrentCounter, REQUEST_COUNT);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeLayout.setEnabled(false);

//        getData(false, mCurrentCounter + 1, REQUEST_COUNT);
    }

//    private void initNewAdapter() {
//        mDataAdapter = new CommentExpandAdapter(getActivity(), lrv);
//        mDataAdapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
//        lrv.setAdapter(mLRecyclerViewAdapter);
//
//        lrv.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        lrv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        lrv.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
//
//        lrv.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mCurrentCounter = 0;
//                getData(false, 1, REQUEST_COUNT);  //下拉刷新
//            }
//        });
//
//        lrv.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                getData(true, mCurrentCounter + 1, REQUEST_COUNT);
//            }
//        });
//        lrv.refresh();
//    }
//
//
//    private void getData(final boolean isLoadMore, int start, int count) {
//        NetworkAPIFactoryImpl.getDealAPI().bookingStarList(start, count, new OnAPIListener<List<BookingStarListBean>>() {
//            @Override
//            public void onError(Throwable ex) {
//                LogUtils.logd("预约明星列表错误----------");
//                if (lrv != null) {
//                    lrv.setNoMore(true);
//                }
//            }
//
//            @Override
//            public void onSuccess(List<BookingStarListBean> bookingStarList) {
//                LogUtils.logd("预约明星列表成功----------");
//
//                if (bookingStarList == null || bookingStarList.size() == 0) {
//                    lrv.setNoMore(false);
//                    return;
//                }
//                if (isLoadMore) {
//                    loadList.clear();
//                    loadList = bookingStarList;
//                    loadMoreData();
//                } else {
//                    list.clear();
//                    list = bookingStarList;
//                    showData();
//                }
//            }
//        });
//    }
//    public void showData() {
//        if (lrv == null ||list.size() == 0 ) {
//            return;
//        }
//        mDataAdapter.setItems(getSampleItems());
//        mCurrentCounter = list.size();
//        lrv.refreshComplete(REQUEST_COUNT);
//        mLRecyclerViewAdapter.notifyDataSetChanged();
//    }
//
//    private void loadMoreData() {
//        if (loadList == null || list.size() == 0) {
//            lrv.setNoMore(true);
//        } else {
//            list.addAll(loadList);
//            mDataAdapter.setItems(loadList);
//            mCurrentCounter += loadList.size();
//            lrv.refreshComplete(REQUEST_COUNT);
//        }
//    }
//
//    public List<BookingStarListBean> getSampleItems() {
//        List<BookingStarListBean> items = new ArrayList<>();
//        BookingStarListBean bean1;
//        BookingStarListBean bean2;
//        for (int i = 0; i < list.size(); i++) {
//            list.get(i).setItemType(TYPE_HEADER);
//            items.add(list.get(i));
//
//            bean1 = new BookingStarListBean();
//            bean1.setTypeTitle("与TA聊天");
//            bean1.setItemType(TYPE_PERSON);
//            items.add(bean1);
//
//            bean2 = new BookingStarListBean();
//            bean2.setTypeTitle("与TA约见");
//            bean2.setItemType(TYPE_PERSON);
//            items.add(bean2);
//        }
//        return items;
//    }
}
