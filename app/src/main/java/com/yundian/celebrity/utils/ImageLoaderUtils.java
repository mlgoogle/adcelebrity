package com.yundian.celebrity.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yundian.celebrity.R;
import com.yundian.celebrity.app.AppConfig;

import java.io.File;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        url = AppConfig.QI_NIU_PIC_ADRESS+url;
        LogUtils.loge("ysl_url"+url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeholder)
                .error(error).crossFade().into(imageView);
    }



    public static void display(Context context, ImageView imageView, String url) {
        url = AppConfig.QI_NIU_PIC_ADRESS+url;
        LogUtils.loge("ysl_url"+url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }

    public static void displayUrl(Context context, ImageView imageView, String url) {

        LogUtils.loge("ysl_url"+url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }

    /**
     * 小方图，专用头像
     * @param context
     * @param imageView
     * @param url
     */
    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        url = AppConfig.QI_NIU_PIC_ADRESS+url;
        LogUtils.loge("ysl_url"+url);

        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.star_default_icon)
                .error(R.drawable.star_default_icon)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    /**
     * 小圆图,专用头像
     * @param context
     * @param imageView
     * @param url
     */
    public static void displaySmallPhotoRound(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_default_head)
                .error(R.drawable.user_default_head)
                .centerCrop()
                .crossFade()
                .transform(new GlideRoundTransformUtil(context))
                .into(imageView);
    }
    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        url = AppConfig.QI_NIU_PIC_ADRESS+url;
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }
    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }
    public static void displayRound(Context context, ImageView imageView, String url) {
        url = AppConfig.QI_NIU_PIC_ADRESS+url;
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.toux2)
                .centerCrop().transform(new GlideRoundTransformUtil(context)).into(imageView);
    }
    public static void displayRound(Context context, ImageView imageView, int resId) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.toux2)
                .centerCrop().transform(new GlideRoundTransformUtil(context)).into(imageView);
    }

    public static void displayWithDefaultImg(Context context, ImageView imageView, String url,int resurce) {
//        url = AppConfig.QI_NIU_PIC_ADRESS+url;
        LogUtils.loge("ysl_url"+url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.edit_cursor)
                .error(resurce)
                .crossFade().into(imageView);
    }

    //预览图
    public static void displayWithPreviewImg(Context context, final ImageView imageView, String url,int resurce) {
//        url = AppConfig.QI_NIU_PIC_ADRESS+url;
        LogUtils.loge("ysl_url"+url);
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.edit_cursor)
                .error(resurce)
                .into(imageView);
    }

}
