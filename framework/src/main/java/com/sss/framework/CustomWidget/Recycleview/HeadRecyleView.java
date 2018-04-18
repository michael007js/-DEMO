package com.sss.framework.CustomWidget.Recycleview;

import android.content.Context;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.sss.framework.CustomWidget.Recycleview.helper.RecyclerViewHelper;


/**
 * Created by leilei on 2017/8/30.
 */

public class HeadRecyleView extends LinearLayout {
    FixedRecyclerView recyclerView;
    RecyclerViewHelper recyclerViewHelper;

    public HeadRecyleView(Context context) {
        super(context);
        init(context);
    }

    public HeadRecyleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeadRecyleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void init(Context context) {
        this.setGravity(Gravity.CENTER);
        this.setOrientation(OrientationHelper.VERTICAL);
        recyclerView = new FixedRecyclerView(context);
        recyclerView.getItemAnimator().setChangeDuration(0);
        this.addView(recyclerView);
    }

    /**
     * 自定义头部宽度(在addHeader之前调用)
     *
     * @param headWidth
     */
    public void setHeadWidth(int headWidth) {
        if (recyclerViewHelper != null) {
            recyclerViewHelper.setHeadWidth(headWidth);
        }

    }

    /**
     * 添加头部
     *
     * @param view
     * @return
     */
    public void addHead(View view) {
        if (recyclerViewHelper != null) {
            recyclerViewHelper.addHeader(view);
        }
    }

    /**
     * 自定义头部宽度(在addHeader之前调用)
     *
     */
    public void setFooterWidth(int footerWidth) {
        if (recyclerViewHelper != null) {
            recyclerViewHelper.setFooterWidth(footerWidth);
        }

    }
    /**
     * 添加脚部
     *
     * @param view
     * @return
     */
    public void addFooter(View view) {
        if (recyclerViewHelper != null) {
            recyclerViewHelper.addFooter(view);
        }
    }


    /**
     * 设置适配器
     *
     * @param adapter
     */

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerViewHelper = new RecyclerViewHelper(adapter);
        recyclerView.setAdapter(recyclerViewHelper);
    }




    /**
     * 设置布局管理器
     *
     * @param layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }


}
