package com.sss.framework.CustomWidget.ViewPager.AnimationViewPager;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.sss.framework.CustomWidget.PhotoDraweeView.PhotoDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * Created by leilei on 2018/2/2.
 */

public class AnimationViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> list;
    private AnimationViewPager animationViewPager;
    private List<PhotoDraweeView> photoDraweeViews=new ArrayList<>();

    public void clear(){
        context=null;
        if (list!=null){
            list.clear();
        }
        list=null;animationViewPager= null;
        if (photoDraweeViews!=null){
            photoDraweeViews.clear();
        }
        photoDraweeViews=null;
    }

    public AnimationViewPagerAdapter(Context context, List<String> list, AnimationViewPager animationViewPager) {
        this.context = context;
        this.list = list;
        this.animationViewPager = animationViewPager;
    }

    @Override
    public int getCount() {
        // TODO 自动生成的方法存根
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == obj;
        } else {
            return view == obj;
        }
    }

    /* 如果viewpager中的view超过3个就需要重写这个方法*/
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        PhotoDraweeView  photoDraweeView = new PhotoDraweeView(context);
        photoDraweeViews.add(photoDraweeView);
        photoDraweeView.setZoomTransitionDuration(500);//设置 动画持续时间
        photoDraweeView.setScale(3f);// 获取/设置 最大缩放倍数
        container.addView(photoDraweeView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

        //这句话必须要写，按不同的位置添加视图
        if (list.get(position).startsWith("/storage/")) {
            photoDraweeView.setPhotoUri(Uri.fromFile(new File(list.get(position))));
        } else {
            photoDraweeView.setPhotoUri(Uri.parse(list.get(position)));

        }
        animationViewPager.setObjectForPosition(photoDraweeView, position);
        return photoDraweeView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView(animationViewPager.findViewFromObject(position));
    }
}
