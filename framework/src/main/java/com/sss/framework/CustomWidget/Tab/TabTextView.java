package com.sss.framework.CustomWidget.Tab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sss.framework.R;
import com.sss.framework.Utils.SizeUtils;


/**
 * TabTextView
 * Created by D on 2017/8/25.
 */
public class TabTextView extends View implements TabView {
    private int width;
    private int height;

    private Paint paint;
    private String text = "title";
    private float textHeight;

    /**
     * define
     */
    private int textSize;//title文字大小
    private int textColor;//title文字颜色
    private int textColorFocus;//title文字颜色
    private int padding;//title文字左右预留间距

    public TabTextView(Context context) {
        this(context, null);
    }

    public TabTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        textSize = SizeUtils.dp2px(context, 16);
        textColor = ContextCompat.getColor(context, R.color.color_text);
        textColorFocus = ContextCompat.getColor(context, R.color.mainColor);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        paint.setColor(textColor);

        textHeight = getTextHeight(paint);
    }
    /**
     * 获取字体高度
     */
    public static float getTextHeight(Paint p) {
        Paint.FontMetrics fm = p.getFontMetrics();// 获取字体高度
        return (float) ((Math.ceil(fm.descent - fm.top) + 2) / 2);
    }

    public static int getTextWidth(String str, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(str, 0, str.length(), bounds);
        return bounds.width();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = width / 2f;
        float y = height / 2f + textHeight / 2f;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = getTextWidth(text, paint) + padding * 2;
        }
        height = getDefaultSize(getSuggestedMinimumWidth(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setPadding(int padding) {
        this.padding = padding;
    }

    @Override
    public void setNumber(String text, int visibility) {

    }

    @Override
    public void notifyData(boolean focus) {
        this.paint.setColor(focus ? textColorFocus : textColor);
        invalidate();
    }

    @Override
    public void onScroll(float factor) {

    }
}
