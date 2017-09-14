package com.cloudTop.starshare.ui.main.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudTop.starshare.R;
import com.cloudTop.starshare.base.BaseActivity;
import com.cloudTop.starshare.bean.BankCardBean;
import com.cloudTop.starshare.bean.BankInfoBean;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.view.RoundImageView;
import com.cloudTop.starshare.utils.FormatUtil;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.widget.NormalTitleBar;

import butterknife.Bind;


/**
 * 银行卡信息
 */
public class BankCardInfoActivity extends BaseActivity {
    @Bind(R.id.nt_title)
    NormalTitleBar ntTitle;
    @Bind(R.id.iconView)
    RoundImageView iconView;
    @Bind(R.id.titleView)
    TextView titleView;
    @Bind(R.id.typeView)
    TextView typeView;
    @Bind(R.id.numberView)
    TextView numberView;
    @Bind(R.id.rl_bank)
    RelativeLayout rlBank;
    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @Bind(R.id.bank_info_view)
    LinearLayout bankView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_card;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initData();
        requestBankCards();
        initListener();
    }
    //请求当前所拥有的银行卡
    private void requestBankCards() {
        NetworkAPIFactoryImpl.getDealAPI().bankCardList(new OnAPIListener<BankCardBean>() {
            @Override
            public void onSuccess(BankCardBean bankCardBeen) {
                srlRefresh.setRefreshing(false);
                if (TextUtils.isEmpty(bankCardBeen.getCardNo()) || TextUtils.isEmpty(bankCardBeen.getBankUsername())) {
                    LogUtils.loge("银行卡列表失败----------------------------------------------");
                } else {
                    //
                    LogUtils.loge("银行卡列表----------------成功");
                    requestBankCardInfo(bankCardBeen);
                }

            }

            @Override
            public void onError(Throwable ex) {
                srlRefresh.setRefreshing(false);
                ex.printStackTrace();
                LogUtils.loge("银行卡错误------------------------");
//                bankInfoEmpty.setVisibility(View.VISIBLE);
//                swipeLayout.setVisibility(View.GONE);
            }
        });
    }
    //根據銀行卡no，請求銀行卡的信息
    private void requestBankCardInfo(final BankCardBean bankCardBeen) {
        String cardNo = bankCardBeen.getCardNo();
        NetworkAPIFactoryImpl.getDealAPI().bankCardInfo(cardNo, new OnAPIListener<BankInfoBean>() {
            @Override
            public void onSuccess(BankInfoBean bankInfoBean) {

                if (TextUtils.isEmpty(bankInfoBean.getCardNO()) || TextUtils.isEmpty(bankInfoBean.getBankName())) {
//                    LogUtils.loge("银行卡列表失败----------------------------------------------");
                } else {
                    LogUtils.loge("银行卡信息----------------成功");
                    bankView.setVisibility(View.VISIBLE);
                    //把銀行卡的信息保存起來
                    SharePrefUtil.getInstance().saveCardNo(bankInfoBean.getCardNO());
                    titleView.setText(bankInfoBean.getBankName());
                    typeView.setText(bankInfoBean.getCardName());
                    numberView.setText(FormatUtil.formatCard(bankInfoBean.getCardNO()));

                }

            }

            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("银行卡错误------------------------");
//                ToastUtils.showShort("请先绑定银行卡");
//                startActivity(BankCardInfoActivity.class);
            }

        });
    }


    private void initData() {
        ntTitle.setTitleText(getString(R.string.bank_info));
    }

    protected void initListener() {
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bankView.setVisibility(View.GONE);
                requestBankCards();
            }
        });
    }
}
