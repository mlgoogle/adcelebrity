package com.yundian.celebrity.ui.main.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import com.yundian.celebrity.ui.main.adapter.AddMeetTypeAdapter;
import com.yundian.celebrity.ui.main.adapter.MeetTypeAdapter;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by sll on 2017/7/11.
 */

public class AddMeetTypeActivity extends BaseActivity {
    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_meet_type;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        initData();

    }


    @OnClick(R.id.tv_add_type_sure)
    public void onViewClicked() {
        ArrayList<WithDrawCashHistoryBean> selectedItem = addMeetTypeAdapter.getSelectedItem();
       LogUtils.loge("--------------size:"+selectedItem.size());
    }

//    RecyclerView mRecyclerView;
    List<WithDrawCashHistoryBean> list;
    AddMeetTypeAdapter addMeetTypeAdapter;
    private void initData() {
        list = generateData();
//        adapter = new ExpandableItemAdapter(list);
        addMeetTypeAdapter = new AddMeetTypeAdapter(R.layout.item_add_meet_type,list);
         LinearLayoutManager manager = new LinearLayoutManager(this);

        mRecyclerView.setAdapter(addMeetTypeAdapter);
        // important! setLayoutManager should be called after setAdapter
        mRecyclerView.setLayoutManager(manager);
//        addMeetTypeAdapter.expandAll();


        addMeetTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (addMeetTypeAdapter.isItemChecked(position)) {
                    addMeetTypeAdapter.setItemChecked(position, false);
                } else {
                    addMeetTypeAdapter.setItemChecked(position, true);
                }
                addMeetTypeAdapter.notifyItemChanged(position);
            }
        });

    }

    private List<WithDrawCashHistoryBean> generateData() {

        List<WithDrawCashHistoryBean> beanList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            WithDrawCashHistoryBean bean = new WithDrawCashHistoryBean();
            bean.setBank("模拟类型:"+i);
            beanList.add(bean);
        }
        return beanList;
    }

}
