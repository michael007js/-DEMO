package com.sss.framework.Application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sss.framework.BroadcastReceiver.OnePixelReceiver;
import com.sss.framework.CrashException.CrashException;
import com.sss.framework.CustomWidget.Layout.LayoutLoading.LoadingLayout;
import com.sss.framework.Library.Fresco.FrescoImageLoader;
import com.sss.framework.Library.Fresco.FrescoImagePipelineConfig;
import com.sss.framework.Library.Glid.GlideImageLoader;
import com.sss.framework.Library.Log.LogUtils;
import com.sss.framework.Library.Log.inner.LogcatTree;
import com.sss.framework.Library.Notifyutil.NotifyUtil;
import com.sss.framework.R;
import com.sss.framework.Utils.NetStatusUtils;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　  ┏┓　　　┏┓
 * 　　┏━┛┻━━━┛┻┓
 * 　　┃　　　　　　　  ┃
 * 　　┃　 　　━　　　 ┃
 * 　　┃　 ┳┛　┗┳　 ┃
 * 　　┃　　　　　　  　┃
 * 　　┃　　 　┻　　 　┃
 * 　　┃　　　　　　  　┃
 * 　　┗━━┓　　　┏━┛
 * 　　　　  ┃　　　┃天灵灵,地灵灵
 * 　　　　  ┃　　　┃神兽保佑
 * 　　　  　┃　　　┃代码无BUG！
 * 　　　　  ┃　　　┗━━━━┓
 * 　　　　  ┃　　　　　　　  ┃
 * 　　　　  ┃　　　　　　  　┣┓
 * 　　　  　┃　　　　　　  　┏┛
 * 　　　  　┗┓┓┏━━┳┓┏┛
 * 　　　　　  ┃┫┫　  ┃┫┫
 * 　　　　　  ┗┻┛　  ┗┻┛
 * <p>
 * ━━━━━━神兽出没━━━━━━━
 * <p>
 * <p>
 * Created by 61642 on 2017/11/23.
 */

