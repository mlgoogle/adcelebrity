package com.yundian.celebrity.ui.main.model;


import com.yundian.celebrity.app.CommentConfig;
import com.yundian.celebrity.bean.LoginReturnInfo;
import com.yundian.celebrity.bean.RegisterReturnWangYiBeen;
import com.yundian.celebrity.bean.ResultBeen;
import com.yundian.celebrity.listener.IDataRequestListener;
import com.yundian.celebrity.listener.OnAPIListener;
import com.yundian.celebrity.networkapi.NetworkAPIFactoryImpl;
import com.yundian.celebrity.utils.LogUtils;
import com.yundian.celebrity.utils.MD5Util;
import com.yundian.celebrity.utils.ToastUtils;

import static com.yundian.celebrity.R.id.passwordEditText;
import static com.yundian.celebrity.R.id.userNameEditText;

public class LoginModel {


	public LoginModel(){
		//
	}

	public void login(String userName,String password, final IDataRequestListener listener){
		NetworkAPIFactoryImpl.getUserAPI().login(userName, MD5Util.MD5(password), new OnAPIListener<LoginReturnInfo>() {
			@Override
			public void onError(Throwable ex) {

				LogUtils.logd("登录失败_____" + ex.toString());
				ToastUtils.showShort("登录失败");
				requestError(listener);
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
				} else if (loginReturnInfo != null && loginReturnInfo.getUserinfo() != null) {
					requestServer(listener);





					LogUtils.logd("登录成功" + loginReturnInfo.toString());
					//网易云注册   usertype  : 0普通用户 1,明星
					NetworkAPIFactoryImpl.getUserAPI().registerWangYi(0, userName, userName, loginReturnInfo.getUserinfo().getId(), new OnAPIListener<RegisterReturnWangYiBeen>() {
						@Override
						public void onError(Throwable ex) {
							LogUtils.logd("网易云注册失败" + ex.toString());
							ToastUtils.showShort("网易云注册失败");
						}

						@Override
						public void onSuccess(RegisterReturnWangYiBeen registerReturnWangYiBeen) {
							requestServer(listener);
							LogUtils.logd("网易云注册成功" + registerReturnWangYiBeen.getResult_value() + "网易云token" + registerReturnWangYiBeen.getToken_value());
							loginWangYi(loginReturnInfo, registerReturnWangYiBeen);
						}
					});
				}

			}
		});
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
	private void requestServer(final IDataRequestListener listener) {
				listener.loadSuccess(1);
	}

	private void requestError(final IDataRequestListener listener) {
				listener.loadFail(1);
	}
}
