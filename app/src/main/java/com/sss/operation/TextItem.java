package com.sss.operation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sss.framework.Library.Log.LogUtils;
import com.sss.framework.Utils.BitmapUtils;
import com.sss.framework.Utils.SizeUtils;

/**
 * Created by leilei on 2018/4/18.
 */

public class TextItem extends View {
    private Bitmap leftBitmap;
    private Bitmap rightBitmap;

    private int leftBitmapWidthAndHeight = 80;
    private int rightBitmapWidthAndHeight = 80;

    private Paint textPaint;


    private int centerHeight = 0;
    private int centerWidth = 0;
    private Rect rect = new Rect();
    private String text = "测试";


    private  int leftRight_left=0;


    private OnTextItemListener onTextItemListener;

    public TextItem(Context context) {
        super(context);
        init();
    }

    public TextItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerHeight = getMeasuredHeight() / 2;
        centerWidth = getMeasuredWidth() / 2;
//        LogUtils.e("onMeasure---widthMeasureSpec:" + MeasureSpec.getSize(widthMeasureSpec) + "heightMeasureSpec:" + MeasureSpec.getSize(heightMeasureSpec));
    }

    private void init() {
        leftBitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.mipmap.ic_launcher, leftBitmapWidthAndHeight, leftBitmapWidthAndHeight);
        rightBitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.mipmap.ic_launcher, rightBitmapWidthAndHeight, rightBitmapWidthAndHeight);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(SizeUtils.sp2px(getContext(), 15));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        LogUtils.e("onLayout---changed:" + changed + "left:" + left + "top:" + top + "right:" + right + "bottom:" + bottom);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (event.getX() > 0
                        && event.getX() < leftBitmapWidthAndHeight
                        && event.getY() > 0
                        && event.getY() < leftBitmapWidthAndHeight) {
                    LogUtils.e("onClickLeftImg");
                    if (onTextItemListener != null) {
                        onTextItemListener.onClickLeftImg();
                    }
                    break;
                }

                if (event.getX() > getMeasuredWidth() - rightBitmapWidthAndHeight
                        && event.getX() < getMeasuredWidth()
                        && event.getY() > 0
                        && event.getY() < leftBitmapWidthAndHeight) {
                    LogUtils.e("onClickLeftImg");
                    if (onTextItemListener != null) {
                        onTextItemListener.onClickLeftImg();
                    }
                    break;
                }

                if (event.getX() > 0 && event.getX() < leftBitmapWidthAndHeight && event.getY() > 0 && event.getY() < leftBitmapWidthAndHeight) {
                    LogUtils.e("onClickLeftImg");
                    if (onTextItemListener != null) {
                        onTextItemListener.onClickLeftImg();
                    }
                    break;
                }

        }
        return true;
    }

    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(leftBitmap, 0, getMeasuredHeight() / 2 - leftBitmapWidthAndHeight / 2, paint);
        canvas.drawBitmap(rightBitmap, getMeasuredWidth() - rightBitmapWidthAndHeight, centerHeight - rightBitmapWidthAndHeight / 2, paint);
        canvas.drawText(text, leftBitmapWidthAndHeight + 5, centerHeight + getTextHeight(textPaint) / 2, textPaint);
        super.onDraw(canvas);
    }

    /**
     * 计算文本宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public float getTextWidth(Paint paint, String str) {
        return paint.measureText(str);
    }

    /**
     * 计算文本宽度
     *
     * @param paint
     * @return
     */
    public float getTextHeight(Paint paint) {
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    public interface OnTextItemListener {
        void onClickText();

        void onClickLeftImg();

        void onClickRightImg();
    }

}
