package com.cloudTop.starshare.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.cloudTop.starshare.widget.photobutton.CaptureButton;

/**
 * Created by Administrator on 2017/8/29.
 */

public class AudioRecordButton extends CaptureButton{
    public AudioRecordButton(Context context) {
        super(context);
    }

    public AudioRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AudioRecordButton(Context context, int size,int bitmapRes) {
        super(context, size,bitmapRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        //外圆（半透明灰色）
        mPaint.setColor(0xEECCCCCC);
        canvas.drawCircle(center_X, center_Y, button_outside_radius, mPaint);

        //内圆（白色）
        mPaint.setColor(0xFFFFFFFF);
        canvas.drawCircle(center_X, center_Y, button_inside_radius, mPaint);

        //如果状态为按钮长按按下的状态，则绘制录制进度条
        if (state == STATE_PRESS_LONG_CLICK) {
            mPaint.setAntiAlias(true);
            mPaint.setColor(0x9900CC00);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth);
            canvas.drawArc(rectF, -90, progress, false, mPaint);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapRes);

        canvas.drawBitmap(bitmap,center_X-bitmap.getWidth()/2,center_Y-bitmap.getHeight()/2,null);
    }
}
