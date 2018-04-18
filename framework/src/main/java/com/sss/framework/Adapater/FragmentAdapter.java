package com.sss.framework.Adapater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sss.framework.Library.ViewPagerAnimation.DepthPageTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.AccordionTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.BackgroundToForegroundTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.CubeInTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.CubeOutTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.FlipHorizontalTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.FlipVerticalTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.ForegroundToBackgroundTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.RotateDownTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.ScaleInOutTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.StackTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.TabletTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.ZoomInTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.ZoomOutSlideTransformer;
import com.sss.framework.Library.ViewPagerAnimation.animation.ZoomOutTranformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leilei on 2017/6/13.
 */

/**
 * Fragment适配器
 * Created by wcy on 2015/11/26.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;

    /**
     * @param fm
     * @param viewPager
     * @param animationType 0.左右黏合滑动
     *                      1.快速消失切入
     *                      2.3D向前飞出屏幕
     *                      3.3D箱子旋转
     *                      4.平移
     *                      5.卡片左右翻页
     *                      6.卡片上下翻页
     *                      7.等比放大缩小
     *                      8左右带角度平移1
     *                      9左右带角度平移2
     *                      10.好像没有写case10.
     *                      11.遮盖翻页
     *                      12.内旋3D翻页
     *                      13.不翻页中心缩小
     *                      14.左右半透明平移
     *                      15.改变透明度变换
     *                      16.左右黏贴平移
     */
    public FragmentAdapter(FragmentManager fm, ViewPager viewPager, int animationType) {
        super(fm);
        setAnimation(viewPager, animationType);
    }

    /**
     * @param fm
     * @param mTitles
     * @param viewPager
     * @param animationType 0.左右黏合滑动
     *                      1.快速消失切入
     *                      2.3D向前飞出屏幕
     *                      3.3D箱子旋转
     *                      4.平移
     *                      5.卡片左右翻页
     *                      6.卡片上下翻页
     *                      7.等比放大缩小
     *                      8左右带角度平移1
     *                      9左右带角度平移2
     *                      10.好像没有写case10.
     *                      11.遮盖翻页
     *                      12.内旋3D翻页
     *                      13.不翻页中心缩小
     *                      14.左右半透明平移
     *                      15.改变透明度变换
     *                      16.左右黏贴平移
     */
    public FragmentAdapter(FragmentManager fm, String[] mTitles, ViewPager viewPager, int animationType) {
        super(fm);
        this.mTitles = mTitles;
        setAnimation(viewPager, animationType);
    }

    public Fragment getFragment(int position) {
        if (mFragments.size() - 1 > position) {
            return mFragments.get(position);
        }
        return null;
    }


    public List<Fragment> getmFragments() {
        return mFragments;
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void clear() {
        mFragments.clear();
        mTitles = null;
    }

    public void clearFragment() {
        mFragments.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles == null) {
            return null;
        }
        return mTitles[position];
    }


    private void setAnimation(ViewPager viewPager, int animationType) {
        switch (animationType) {
            case 1:
                viewPager.setPageTransformer(true,
                        new AccordionTransformer());
                break;
            case 2:
                viewPager.setPageTransformer(true,
                        new BackgroundToForegroundTransformer());

                break;
            case 3:
                viewPager.setPageTransformer(true,
                        new CubeInTransformer());
                break;
            case 4:
                viewPager.setPageTransformer(true,
                        new CubeOutTransformer());
                break;
            case 5:
                viewPager.setPageTransformer(true,
                        new DepthPageTransformer());
                break;
            case 6:
                viewPager.setPageTransformer(true,
                        new FlipHorizontalTransformer());
                break;
            case 7:
                viewPager.setPageTransformer(true,
                        new FlipVerticalTransformer());
                break;
            case 8:
                viewPager.setPageTransformer(true,
                        new ForegroundToBackgroundTransformer());
                break;
            case 9:
                viewPager.setPageTransformer(true,
                        new RotateDownTransformer());
                break;
            case 11:
                viewPager.setPageTransformer(true,
                        new ScaleInOutTransformer());
                break;
            case 12:
                viewPager.setPageTransformer(true,
                        new StackTransformer());
                break;
            case 13:
                viewPager.setPageTransformer(true,
                        new TabletTransformer());
                break;
            case 14:
                viewPager.setPageTransformer(true,
                        new ZoomInTransformer());
                break;
            case 15:
                viewPager.setPageTransformer(true,
                        new ZoomOutSlideTransformer());
                break;
            case 16:
                viewPager.setPageTransformer(true,
                        new ZoomOutTranformer());
                break;
        }

    }
}
