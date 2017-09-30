package com.cloudTop.starshare.ui.main.model;


import com.cloudTop.starshare.app.CommentConfig;
import com.cloudTop.starshare.bean.ResultBeen;
import com.cloudTop.starshare.listener.IDataRequestListener;
import com.cloudTop.starshare.listener.OnAPIListener;
import com.cloudTop.starshare.networkapi.NetworkAPIFactoryImpl;

public class CircleModel {
	
	
	public CircleModel(){
		//
	}

	public void loadData(final IDataRequestListener listener){
		requestServer(listener);
	}
	
	public void deleteCircle( final IDataRequestListener listener) {
		requestServer(listener);

	}

	public void addFavort(String symbol, long circle_id, int uid, final IDataRequestListener listener) {
		NetworkAPIFactoryImpl.getDealAPI().getPraisestar(symbol, circle_id, uid, new OnAPIListener<ResultBeen>() {
			@Override
			public void onError(Throwable ex) {

			}

			@Override
			public void onSuccess(ResultBeen resultBeen) {
				if (resultBeen.getResult()==1){
					requestServer(listener);
				}
			}
		});

	}

	public void deleteFavort(final IDataRequestListener listener) {
		requestServer(listener);
	}

	public void addComment(String content, CommentConfig config, final IDataRequestListener listener) {
//		int type = -1 ;
//		if (config.commentType==CommentConfig.Type.PUBLIC){
//			type = 0;
//		}else if (config.commentType==CommentConfig.Type.REPLY){
//			type = 2 ;
//		}else{
//			type = 1;
//		}
		int type = 1;  //明星回复用户的评论
		NetworkAPIFactoryImpl.getDealAPI().starCommentUid(config.symbol_code, config.Circle_id,
				config.uid, type, content, new OnAPIListener<ResultBeen>() {
					@Override
					public void onError(Throwable ex) {

					}

					@Override
					public void onSuccess(ResultBeen resultBeen) {
						if (resultBeen.getResult()==1){
							requestServer(listener);
						}
					}
				});

	}

	public void deleteComment( final IDataRequestListener listener) {
		requestServer(listener);
	}
	
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
}
