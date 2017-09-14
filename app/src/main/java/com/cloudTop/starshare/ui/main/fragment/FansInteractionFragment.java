package com.cloudTop.starshare.ui.main.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.app.AppConfig;
import com.cloudTop.starshare.app.CommentConfig;
import com.cloudTop.starshare.base.BaseFragment;
import com.cloudTop.starshare.bean.CircleFriendBean;
import com.cloudTop.starshare.bean.EventBusMessage;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.ui.main.adapter.CircleFriendAdapter;
import com.cloudTop.starshare.ui.main.contract.CircleContract;
import com.cloudTop.starshare.ui.main.presenter.CirclePresenter;
import com.cloudTop.starshare.utils.ImageLoaderUtils;
import com.cloudTop.starshare.utils.KeyBordUtil;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.ToastUtils;
import com.cloudTop.starshare.widget.ZoomImageView;
import com.cloudTop.starshare.widget.emoji.EmotionKeyboard;
import com.cloudTop.starshare.widget.emoji.EmotionLayout;
import com.cloudTop.starshare.widget.emoji.IEmotionExtClickListener;
import com.cloudTop.starshare.widget.emoji.IEmotionSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝互动
 */

public class FansInteractionFragment extends BaseFragment implements CircleContract.View, IEmotionSelectedListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView lrv;
    private FrameLayout fl_pr;
    private LinearLayout emoji_include;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_fans_interaction;
    }

    //
    private CirclePresenter presenter;
    private CircleFriendAdapter circleFriendAdapter;

    private int selectCommentItemOffset;
    private LinearLayoutManager layoutManager;
    private CommentConfig commentConfig;
    private int selectCircleItemH;
    private static int mCurrentCounter = 0;
    private static final int REQUEST_COUNT = 10;
    private ArrayList<CircleFriendBean.CircleListBean> list = new ArrayList<>();
    private ArrayList<CircleFriendBean.CircleListBean> loadList = new ArrayList<>();
    private LinearLayout mLlContent;
    private LinearLayout ll_inputt;
    private EditText mEtContent;
    private ImageView mIvEmo;
    private Button mBtnSend;
    private FrameLayout mFlEmotionView;
    //    private FrameLayout flEmotionView;
    private EmotionLayout mElEmotion;
    private EmotionKeyboard mEmotionKeyboard;
    private boolean flag = true;


    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        //第一步，new 一个presenter
        presenter = new CirclePresenter(this);
        initEmoji();
        initAdapter();
        initListener();
        getData(false, 0, REQUEST_COUNT);
    }

    private void initEmoji() {
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        emoji_include = (LinearLayout) rootView.findViewById(R.id.emoji_include);
        lrv = (RecyclerView) rootView.findViewById(R.id.lrv);
        fl_pr = (FrameLayout) rootView.findViewById(R.id.fl_pr);
        mLlContent = (LinearLayout) rootView.findViewById(R.id.llContent);
        ll_inputt = (LinearLayout) rootView.findViewById(R.id.ll_inputt);
        mEtContent = (EditText) rootView.findViewById(R.id.etContent);
        mIvEmo = (ImageView) rootView.findViewById(R.id.ivEmo);
        mBtnSend = (Button) rootView.findViewById(R.id.btnSend);
        mFlEmotionView = (FrameLayout) rootView.findViewById(R.id.flEmotionView);
        mElEmotion = (EmotionLayout) rootView.findViewById(R.id.elEmotion);
        mElEmotion.attachEditText(mEtContent);
        initEmotionKeyboard();

        if (flag) {
            EventBus.getDefault().register(this); // EventBus注册广播()
            flag = false;//更改标记,使其不会再进行多次注册
        }
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void ReciveMessageEventBus(final EventBusMessage eventBusMessage) {
        switch (eventBusMessage.Message) {
            case -65:  //修改类型后
                LogUtils.loge("从修改类型界面过来------------------");
                getData(false, 0, REQUEST_COUNT);
                break;
        }
    }

    private List<CircleFriendBean.CircleListBean> dataList = new ArrayList<>();

    private void initAdapter() {

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        circleFriendAdapter = new CircleFriendAdapter(R.layout.adapter_circle_item, dataList);
        circleFriendAdapter.setOnLoadMoreListener(this, lrv);
        //把presenter设置到adapter中
        circleFriendAdapter.setCirclePresenter(presenter);
        lrv.setAdapter(circleFriendAdapter);
        lrv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCurrentCounter = circleFriendAdapter.getData().size();

        circleFriendAdapter.setEmptyView(R.layout.message_search_empty_view, (ViewGroup) lrv.getParent());
        lrv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (ll_inputt.getVisibility() == View.VISIBLE) {
                    updateEditTextBodyVisible(View.GONE, null);
                }
            }
        });
    }

    @Override
    public void update2DeleteCircle(String circleId) {

    }

    @Override
    public void update2AddFavorite(int circlePosition, CircleFriendBean.CircleListBean.ApproveListBean addItem) {
        if (addItem != null) {
            CircleFriendBean.CircleListBean item = (CircleFriendBean.CircleListBean) circleFriendAdapter.getData().get(circlePosition);
            item.getApprove_list().add(addItem);
            circleFriendAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
    }

    @Override
    public void update2DeleteFavort(int circlePosition, String favortId) {

    }

    @Override
    public void update2AddComment(int circlePosition, CircleFriendBean.CircleListBean.CommentListBean addItem) {
        if (addItem != null) {
            CircleFriendBean.CircleListBean item = circleFriendAdapter.getData().get(circlePosition);
            item.getComment_list().add(addItem);
            circleFriendAdapter.notifyDataSetChanged();
            ToastUtils.showShort("评论发布成功");
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
        //清空评论文本
        updateEditTextBodyVisible(View.GONE, null);
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {

    }

    //发表评论吊起
    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
        if (View.GONE == visibility && mEtContent.getVisibility() == View.VISIBLE) {
            mEtContent.setText("");
        }
        if (View.VISIBLE == visibility) {
            ll_inputt.setVisibility(View.VISIBLE);
            mEtContent.requestFocus();
            //弹出键盘
            KeyBordUtil.showSoftInput(mEtContent.getContext(), mEtContent);
        } else if (View.GONE == visibility) {
            if (mFlEmotionView.getVisibility() == View.VISIBLE) {
                mFlEmotionView.setVisibility(View.GONE);
            }
            //隐藏键盘
            KeyBordUtil.hideSoftInput(mEtContent.getContext(), mEtContent);
            ll_inputt.setVisibility(View.GONE);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void update2loadData(int loadType, List<CircleFriendBean.CircleListBean> datas) {
        //具体的方法
    }


    public void getData(final boolean isLoadMore, int start, int count) {


        NetworkAPIFactoryImpl.getDealAPI().getAllCircleInfo(start, count, new OnAPIListener<CircleFriendBean>() {
            @Override
            public void onError(Throwable ex) {
                LogUtils.loge("朋友圈请求失败------------");
                if (isLoadMore) {
                    circleFriendAdapter.loadMoreEnd();
                } else {
                    swipeLayout.setRefreshing(false);//下拉刷新,应该显示空白页
                    circleFriendAdapter.setEnableLoadMore(true);
                }
            }

            @Override
            public void onSuccess(CircleFriendBean circleFriendBean) {
                LogUtils.loge("圈子反馈" + circleFriendBean.toString());

                if (circleFriendBean == null || circleFriendBean.getCircle_list() == null || circleFriendBean.getCircle_list().size() == 0) {
                    circleFriendAdapter.loadMoreEnd();  //没有更多数据     显示"没有更多数据"
                    swipeLayout.setRefreshing(false);//下拉刷新,应该显示空白页
                    LogUtils.loge("-------------显示没有更多数据");
                    return;
                }
                //有数据了
                if (isLoadMore) {   //上拉加载--成功获取数据
                    circleFriendAdapter.addData(circleFriendBean.getCircle_list());
                    mCurrentCounter = circleFriendAdapter.getData().size();
                    circleFriendAdapter.loadMoreComplete();
                } else {  //下拉刷新  成功获取数据
                    circleFriendAdapter.setNewData(circleFriendBean.getCircle_list());
                    mCurrentCounter = circleFriendBean.getCircle_list().size();
                    swipeLayout.setRefreshing(false);
                    circleFriendAdapter.setEnableLoadMore(true);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.recycle();
        }
        super.onDestroyView();
    }

//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//    }

    private void initEmotionKeyboard() {
        mEmotionKeyboard = EmotionKeyboard.with(getActivity());
        mEmotionKeyboard.bindToEditText(mEtContent);
        mEmotionKeyboard.bindToContent(mLlContent);
        mEmotionKeyboard.setEmotionLayout(mFlEmotionView);
        mEmotionKeyboard.bindToEmotionButton(mIvEmo);
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(new EmotionKeyboard.OnEmotionButtonOnClickListener() {
            @Override
            public boolean onEmotionButtonOnClickListener(View view) {
                switch (view.getId()) {
                    case R.id.ivEmo:
                        if (!mElEmotion.isShown()) {
                            /*if (mLlMore.isShown()) {
                                showEmotionLayout();
                                hideMoreLayout();
                                hideAudioButton();
                                return true;
                            }*/
                        } else if (mElEmotion.isShown()) {
                            mIvEmo.setImageResource(R.drawable.ic_cheat_emo);
                            return false;
                        }
                        showEmotionLayout();
                        break;
                }
                return false;
            }
        });

    }


    private void showEmotionLayout() {
        mElEmotion.setVisibility(View.VISIBLE);
        mIvEmo.setImageResource(R.drawable.ic_cheat_keyboard);
    }

    private void hideEmotionLayout() {
        mElEmotion.setVisibility(View.GONE);
        mIvEmo.setImageResource(R.drawable.ic_cheat_emo);
    }

    /*private void showMoreLayout() {
        mLlMore.setVisibility(View.VISIBLE);
    }

    private void hideMoreLayout() {
        mLlMore.setVisibility(View.GONE);
    }*/

    private void closeBottomAndKeyboard() {
        mElEmotion.setVisibility(View.GONE);
        //mLlMore.setVisibility(View.GONE);
        if (mEmotionKeyboard != null) {
            mEmotionKeyboard.interceptBackPress();
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (mElEmotion.isShown()) {
//            mEmotionKeyboard.interceptBackPress();
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onEmojiSelected(String key) {
        Log.e("onEmojiSelected>>>", key);
        Log.e("CSDN_LQR", "onEmojiSelected : " + key);
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
        Log.e("onStickerSelected>>>", categoryName + "..." + stickerName + "..." + stickerBitmapPath);
        ToastUtils.showShort("--:" + stickerBitmapPath);
        Log.e("CSDN_LQR", "stickerBitmapPath : " + stickerBitmapPath);
    }

    public void initListener() {
        mElEmotion.setEmotionSelectedListener(this);
        mElEmotion.setEmotionAddVisiable(false);
        mElEmotion.setEmotionSettingVisiable(false);
        mElEmotion.setEmotionExtClickListener(new IEmotionExtClickListener() {
            @Override
            public void onEmotionAddClick(View view) {
                ToastUtils.showShort("--:" + "add");
            }

            @Override
            public void onEmotionSettingClick(View view) {
                ToastUtils.showShort("--:" + "setting");
            }
        });
        /*mLlContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        closeBottomAndKeyboard();
                        break;
                }
                return false;
            }
        });*/

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    //发布评论
                    String content = mEtContent.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.showShort("评论内容不能为空...");
                        return;
                    }
                    presenter.addComment(content, commentConfig);
                }
                mEtContent.setText("");
            }
        });

        circleFriendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showPopupWindow(circleFriendAdapter.getData().get(position).getPic_url_tail());
            }
        });
    }

    private void showPopupWindow(String prc_url) {
        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_imegview, null);
        ZoomImageView zoomImageView = (ZoomImageView) popView.findViewById(R.id.zoomimage);
        final PopupWindow popupWindow = new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(popView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        ImageLoaderUtils.displayWithPreviewImg(getActivity(), zoomImageView, AppConfig.QI_NIU_PIC_ADRESS+prc_url, R.drawable.infos_news_defolat);
        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (ll_inputt != null && ll_inputt.getVisibility() == View.VISIBLE) {
//                //edittextbody.setVisibility(View.GONE);
//                updateEditTextBodyVisible(View.GONE, null);
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    @Override
    public void onRefresh() {
        circleFriendAdapter.setEnableLoadMore(false);
        getData(false, 0, REQUEST_COUNT);
    }

    @Override
    public void onLoadMoreRequested() {
        LogUtils.loge("上拉加载-------");
        swipeLayout.setEnabled(false);
        getData(true, mCurrentCounter, REQUEST_COUNT);
        swipeLayout.setEnabled(true);
    }
}
