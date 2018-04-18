package com.sss.framework.CustomWidget.Tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


import com.sss.framework.Library.Log.LogUtils;
import com.sss.framework.R;
import com.sss.framework.Utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;


/*
step1:
     <LinearLayout
        android:background="#fff"
        android:gravity="center"
        android:layout_width="match_parent"
        android:layout_height="40dp">
        <com.sss.framework.CustomWidget.Tab.ScrollTab
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:id="@+id/scrollTab"
            android:layout_width="wrap_content"
            android:layout_height="30dp"
            android:orientation="vertical"
            android:visibility="visible"
            app:stab_avag="false"
            app:stab_indicator_color="@color/mainColor"
            app:stab_indicator_padding="2dp"
            app:stab_indicator_radius="0.5dp"
            app:stab_indicator_type="trend"
            app:stab_indicator_weight="1dp"
            app:stab_indicator_width="40dp"
            app:stab_type="view_group"/>
    </LinearLayout>

                效果控制:
                app:stab_indicator_type="translation"  两端无伸缩但带平滑移动
                app:stab_indicator_type="none"  无伸缩无平滑移动

step2:
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

step3:
     String[] title = {
                "标题",
                "标题",
                "标题",
        };
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), title);
        fragmentAdapter.addFragment(XXX);
        fragmentAdapter.addFragment(XXX);
        fragmentAdapter.addFragment(XXX);
        ScrollTab.setTitles(Arrays.asList(title));
        ScrollTab.setViewPager(viewPager);
        ScrollTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                viewPager.setCurrentItem(position);
            }
        });

      viewPager.setAdapter(fragmentAdapter);
step4:
       viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ScrollTab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





















* */

/**
 * ScrollTab
 * Created by D on 2017/8/25.
 */
@SuppressWarnings("ALL")
public class ScrollTab extends HorizontalScrollView implements View.OnClickListener, ViewPager.OnPageChangeListener {
    /**
     * TAB类型
     */
    private final int TYPE_VIEW = 0;
    private final int TYPE_VIEW_GROUP = 1;

    /**
     * 指示器类型
     */
    private final int TYPE_INDICATOR_TREND = 0;//两端伸缩且带平滑移动
    private final int TYPE_INDICATOR_TRANSLATION = 1;//两端无伸缩但带平滑移动
    private final int TYPE_INDICATOR_NONE = 2;//无伸缩无平滑移动

    private int width;
    private int height;

    private Context context;
    private RectF rectF;
    private Paint paint;

    private int type;
    private boolean isAvag;
    private float padding;//item内部左右预留间距
    private String strTitles;
    private int indicatorType;
    private int indicatorColor;
    private float indicatorWidth;
    private float indicatorWeight;
    private float indicatorRadius;
    private float indicatorPadding;

    private ArrayList<TabItem> items;
    private ArrayList<View> tabs;
    private int count;
    public int position = 0;
    private float positionOffset;
    public boolean isLoad = false;
    private ViewPager viewPager;
    private OnTabListener listener;
    private int textViewWidth=0;

    public ScrollTab(Context context) {
        this(context, null);
    }

