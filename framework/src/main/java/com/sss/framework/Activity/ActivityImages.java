package com.sss.framework.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sss.framework.CustomWidget.Layout.LayoutLoading.LoadingLayout;
import com.sss.framework.CustomWidget.ViewPager.AnimationViewPager.AnimationViewPager;
import com.sss.framework.CustomWidget.ViewPager.AnimationViewPager.AnimationViewPagerAdapter;
import com.sss.framework.Library.Glid.GlidUtils;
import com.sss.framework.R;
import com.sss.framework.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;



/*
 if (getBaseFragmentActivityContext() != null) {
                                    startActivity(new Intent(getBaseFragmentActivityContext(), ActivityImages.class)
                                            .putStringArrayListExtra("data", (ArrayList<String>) urlList)
                                            .putExtra("current", position));
                                }
* */

/**
 *
 * Created by leilei on 2017/8/29.
 */

public class ActivityImages extends BaseActivity {
   private LinearLayout activityImage;
    private  AnimationViewPager viewpagerActivityImage;
    private  ImageView back;
    private LinearLayout click;
    private  List<String> list = new ArrayList<>();
    private AnimationViewPagerAdapter animationViewPagerAdapter;

    @Override
    protected void cacheData() {

    }

    @Override
    protected void destroy() {
        if (animationViewPagerAdapter!=null){
            animationViewPagerAdapter.clear();
        }
        animationViewPagerAdapter=null;
        if (list != null) {
            list.clear();
        }
        list = null;
        activityImage = null;
        viewpagerActivityImage = null;
        back = null;
    }

    @Override
    protected void uiHidden() {

    }

    @Override
    protected void memoryWarningYellow() {

    }

    @Override
    protected void memoryWarningOrange() {

    }

    @Override
    protected void memoryWarningRed() {

    }

    @Override
    protected void setReLoadingCallBack(LoadingLayout loading) {

    }

    @Override
    protected List<PermissionItem> permissionItems() {
        return null;
    }

    @Override
    protected PermissionCallback permissionCallback() {
        return null;
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_images);
        activityImage= (LinearLayout) findViewById(R.id.activity_image);
        viewpagerActivityImage= (AnimationViewPager) findViewById(R.id.viewpager_activity_image);
        viewpagerActivityImage.setTransitionEffect(AnimationViewPager.TransitionEffect.CubeOut);
        back= (ImageView) findViewById(R.id.back);
        click= (LinearLayout) findViewById(R.id.click);
        customInit(activityImage, true, false,true,R.color.black);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.BLACK);
            }
        }
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseContext(), "数据传递错误error-1");
            finish();
        }
        list = getIntent().getExtras().getStringArrayList("data");
        if (list == null) {
            ToastUtils.showShortToast(getBaseContext(), "数据传递错误error-2");
            finish();
        }
        addImageViewList(GlidUtils.glideLoad(false, back, getBaseContext(), R.drawable.back));



        viewpagerActivityImage.setOffscreenPageLimit(list.size());
        animationViewPagerAdapter=new AnimationViewPagerAdapter(getBaseContext(),list,viewpagerActivityImage);
        viewpagerActivityImage.setAdapter(animationViewPagerAdapter);
        viewpagerActivityImage.setCurrentItem(getIntent().getExtras().getInt("current"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