@SuppressWarnings("ALL")
public class FrameworkApplication extends MultiDexApplication/*LitePalApplication*/ {
    private OnePixelReceiver onepxReceiver;
    private static FunctionConfig functionConfig;
    private ImageLoader imageloader;
    private CoreConfig coreConfig;
    private static NetStatusUtils.OnNetEventCallBack netEvevt;
    private static NetStatusUtils netStatusUtils;
    private static boolean isPing;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        initErrorLog();
        repairAndroidOsFileUriExposedException();
        initFresco();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }




    /**
     * 初始化Log日志
     */
    public void initLogger() {

        LogUtils.getLogConfig()
                .configAllowLog(true)//是否输出日志
                .configShowBorders(true)//是否排版显示
                .configTagPrefix("ViseLog")//设置标签前缀
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")//个性化设置标签，默认显示包名
                .configLevel(Log.VERBOSE);//设置日志最小输出级别，默认Log.VERBOSE
        LogUtils.plant(new LogcatTree());//添加打印日志信息到Logcat的树

    }

    /**
     * 配置加载布局
     *
     * @param errorText             出错提示
     * @param emptyText             无数据提示
     * @param noNetworkText         没有网络提示
     * @param errorImage            出错时图片
     * @param emptyImage            无数据时图片
     * @param noNetworkImage        无网络时图片
     * @param tipTextColor          提示字体颜色
     * @param reloadButtonText      重载按钮文字
     * @param reloadButtonTextColor 重载按钮字体颜色
     */
    public void setLoadingLayoutConfig(String errorText, String emptyText, String noNetworkText, int errorImage, int emptyImage, int noNetworkImage, int tipTextColor, String reloadButtonText, int reloadButtonTextColor) {
        LoadingLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setErrorImage(errorImage)
                .setEmptyImage(emptyImage)
                .setNoNetworkImage(noNetworkImage)
                .setAllTipTextColor(tipTextColor)
                .setAllTipTextSize(15)
                .setReloadButtonText(reloadButtonText)
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(reloadButtonTextColor)
                .setReloadButtonWidthAndHeight(150, 40);
    }

    /**
     * 初始化一像素保活
     */
    public void initOnePxAlive() {
        //注册监听屏幕的广播
        if (onepxReceiver == null) {
            //注册监听屏幕的广播
            onepxReceiver = new OnePixelReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
            registerReceiver(onepxReceiver, intentFilter);
        }

    }

    /**
     * 初始化网络监听(包含判断网络类型与PING网络延迟,需要NetStatusBroadcastReceiver支持,只有在网络状态被改变的时收到系统广播后才开始监听,)
     *
     * @param isPing   是否需要实时Ping
     * @param netEvevt 网络事件回调
     */
    public void initNetWorkStatusListener(boolean isPing, NetStatusUtils.OnNetEventCallBack netEvevt) {
        this.isPing = isPing;
        this.netEvevt = netEvevt;
    }

    /**
     * 初始化网络监听(只包含PING网络延迟)
     *
     * @param isPing
     * @param netEvevt
     */
    public void initNetWorkPing(boolean isPing, NetStatusUtils.OnNetEventCallBack netEvevt) {
        this.isPing = isPing;
        if (netStatusUtils == null) {
            netStatusUtils = new NetStatusUtils();
        }
        netStatusUtils.pingSwitch(this.isPing);
        netStatusUtils.ping(getApplicationContext(), netEvevt);
    }

    /**
     * 初始化通知栏配置
     */
    public void initNotification() {
        NotifyUtil.init(getApplicationContext());
    }

    /**
     * 拍照与相册配置器
     *
     * @return
     */
    public static FunctionConfig getFunctionConfig() {
        return functionConfig;
    }


    /**
     * 错误监视
     */
    private void initErrorLog() {
        CrashException crashException = CrashException.getInstance();
        crashException.init(this);
//        CrashUtils.init(new CrashUtils.OnCrashListener() {
//            @Override
//            public void onCrash(Throwable e) {
//            }
//        });
    }

    /**
     * 初始化fresco
     */
    public void initFresco() {
        Fresco.initialize(this, FrescoImagePipelineConfig.getImagePipelineConfig(this, Bitmap.Config.RGB_565));
    }

    /**
     * 初始化拍照与相册配置器(如果需要使用fresco，则必须先调用initFresco)
     */
    public void initCameraConfig() {
        if (functionConfig == null) {
            //配置功能
            functionConfig = new FunctionConfig.Builder()
                    .setMutiSelectMaxSize(9)//配置多选数量
                    .setEnableEdit(true)//开启编辑功能
                    .setEnableCrop(true)//开启裁剪功能
                    .setEnableRotate(false)//开启旋转功能
                    .setEnableCamera(false)//开启相机功能
//            .setCropWidth( int width)//裁剪宽度
//            .setCropHeight( int height)//裁剪高度
                    .setCropSquare(true)//裁剪正方形
//            .setSelected(List)//添加已选列表,只是在列表中默认呗选中不会过滤图片
//           . setFilter(List)//添加图片过滤，也就是不在GalleryFinal中显示
//           . takePhotoFolter(File )//配置拍照保存目录，不做配置的话默认是/sdcard/DCIM/GalleryFinal/
                    . setRotateReplaceSource(false)//配置选择图片时是否替换原始图片，默认不替换
                    . setCropReplaceSource(false)//配置裁剪图片时是否替换原始图片，默认不替换
                    .setForceCrop(false)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                    .setForceCropEdit(true)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
                    .setEnablePreview(true)//是否开启预览功能
                    .build();
            //配置imageloader
            imageloader = new GlideImageLoader();
//            imageloader = new FrescoImageLoader(this);
            //核心配置
            coreConfig = new CoreConfig.Builder(getApplicationContext(), imageloader,
                /*设置主题*/
                    new ThemeConfig.Builder()
                            .setTitleBarBgColor(getResources().getColor(R.color.mainColor))
                            .build())
                    .setFunctionConfig(functionConfig).build();
            GalleryFinal.init(coreConfig);
        }

    }

    /**
     * 修复调用Uri.fromFile 报android.os.FileUriExposedException
     */
    public void repairAndroidOsFileUriExposedException() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    public static Context getContext() {
        return context;
    }


    /**
     * 自定义检查手机网络状态是否切换的广播接受器
     * <p>
     * <p>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * <p>
     * <p>
     * 记得在manifest中注册
     * <receiver android:name="com.sss.framework.application.UtilCodeApplication$NetStatusBroadcastReceiver" >
     * <intent-filter>
     * <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
     * </intent-filter>
     * </receiver>
     */
    public class NetStatusBroadcastReceiver extends BroadcastReceiver {
        /*网络连接类型-1未连接  0移动网络  1无线网络*/
        int netWorkState;

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // 如果相等的话就说明网络状态发生了变化
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                if (netStatusUtils == null && isPing == true) {
                    netStatusUtils = new NetStatusUtils();
                }
                netWorkState = netStatusUtils.getNetWorkState(context);
                // 接口回调传过去状态的类型
                if (netEvevt != null) {
                    netEvevt.onNetChange(context, netWorkState);
                    if (netWorkState != -1) {
                        if (isPing == true) {
                            LogUtils.e(netWorkState + "");
                            netStatusUtils.pingSwitch(true);
                            netStatusUtils.ping(context, netEvevt);
                        }
                    } else {
                        LogUtils.e(netWorkState + "");
                        if (isPing == true) {
                            netStatusUtils.pingSwitch(false);
                        }
                    }
                }
            }
        }
    }


}
