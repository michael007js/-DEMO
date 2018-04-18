package com.sss.framework.CustomWidget.Loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by leilei on 2018/3/13.
 */

public class BaiduloadingView extends View {
    //控件的长、宽
    int mWidth, mHeight;
    //上下文
    Context context;
    //小球半径
    int radius;
    //三支画笔
    Paint paint, paint1, paint2;

    //文字画笔
    Paint paintText;

    int degree = 0;
    int fegree = 0;
    int segree = 0;
    int status = 0;
    private String loadingText = "页面寻找中...";
    public BaiduloadingView(Context context) {
        this(context, null);
    }

    public BaiduloadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaiduloadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();

        status = dp2px(context, 3);
    }


    private void initPaint() {

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(100);
        paint.setAntiAlias(true);
        paint.setDither(true);

        paint1 = new Paint();
        paint1.setStyle(Paint.Style.FILL);
        paint1.setColor(Color.GREEN);
        paint1.setStrokeWidth(100);
        paint1.setAntiAlias(true);
        paint1.setDither(true);

        paint2 = new Paint();
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.BLUE);
        paint2.setStrokeWidth(100);
        paint2.setAntiAlias(true);
        paint2.setDither(true);

        paintText = new Paint();
        paintText.setStyle(Paint.Style.FILL);
        paintText.setColor(Color.BLACK);
        paintText.setStrokeWidth(100);
        paintText.setAntiAlias(true);
        paintText.setDither(true);
        paintText.setTextSize(dp2px(context, 15));

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);


        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
        } else {
            mWidth = dp2px(context, 250);
        }

        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = dp2px(context, 200);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        radius = dp2px(context, 10);


        canvas.drawCircle(radius + degree, mHeight / 2, radius, paint);

        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, paint1);

        canvas.drawCircle(mWidth - radius + fegree, mHeight / 2, radius, paint2);


        degree += status;
        fegree -= status;

        if (degree <= 0) {
            status = dp2px(context, 3);
        }
        if (degree >= mWidth - 2 * radius) {
            status = -dp2px(context, 3);
        }

        if (Math.abs((mWidth - radius + fegree) - (radius + degree)) <= 10) {
            changePaintColor();
        }
        canvas.drawText(loadingText, getMeasuredWidth() / 2 - getTextWidth(paintText,loadingText) / 2, getMeasuredHeight() / 2 + getTextHeight(paint)+dp2px(context,35), paintText);
        invalidate();


    }

    /**
     * 计算文本高度
     *
     * @param paint
     * @return
     */
    public int getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) (Math.abs(fm.ascent) + Math.abs(fm.descent));
    }

    /**
     * 计算文本宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public int getTextWidth(Paint paint, String str) {
        int x = (int) paint.measureText(str);
        return x;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 更改小球的颜色
     */
    private void changePaintColor() {


        segree++;

        if (segree % 3 == 0) {

            paint.setColor(Color.GREEN);
            paint1.setColor(Color.BLUE);
            paint2.setColor(Color.RED);

        } else if (segree % 3 == 1) {

            paint.setColor(Color.BLUE);
            paint1.setColor(Color.RED);
            paint2.setColor(Color.GREEN);

        } else {

            paint.setColor(Color.RED);
            paint1.setColor(Color.GREEN);
            paint2.setColor(Color.BLUE);

        }

    }


}