    public ScrollTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    public ScrollTab setWidth(int textViewWidth) {
        this.textViewWidth = textViewWidth;
        return this;
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollTab);
        type = typedArray.getInt(R.styleable.ScrollTab_stab_type, TYPE_VIEW);
        isAvag = typedArray.getBoolean(R.styleable.ScrollTab_stab_avag, false);
        padding = typedArray.getDimension(R.styleable.ScrollTab_stab_padding, SizeUtils.dp2px(context, 12));
        strTitles = typedArray.getString(R.styleable.ScrollTab_stab_titles);
        indicatorType = typedArray.getInt(R.styleable.ScrollTab_stab_indicator_type, TYPE_INDICATOR_TREND);
        indicatorColor = typedArray.getColor(R.styleable.ScrollTab_stab_indicator_color, ContextCompat.getColor(context, R.color.mainColor));
        indicatorWidth = typedArray.getDimension(R.styleable.ScrollTab_stab_indicator_width, SizeUtils.dp2px(context, 30));
        indicatorWeight = typedArray.getDimension(R.styleable.ScrollTab_stab_indicator_weight, SizeUtils.dp2px(context, 1));
        indicatorRadius = typedArray.getDimension(R.styleable.ScrollTab_stab_indicator_radius, SizeUtils.dp2px(context, 0.5f));
        indicatorPadding = typedArray.getDimension(R.styleable.ScrollTab_stab_indicator_padding, SizeUtils.dp2px(context, 5));
        typedArray.recycle();
    }

    private void init(Context context) {
        this.context = context;
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setFillViewport(isAvag);
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(indicatorColor);

        tabs = new ArrayList<>();
        items = new ArrayList<>();
        if (!TextUtils.isEmpty(strTitles)) {
            String[] strs = strTitles.split(";");
            for (String t : strs) {
                items.add(new TabItem(t, ""));
            }
        }
    }

    /**
     * 设置titles
     */
    public void setTitles(List<String> ts) {
        LogUtils.e(ts.size());
        if (this.items != null && ts != null) {
            this.items.clear();
            for (String t : ts) {
                this.items.add(new TabItem(t, ""));
            }
            if (isLoad == false) {
                resetTab();
                invalidate();
            }
        }
    }

    /**
     * 设置titles
     */
    public void resTitles(List<String> ts) {
        LogUtils.e(ts.size());
        if (this.items != null && ts != null) {
            this.items.clear();
            for (String t : ts) {
                this.items.add(new TabItem(t, ""));
            }
                resetTab();
                invalidate();
        }
    }

    private void resetTab() {
        if (items == null || items.size() <= 0 || width <= 0) {
            return;
        }
        isLoad = true;
        count = items.size();
        tabs.clear();
        removeAllViews();
        LinearLayout parent = new LinearLayout(context);
        LayoutParams lp = new LayoutParams(isAvag ? LayoutParams.MATCH_PARENT : LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setLayoutParams(lp);
        for (int i = 0; i < count; i++) {
            View child = getTabView(i);
            parent.addView(child);
            tabs.add(child);
        }
        addView(parent);
    }

    private View getTabView(int i) {
        View child;
        if (type == TYPE_VIEW) {
            child = new TabTextView(context);
        } else {
            child = new TabViewGroup(context);
        }
        ((TabView) child).setText(items.get(i).title);
        ((TabView) child).setNumber(items.get(i).text, TextUtils.isEmpty(items.get(i).text) ? GONE : VISIBLE);
        if (!isAvag) {
            ((TabView) child).setPadding((int) padding);
        }
        ((TabView) child).notifyData(i == position);
        if (textViewWidth==0){
            child.setLayoutParams(new LinearLayout.LayoutParams(isAvag ? width / (count > 0 ? count : 1) : ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }else {
            child.setLayoutParams(new LinearLayout.LayoutParams(isAvag ? width / (count > 0 ? count : 1) : textViewWidth,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        child.setTag(i);
        child.setOnClickListener(this);
        return child;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || count <= 0 || position < 0 || position > count - 1) {
            return;
        }
        if (indicatorType == TYPE_INDICATOR_TREND) {
            float left = tabs.get(position).getLeft() + indicatorPadding;
            float right = tabs.get(position).getRight() - indicatorPadding;
            if (position < count - 1) {
                float nextLeft = tabs.get(position + 1).getLeft() + indicatorPadding;
                float nextRight = tabs.get(position + 1).getRight() - indicatorPadding;
                if (positionOffset < 0.5) {
                    right = right + (nextRight - right) * positionOffset * 2;
                } else {
                    left = left + (nextLeft - left) * (positionOffset - 0.5f) * 2;
                    right = nextRight;
                }
            }
            rectF.set(left, height - indicatorWeight, right, height);
        } else if (indicatorType == TYPE_INDICATOR_TRANSLATION) {
            float left = tabs.get(position).getLeft();
            float right = tabs.get(position).getRight();
            float middle = left + (right - left) / 2;
            if (position < count - 1) {
                float nextLeft = tabs.get(position + 1).getLeft();
                float nextRight = tabs.get(position + 1).getRight();
                float nextMiddle = nextLeft + (nextRight - nextLeft) / 2;
                middle = middle + (nextMiddle - middle) * positionOffset;
            }
            left = middle - indicatorWidth / 2;
            right = middle + indicatorWidth / 2;
            rectF.set(left, height - indicatorWeight, right, height);
        } else {
            float left = tabs.get(position).getLeft();
            float right = tabs.get(position).getRight();
            float middle = left + (right - left) / 2;
            left = middle - indicatorWidth / 2;
            right = middle + indicatorWidth / 2;
            rectF.set(left, height - indicatorWeight, right, height);
        }
        canvas.drawRoundRect(rectF, indicatorRadius, indicatorRadius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (isLoad==false) {
            resetTab();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        if (viewPager == null) {
            position = index;
            positionOffset = 0;
            onChange(index);
            adjustScrollY(index);
        }
        if (listener != null) {
            listener.onChange(index, v);
        }
    }

    private void onChange(int position) {
        for (int i = 0; i < count; i++) {
            TabView view = (TabView) tabs.get(i);
            view.notifyData(i == position);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * 设置红点
     */
    public void setNumber(int position, String text, int visibility) {
        if (position < 0 || position > items.size() - 1) {
            return;
        }
        items.get(position).text = text;
        if (position < 0 || position > count - 1) {
            return;
        }
        TabView view = (TabView) tabs.get(position);
        view.setNumber(text, visibility);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LogUtils.d("dsiner_onPageScrolled: position: " + position + " Offset: " + positionOffset);
        if (indicatorType != TYPE_INDICATOR_NONE) {
            this.position = position;
            this.positionOffset = positionOffset;
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.d("dsiner_onPageSelected: position: " + position + " Offset: " + positionOffset);
        onChange(position);
        adjustScrollY(position);
        if (indicatorType == TYPE_INDICATOR_NONE) {
            this.position = position;
            invalidate();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        LogUtils.d("dsiner_onPageScrollStateChanged: state: " + state);
    }

    private void adjustScrollY(int position) {
        if (isAvag) {
            return;
        }
        if (tabs.size() > 0) {
            View v = tabs.get(position);
            int dr = v.getRight() - (width + getScrollX());
            int dl = getScrollX() - v.getLeft();
            if (dr > 0) {
                smoothScrollBy(dr, 0);
            } else if (dl > 0) {
                smoothScrollBy(-dl, 0);
            }
        }
    }

    public interface OnTabListener {
        void onChange(int position, View v);
    }

    public void setOnTabListener(OnTabListener l) {
        this.listener = l;
    }
}
