package com.yundian.celebrity.ui.main.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yundian.celebrity.R;
import com.yundian.celebrity.bean.WithDrawCashHistoryBean;
import java.util.ArrayList;
import java.util.List;




/**
 * Created by sll on 2017/7/11.
 */

public class AddMeetTypeAdapter extends BaseQuickAdapter<WithDrawCashHistoryBean, BaseViewHolder> {
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private boolean mIsSelectable = false;


    //更新adpter的数据和选择状态
    public void updateDataSet(ArrayList<String> list) {
//        this.mList = list;
        mSelectedPositions = new SparseBooleanArray();
//            ab.setTitle("已选择" + 0 + "项");
//        Toast.makeText(MainActivity.this,"已选择" + 0 + "项",Toast.LENGTH_SHORT).show();

    }

    //获得选中条目的结果
    public ArrayList<WithDrawCashHistoryBean> getSelectedItem() {
        ArrayList<WithDrawCashHistoryBean> selectList = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            if (isItemChecked(i)) {
                selectList.add(getData().get(i));
            }
        }
        return selectList;
    }
    //设置给定位置条目的选择状态
    public void setItemChecked(int position, boolean isChecked) {
        mSelectedPositions.put(position, isChecked);
    }

    //根据位置判断条目是否选中
    public boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }

    //根据位置判断条目是否可选
    private boolean isSelectable() {
        return mIsSelectable;
    }
    //设置给定位置条目的可选与否的状态
    private void setSelectable(boolean selectable) {
        mIsSelectable = selectable;
    }

    public AddMeetTypeAdapter(@LayoutRes int layoutResId, @Nullable List<WithDrawCashHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithDrawCashHistoryBean item) {
        CheckBox checkBox = helper.getView(R.id.checkbox);
        final int layoutPosition = helper.getLayoutPosition();
        checkBox.setChecked(isItemChecked(layoutPosition));

        //checkBox的监听
  checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItemChecked(layoutPosition)) {
                    setItemChecked(layoutPosition, false);
                } else {
                    setItemChecked(layoutPosition, true);
                }
//                    ab.setTitle("已选择" + getSelectedItem().size() + "项");
//                Toast.makeText(MainActivity.this,"已选择" + getSelectedItem().size() + "项",Toast.LENGTH_SHORT).show();
            }
        });

//        //条目view的监听
//       helper.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                    ab.setTitle("已选择" + getSelectedItem().size() + "项");
//               // Toast.makeText(MainActivity.this,"已选择" + getSelectedItem().size() + "项",Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }
}
