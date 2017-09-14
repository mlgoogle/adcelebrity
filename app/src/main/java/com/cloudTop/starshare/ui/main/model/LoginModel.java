package com.cloudTop.starshare.ui.main.model;


import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.cloudTop.starshare.R;
import com.cloudTop.starshare.bean.LoginReturnInfo;
import com.cloudTop.starshare.bean.RegisterReturnWangYiBeen;
import com.cloudTop.starshare.listener.IDataRequestListener;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;
import com.cloudTop.starshare.networkapi.UserAPI;
import com.cloudTop.starshare.ui.wangyi.DemoCache;
import com.cloudTop.starshare.ui.wangyi.config.preference.Preferences;
import com.cloudTop.starshare.utils.LogUtils;
import com.cloudTop.starshare.utils.MD5Util;
import com.cloudTop.starshare.utils.SharePrefUtil;
import com.cloudTop.starshare.utils.ToastUtils;

public class LoginModel {


	public LoginModel(){
		//
	}
//	Looper myLooper=Looper.getMainLooper();



	public void login(final String userName, String password,UserAPI userAPI, final IDataRequestListener listener){


		userAPI
				.login(userName, MD5Util.MD5(password), new OnAPIListener<LoginReturnInfo>() {
			@Override
			public void onError(Throwable ex) {
				requestError(listener);
				LogUtils.logd("登录失败_____" + ex.toString());
				ToastUtils.showShort("登录失败");

			}

			@Override
			public void onSuccess(final LoginReturnInfo loginReturnInfo) {

				if (loginReturnInfo.getResult() == -301) {
					ToastUtils.showShort("用户不存在,请先注册");
					requestError(listener);
					return;
				} else if (loginReturnInfo.getResult() == -302) {
					ToastUtils.showShort("账号或密码错误");
					requestError(listener);
					return;
				} else if (loginReturnInfo.getResult() == -303) {
					ToastUtils.showShort("登录信息失效，请重新登录");
					requestError(listener);
					return;
				} else if (loginReturnInfo.getResult() == -304) {
					ToastUtils.showShort("用户已存在");
					requestError(listener);
					return;
				} else if (loginReturnInfo != null && loginReturnInfo.getUserinfo() != null&&loginReturnInfo.getUserinfo().getStarcode()!=null) {
//					requestServer(listener);

					LogUtils.logd("登录成功" + loginReturnInfo.toString());
					wangyiRegister(userName,loginReturnInfo,listener);
				}

			}
		});

		LogUtils.logi("");
	}


	public void wangyiRegister(String userName, final LoginReturnInfo loginReturnInfo, final IDataRequestListener listener){
		//网易云注册   usertype  : 0普通用户 1,明星
		NetworkAPIFactoryImpl.getUserAPI().registerWangYi(0, userName, userName, loginReturnInfo.getUserinfo().getId(), new OnAPIListener<RegisterReturnWangYiBeen>() {
			@Override
			public void onError(Throwable ex) {
				requestError(listener);
				LogUtils.logd("网易云注册失败" + ex.toString());
				ToastUtils.showShort("网易云注册失败");
			}

			@Override
			public void onSuccess(RegisterReturnWangYiBeen registerReturnWangYiBeen) {

				LogUtils.logd("网易云注册成功" + registerReturnWangYiBeen.getResult_value() + "网易云token" + registerReturnWangYiBeen.getToken_value());
				loginWangYi(loginReturnInfo, registerReturnWangYiBeen,listener);
			}
		});
	}


