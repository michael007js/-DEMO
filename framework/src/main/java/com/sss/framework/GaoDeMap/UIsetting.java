package com.sss.framework.GaoDeMap;

import com.amap.api.maps.UiSettings;

/**
 * 控件交互设置
 * Created by leilei on 2017/4/19.
 */

public class UIsetting {


    /**
     * 初始化地图控件信息
     * uiSettings=aMap.aMap.getUiSettings()
     *
     * @param
     */
    public static void init(UiSettings uiSettings) {
        //实例化UiSettings类对象
        //缩放按钮是提供给 App 端用户控制地图缩放级别的交换按钮，每次点击改变1个级别，此控件默认打开
        uiSettings.setZoomControlsEnabled(false);
        //指南针用于向 App 端用户展示地图方向，默认不显示。
        uiSettings.setCompassEnabled(false);
        //设置默认定位按钮是否显示
        uiSettings.setMyLocationButtonEnabled(false);
        //控制比例尺控件是否显示
        uiSettings.setScaleControlsEnabled(false);
        //设置启用手势交互
        uiSettings.setAllGesturesEnabled(true);
        //控制地图是否支持手势拖动旋转
        uiSettings.setRotateGesturesEnabled(false);
        //控制地图是否支持手势拖动倾斜
        uiSettings.setTiltGesturesEnabled(true);
        //控制地图是否支持手势缩放
        uiSettings.setZoomGesturesEnabled(true);
        //设置初始缩放级别
        uiSettings.setZoomPosition(1);
        //将Logo向左偏移50,达到影藏的效果
        uiSettings.setLogoBottomMargin(-50);
    }

    /**
     * 旋转手势是否生效
     *
     * @return
     */
    public static boolean isRotateGesturesEnabled(UiSettings uiSettings) {
        return uiSettings.isRotateGesturesEnabled();
    }


    /**
     * 缩放手势是否生效
     *
     * @return
     */
    public static boolean isZoomGesturesEnabled(UiSettings uiSettings) {
        return uiSettings.isZoomGesturesEnabled();
    }


    /**
     * 滑动手势是否生效
     *
     * @return
     */
    public boolean isScrollGesturesEnabled(UiSettings uiSettings) {
        return uiSettings.isScrollGesturesEnabled();
    }


    /**
     * 倾斜手势是否生效
     *
     * @return
     */
    public static boolean isTiltGesturesEnabled(UiSettings uiSettings) {
        return uiSettings.isTiltGesturesEnabled();
    }
}
