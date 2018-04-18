package com.sss.framework.CustomWidget.Loading;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * WIN8 风格的loading，基于http://blog.csdn.net/fly1183989782/article/details/46858813修改
 * Created by leilei on 2018/3/9.
 */
public class Windows8LoadingView extends View {

    /**
     * 小圆点的Y轴位置
     */
    private float cy;

    /**
     * 小圆点半径
     */
    private float radius;

    /**
     * 用于动画
     */
    private long startMillis = -1;
    private long lastMills = -1;

    /**
     * 插值器
     */
    private Interpolator enterInterpolator, exitInterpolator;


    /**
     * 小圆点数量
     */
    private int pointNum;

    private HandlerThread workerThread;
    private Handler workerHandler;


    private long enterDuration = 170;

    private long moveDuration = 1500;

    private long exitDuraion = 170;

    private long cycle;

    private float enterDistance;
    private float exitDistance;
    private float moveVelocity;

    private float offsetBetweenPoint;
    private List<LoadingPoint> points;
    private String pointColor = "#455A64";
    private String textColor = "#455A64";
    private String loadingText = "页面寻找中";

    private float uniformCxOffset;
    Paint paint;

    private int degree;

    public Windows8LoadingView(Context context) {
        super(context);
        init();
    }

    public Windows8LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Windows8LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        paint.setColor(Color.parseColor(textColor));

        pointNum = 5;

        enterInterpolator = new DecelerateInterpolator(1.6F);
        exitInterpolator = new AccelerateInterpolator(1.2F);

        cycle = enterDuration + moveDuration + exitDuraion + enterDuration * (pointNum - 1);

        points = new ArrayList<>(pointNum);
        for (int i = 0; i < pointNum; i++) {
            LoadingPoint point = new LoadingPoint(i);
            points.add(point);
        }

        workerThread = new HandlerThread("workerThread");
        workerThread.start();
        workerHandler = new Handler(workerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                updateDrawParams();
                postInvalidate();
                if (degree == 60) {
                    degree = 0;
                } else {
                    degree++;
                }
                Log.e("sss", degree + "");
                return true;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initSize();
    }

    private void initSize() {
//        cy = getMeasuredHeight() / 2;
//        radius = getMeasuredHeight() / 3;


        cy = getMeasuredHeight() / 2 - 100;
        radius = 30;
        enterDistance = getMeasuredWidth() * 0.625F;
        float moveDistance = getMeasuredWidth() * 0.15F;
        exitDistance = getMeasuredWidth() * 0.425F;

        moveVelocity = moveDistance / moveDuration;

        uniformCxOffset = -moveDistance * 0.5F;

        offsetBetweenPoint = moveDistance / 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < points.size(); i++) {
            points.get(i).draw(i, canvas);
        }
        canvas.drawText(loadingText + textPoint(), getMeasuredWidth() / 2 - getTextWidth(paint, loadingText) / 2, getMeasuredHeight() / 2 + getTextHeight(paint), paint);
        workerHandler.sendEmptyMessage(0);//update draw params on worker thread
    }

    private String textPoint() {
        if (degree < 15) {
            return ".";
        } else if (30 > degree && degree > 15) {
            return "..";
        } else {
            return "...";
        }
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        workerThread.quit();
    }

    private void updateDrawParams() {
        long currentMillis = System.currentTimeMillis();
        if (startMillis == -1) {
            startMillis = currentMillis;
        }

        if (lastMills == -1) {
            lastMills = currentMillis;
        } else {
            long timeDelta = currentMillis - lastMills;
            if (timeDelta < 16) {
                try {
                    Thread.sleep(16 - timeDelta);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //acquire current millis again, because this thread may sleep for not invalidating too frequently
        currentMillis = System.currentTimeMillis();

        long passMills = (currentMillis - startMillis) % cycle;

        for (int i = 0; i < points.size(); i++) {
            points.get(i).update(passMills);
        }

        lastMills = currentMillis;
    }

    private class LoadingPoint {

        private int index;
        private float translateX;
        private boolean visible;

        private float cx;
        private Paint paint;

        public LoadingPoint(int i) {
            index = i;
            translateX = i * offsetBetweenPoint;
            paint = new Paint();
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor(pointColor));
            paint.setAlpha(0);
        }

        public void update(long passMills) {
            //做时间偏移
            passMills = passMills - index * enterDuration;

            //还没有出现
            if (passMills < 0) {
                visible = false;
                return;
            }
            visible = true;

            float enterX = 0;
            float exitX = 0;

            if (passMills < enterDuration) {
                //enter
                float enterFraction = ((float) passMills) / enterDuration;
                float interpolatedEnterFraction = enterInterpolator.getInterpolation(enterFraction);
                enterX = interpolatedEnterFraction * enterDistance;

                exitX = 0;

                paint.setAlpha((int) (255 * interpolatedEnterFraction));
            } else if (passMills < enterDuration + moveDuration) {
                enterX = enterDistance;

                exitX = 0;

                paint.setAlpha(255);
            } else {
                enterX = enterDistance;

                float exitFraction = ((float) (passMills - enterDuration - moveDuration)) / exitDuraion;
                float interpolatedExitFraction = exitInterpolator.getInterpolation(exitFraction);
                exitX = interpolatedExitFraction * exitDistance;

                paint.setAlpha((int) (255 * (1 - interpolatedExitFraction)));

            }

            //move
            float moveX = passMills * moveVelocity;

            cx = enterX + moveX + exitX;
        }

        public void draw(int i, Canvas canvas) {
            if (visible) {
                canvas.save();
                //做位置偏移
                canvas.translate(translateX + uniformCxOffset - i * 60, 0);
                canvas.drawCircle(cx, cy, radius, paint);
                canvas.restore();
            }
        }
    }
}