	private AbortableFuture<LoginInfo> loginRequest;
	private void loginWangYi(final LoginReturnInfo loginReturnInfos, RegisterReturnWangYiBeen registerReturnWangYiBeen ,final IDataRequestListener listener) {
		LogUtils.logd(loginReturnInfos.getUserinfo().getPhone() + "..." + registerReturnWangYiBeen.getToken_value());
		// 登录
		loginRequest = NimUIKit.doLogin(new LoginInfo(loginReturnInfos.getUserinfo().getPhone(), registerReturnWangYiBeen.getToken_value()), new RequestCallback<LoginInfo>() {
			@Override
			public void onSuccess(LoginInfo param) {
				saveDevice(loginReturnInfos,param,listener);
			}

			@Override
			public void onFailed(int code) {
				requestError(listener);
				if (code == 302 || code == 404) {
					LogUtils.logd("网易云登录失败" + R.string.login_failed);
				} else {
					LogUtils.logd("网易云登录失败" + code);
				}
			}

			@Override
			public void onException(Throwable exception) {
				LogUtils.logd("网易云登录失败" + R.string.login_exception);
			}
		});
	}

	public void saveDevice(LoginReturnInfo loginReturnInfos,LoginInfo param,final IDataRequestListener listener){
		NetworkAPIFactoryImpl.getUserAPI().saveDevice(loginReturnInfos.getUserinfo().getId(), new OnAPIListener<Object>() {
			@Override
			public void onError(Throwable ex) {
				LogUtils.logd("上传设备id和类型失败:" + ex.toString());
			}

			@Override
			public void onSuccess(Object o) {
				LogUtils.logd("上传设备id和类型成功:" + o.toString());
			}
		});
		LogUtils.logd("网易云登录成功:" + param.toString());
		ToastUtils.showStatusView("登录成功", true);

		saveLoginInfo(param.getAccount(), param.getToken());
		DemoCache.setAccount(param.getAccount());
		// 构建缓存
		DataCacheManager.buildDataCacheAsync();
		SharePrefUtil.getInstance().saveLoginUserInfo(loginReturnInfos);
		SharePrefUtil.getInstance().putTokenTime(loginReturnInfos.getToken_time());
		SharePrefUtil.getInstance().putLoginPhone(loginReturnInfos.getUserinfo().getPhone());
		requestServer(listener);

	}

	private void saveLoginInfo(final String account, final String token) {
		Preferences.saveUserAccount(account);
		Preferences.saveUserToken(token);
	}
//	public void loadData(final IDataRequestListener listener){
//		requestServer(listener);
//	}
	
//	public void deleteCircle( final IDataRequestListener listener) {
//		requestServer(listener);
//	}

//	public void addFavort(String symbol, long circle_id, int uid, final IDataRequestListener listener) {
//		NetworkAPIFactoryImpl.getDealAPI().getPraisestar(symbol, circle_id, uid, new OnAPIListener<ResultBeen>() {
//			@Override
//			public void onError(Throwable ex) {
//
//			}
//
//			@Override
//			public void onSuccess(ResultBeen resultBeen) {
//				if (resultBeen.getResult()==1){
//					requestServer(listener);
//				}
//			}
//		});
//
//	}


//
//	public void addComment(String content, CommentConfig config, final IDataRequestListener listener) {
////		int type = -1 ;
////		if (config.commentType==CommentConfig.Type.PUBLIC){
////			type = 0;
////		}else if (config.commentType==CommentConfig.Type.REPLY){
////			type = 2 ;
////		}else{
////			type = 1;
////		}
//		int type = 1;  //明星回复用户的评论
//		NetworkAPIFactoryImpl.getDealAPI().starCommentUid(config.symbol_code, config.Circle_id,
//				config.uid, type, content, new OnAPIListener<ResultBeen>() {
//					@Override
//					public void onError(Throwable ex) {
////						requestError(listener);
//					}
//
//					@Override
//					public void onSuccess(ResultBeen resultBeen) {
//						if (resultBeen.getResult()==1){
//							requestServer(listener);
//						}
//					}
//				});
//
//	}



	/**
	 * 
	* @Title: requestServer 
	* @Description: 与后台交互, 因为demo是本地数据，不做处理
	* @param  listener    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void requestServer(final IDataRequestListener listener) {
				listener.loadSuccess(1);
	}

	public void requestError(final IDataRequestListener listener) {
				listener.loadFail(1);
	}
}
