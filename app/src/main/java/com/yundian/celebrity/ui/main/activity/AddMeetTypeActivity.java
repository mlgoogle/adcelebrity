package com.yundian.celebrity.ui.main.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yundian.celebrity.R;
import com.yundian.celebrity.base.BaseActivity;
import com.yundian.celebrity.bean.EventBusMessage;
import com.yundian.celebrity.bean.OrderListReturnBean;
import com.yundian.celebrity.bean.RequestResultBean;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.ui.main.adapter.AddMeetTypeAdapter;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.SharePrefUtil;
import com.yundian.celebrity.utils.ToastUtils;
import com.yundian.celebrity.widget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 约见管理-添加类型
 * Created by sll on 2017/7/11.
 */

public class AddMeetTypeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.rv_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private List<OrderListReturnBean> list = new ArrayList<>();
    private AddMeetTypeAdapter addMeetTypeAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_meet_type;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ntTitle.setTitleText(getString(R.string.meeting_manager));
        initAdapter();
        getHaveOrderList();
        initListener();
    }


    private void getHaveOrderList() {
        //获取到明星的code
        String starCode = SharePrefUtil.getInstance().getStarcode();
        NetworkAPIFactoryImpl.getDealAPI().haveOrderType(starCode, new OnAPIListener<List<OrderListReturnBean>>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("拥有明星类型失败----------------------");
            }

            @Override
            public void onSuccess(List<OrderListReturnBean> beanList) {
                LogUtils.loge("拥有明星类型成功----------------------");
                //如果當前list的size大於0，先清空一下
                if (list.size() > 0) {
                    list.clear();

                }
                //然後加進去
                list.addAll(beanList);
                getData();
            }
        });
    }


    private void getData() {
        String starCode = SharePrefUtil.getInstance().getStarcode();
        NetworkAPIFactoryImpl.getDealAPI().orderList(starCode, new OnAPIListener<List<OrderListReturnBean>>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("明星类型列表失败---------------");
                swipeLayout.setRefreshing(false);
                list.clear();
                addMeetTypeAdapter.loadMoreEnd();
            }

            @Override
            public void onSuccess(List<OrderListReturnBean> orderListReturnBeen) {
                swipeLayout.setRefreshing(false);
                if (orderListReturnBeen == null || orderListReturnBeen.size() == 0) {
                    return;
                }
                for (OrderListReturnBean orderListReturnBean : list) {
                    for (OrderListReturnBean listReturnBean : orderListReturnBeen) {
                        if (listReturnBean.getMid() == orderListReturnBean.getMid()) {
                            listReturnBean.setCheck(true);
                        }
                    }
                }
                addMeetTypeAdapter.setNewData(orderListReturnBeen);


//                if (list.size() > 0) {
//                    list.clear();
//                    list = orderListReturnBeen;
//                }
            }
        });
    }


//    @OnClick(R.id.tv_add_type_sure)
//    public void onViewClicked() {
//        ArrayList<OrderListReturnBean> selectedItem = addMeetTypeAdapter.getSelectedItem();
//        LogUtils.loge("--------------size:" + selectedItem.size());
//    }

    //初始化recycleview
    private void initAdapter() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        addMeetTypeAdapter = new AddMeetTypeAdapter(R.layout.item_add_meet_type, list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(addMeetTypeAdapter);
        mRecyclerView.setLayoutManager(manager);
        addMeetTypeAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) mRecyclerView.getParent());
//        addMeetTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (addMeetTypeAdapter.isItemChecked(position)) {
//                    addMeetTypeAdapter.setItemChecked(position, false);
//                } else {
//                    addMeetTypeAdapter.setItemChecked(position, true);
//                }
//                addMeetTypeAdapter.notifyItemChanged(position);
//            }
//        });
    }

    private void initListener() {
        addMeetTypeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort("选中了checkBox");
                CheckBox checkBox = (CheckBox) view;
                LogUtils.loge("当前选中的状态:" + checkBox.isChecked());

                int mid = addMeetTypeAdapter.getData().get(position).getMid();
                int type = 1;  //  1新增,0删除
                if (checkBox.isChecked()) {
                    type = 1;
                } else {
                    type = 0;
                }
                updateMeetType(mid, type);
            }
        });
    }

    private void updateMeetType(int mid, int type) {
        String starCode = SharePrefUtil.getInstance().getStarcode();
        NetworkAPIFactoryImpl.getDealAPI().updateOrderType(starCode, mid, type, new OnAPIListener<RequestResultBean>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("修改类型失败");
            }

            @Override
            public void onSuccess(RequestResultBean requestResultBean) {
                if (requestResultBean.getResult() == 1){
                    LogUtils.loge("修改类型成功");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new EventBusMessage(-77));
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
