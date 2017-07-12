package com.yundian.celebrity.ui.main.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseFragment;

import com.yundian.celebrity.bean.WithDrawCashHistoryBean;

import com.yundian.celebrity.ui.main.activity.AddMeetTypeActivity;
import com.yundian.celebrity.ui.main.adapter.MeetTypeAdapter;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;



/**
 * 约见类型
 */

public class MeetingFansTypeFragment extends BaseFragment{
    private NormalTitleBar ntTitle;
//    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeLayout;
    private TextView addType;


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

        initData();
        initListener();

    }



    private void initFindById() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        addType = (TextView) rootView.findViewById(R.id.tv_add_type);
    }
    private void initListener() {
        addType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddMeetTypeActivity.class);
            }
        });
    }

    RecyclerView mRecyclerView;
//    ExpandableItemAdapter adapter;
    List<WithDrawCashHistoryBean> list;
    MeetTypeAdapter meetTypeAdapter;



    private void initData() {
        list = generateData();
//        adapter = new ExpandableItemAdapter(list);
        meetTypeAdapter = new MeetTypeAdapter(R.layout.item_meet_type,list);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });


        mRecyclerView.setAdapter(meetTypeAdapter);
        // important! setLayoutManager should be called after setAdapter
        mRecyclerView.setLayoutManager(manager);
        meetTypeAdapter.expandAll();

        meetTypeAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort("长按删除");
                adapter.getData().remove(position);
                adapter.notifyItemRemoved(position);
                return false;
            }
        });

    }

    private List<WithDrawCashHistoryBean>  generateData() {

        List<WithDrawCashHistoryBean> beanList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            WithDrawCashHistoryBean bean = new WithDrawCashHistoryBean();
            bean.setBank("模拟类型:"+i);
            beanList.add(bean);
        }
        return beanList;
    }
}
