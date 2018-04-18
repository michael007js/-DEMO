package com.sss.framework.Adapater;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.sss.framework.CustomWidget.ImageView.ImageViewPhotoview.PhotoView;

import java.util.List;


public class ViewPagerAdpter extends PagerAdapter {
    private List<View> mViews;

    public ViewPagerAdpter(List<View> mViews) {
        super();
        this.mViews = mViews;
    }

    public void clear() {
        mViews = null;
    }

    @Override
    public int getCount() {

        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

}

