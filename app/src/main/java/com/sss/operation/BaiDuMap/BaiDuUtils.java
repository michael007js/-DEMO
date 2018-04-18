package com.sss.operation.BaiDuMap;


import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.ScrollView;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.sss.framework.Library.Log.LogUtils;
import com.sss.framework.Model.TargetInfoModel;

import static android.R.attr.y;


/**
 * Created by leilei on 2018/3/24.
 */

public class BaiDuUtils {

    /**
     * 初始化（需在onCreate中setContentView之前）
     *
     * @param context
     */
    public static void init(Context context) {
        SDKInitializer.initialize(context.getApplicationContext());
    }

    /**
     * 定位配置
     * 详见 http://lbsyun.baidu.com/index.php?title=androidsdk/guide/create-map/location
     *
     * @param mode                      LocationMode.FOLLOWING;//定位跟随态
     *                                  LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态
     *                                  LocationMode.COMPASS;  //定位罗盘态
     * @param enableDirection           是否启用指向
     * @param customMarker              自定义定位图标样式，替换定位icon   BitmapDescriptorFactory .fromResource(R.drawable.icon_geo);
     * @param accuracyCircleFillColor   自定义精度圈填充颜色
     * @param accuracyCircleStrokeColor 自定义精度圈边框颜色
     * @param mapView
     */
    public static void locationConfiguration(MyLocationConfiguration.LocationMode mode, boolean enableDirection, BitmapDescriptor customMarker, int accuracyCircleFillColor, int accuracyCircleStrokeColor, MapView mapView) {
        mapView.getMap().setMyLocationConfiguration(new MyLocationConfiguration(mode, enableDirection, customMarker, accuracyCircleFillColor, accuracyCircleStrokeColor));
    }


    /**
     * 定制地图UI控件
     *
     * @param mapView
     */
    public static void initUI(MapView mapView) {
        //地图Logo共支持6个显示位置(左下，中下，右下，左上，中上，右上)。
        mapView.setLogoPosition(LogoPosition.logoPostionleftBottom);
        //指南针
        mapView.getMap().getUiSettings().setCompassEnabled(false);
        //控制是否启用或禁用平移的功能，默认开启。如果启用，则用户可以平移地图。
        mapView.getMap().getUiSettings().setScrollGesturesEnabled(true);
        //控制是否启用或禁用缩放手势，默认开启。如果启用，用户可以双指点击或缩放地图视图。
        mapView.getMap().getUiSettings().setZoomGesturesEnabled(true);
        //控制是否启用或禁用俯视（3D）功能，默认开启。如果启用，则用户可使用双指 向下或向上滑动到俯视图。
        mapView.getMap().getUiSettings().setOverlookingGesturesEnabled(true);
        //控制是否启用或禁用地图旋转功能，默认开启。如果启用，则用户可使用双指 旋转来旋转地图。
        mapView.getMap().getUiSettings().setRotateGesturesEnabled(true);
        //控制是否一并禁止所有手势，默认关闭。如果启用，所有手势都将被禁用。
        mapView.getMap().getUiSettings().setAllGesturesEnabled(true);
        //显示比例尺
        mapView.showScaleControl(false);
        //最大缩放和最小缩放
        mapView.getMap().setMaxAndMinZoomLevel(20, 7);
        //缩放按钮是否显示
        mapView.showZoomControls(false);
    }


    /**
     * 初始化定位监听
     *
     * @param targetInfoModel
     * @return
     */
    public static BDAbstractLocationListener initLocationListerer(final TargetInfoModel targetInfoModel, final OnLocationCallBack onLocationCallBack) {
        if (targetInfoModel == null) {
            throw new NullPointerException("目标结果坐标载体为空");
        }
        return new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    //gps定位成功
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    //网络定位成功
                } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    //离线定位成功，离线定位结果也是有效的
                } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                    //服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
                    if (onLocationCallBack != null) {
                        onLocationCallBack.onLocationFail("服务端网络定位失败");
                    }
                    return;
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                    //网络不同导致定位失败，请检查网络是否通畅
                    if (onLocationCallBack != null) {
                        onLocationCallBack.onLocationFail("网络不同导致定位失败，请检查网络是否通畅");
                    }
                    return;
                } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    if (onLocationCallBack != null) {
                        onLocationCallBack.onLocationFail("无法获取有效定位依据，请检查网络是否通畅");
                    }
                    return;
                }
                targetInfoModel.setLocationType(bdLocation.getLocType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                targetInfoModel.setLatitude(bdLocation.getLatitude());//获取纬度
                targetInfoModel.setLongitude(bdLocation.getLongitude());//获取经度
                targetInfoModel.setAddress(bdLocation.getAddress().address);//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                targetInfoModel.setCountry(bdLocation.getCountry());//国家信息
                targetInfoModel.setProvince(bdLocation.getProvince());//省信息
                targetInfoModel.setCity(bdLocation.getCity());
                //城市信息
                targetInfoModel.setDistrict(bdLocation.getDistrict());//城区信息
                targetInfoModel.setStreet(bdLocation.getStreet());//街道信息
                targetInfoModel.setStreetNum(bdLocation.getStreetNumber());
                //街道门牌号信息
                targetInfoModel.setCityCode(bdLocation.getCityCode());//城市编码
                targetInfoModel.setAdCode(bdLocation.getAdCode());//地区编码
                targetInfoModel.setBuildingId(bdLocation.getBuildingID());//获取当前室内定位的建筑物Id
                targetInfoModel.setFloor(bdLocation.getFloor());//获取当前室内定位的楼层
                targetInfoModel.setGetGpsAccuracyStatus(bdLocation.getGpsAccuracyStatus());//获取GPS的精度状态
                targetInfoModel.setGetAltitude(bdLocation.getAltitude());//获得高度
                targetInfoModel.setGetLocationDetail(bdLocation.getLocationDescribe());//获得具体位置
                targetInfoModel.setGetSpeed(bdLocation.getSpeed());//获得当前速度
                targetInfoModel.setTime(bdLocation.getTime()); //获取定位时间
                if (onLocationCallBack != null) {
                    onLocationCallBack.onLocationSuccess(targetInfoModel);
                }
            }
        };
    }


    /**
     * 注册百度地图SDK广播监听
     *
     * @param baiduMapSDKReceiver
     * @param activity
     */
    public static void registerBaiduMapSDKReceiver(BaiduMapSDKReceiver baiduMapSDKReceiver, Activity activity) {
        if (baiduMapSDKReceiver == null) {
            throw new NullPointerException("百度地图SDK广播实例为空==》BaiduMapSDKReceiver");
        }
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        activity.registerReceiver(baiduMapSDKReceiver, iFilter);
    }

    /**
     * 取消百度地图SDK广播监听
     *
     * @param baiduMapSDKReceiver
     * @param activity
     */
    public static void unRegisterBaiduMapSDKReceiver(BaiduMapSDKReceiver baiduMapSDKReceiver, Activity activity) {
        if (baiduMapSDKReceiver == null) {
            throw new NullPointerException("百度地图SDK广播实例为空==》BaiduMapSDKReceiver");
        }
        activity.unregisterReceiver(baiduMapSDKReceiver);
    }

    /**
     * 解决地图的touch事件和touch事件冲突问题
     * 重写onTouch()事件,在事件里通过requestDisallowInterceptTouchEvent(boolean)方法来设置父类的不可用,true表示父类的不可用
     *
     * @param mapView
     * @param view
     */
    public static void setOnTouchListener(final MapView mapView, final View view) {
        mapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (view instanceof ScrollView) {
                        ((ScrollView) view).requestDisallowInterceptTouchEvent(false);
                        ((ScrollView) view).scrollTo(0, 1);
                    } else if (view instanceof ListView) {
                        ((ListView) view).requestDisallowInterceptTouchEvent(false);
                        ((ListView) view).scrollTo(0, 1);
                    } else if (view instanceof RecyclerView) {
                        ((RecyclerView) view).requestDisallowInterceptTouchEvent(false);
                        ((RecyclerView) view).scrollTo(0, 1);
                    } else if (view instanceof ViewPager) {
                        ((ViewPager) view).requestDisallowInterceptTouchEvent(false);
                    }
                } else {
                    if (view instanceof ScrollView) {
                        ((ScrollView) view).requestDisallowInterceptTouchEvent(true);
                        ((ScrollView) view).scrollTo(0, 1);
                    } else if (view instanceof ListView) {
                        ((ListView) view).requestDisallowInterceptTouchEvent(true);
                        ((ListView) view).scrollTo(0, 1);
                    } else if (view instanceof RecyclerView) {
                        ((RecyclerView) view).requestDisallowInterceptTouchEvent(true);
                        ((RecyclerView) view).scrollTo(0, 1);
                    } else if (view instanceof ViewPager) {
                        ((ViewPager) view).requestDisallowInterceptTouchEvent(true);
                    }
                }
                return false;
            }
        });

    }


    /**
     * 是否打开室内图，默认为关闭状态
     * 相关POI搜索在POISearchIndoor类中
     * 详见 http://lbsyun.baidu.com/index.php?title=androidsdk/guide/create-map/indoormap
     *
     * @param mapView
     * @param enable
     */
    public static void setIndoorEnable(MapView mapView, boolean enable) {
        mapView.getMap().setIndoorEnable(enable);
    }


    /**
     * 置地图缩放级别
     * mapView.getMap().zoomIn():放大地图缩放级别
     * mapView.getMap().zoomOut()：缩小地图缩放级别
     *
     * @param mapView
     * @param zoom
     */
    public static void setMapStatus(MapView mapView, float zoom) {
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomTo(zoom));
    }


    //初始化添加覆盖物mark
    public static Marker showTargetMarker(MapView mapView, Double lat, Double lng, Context context, int icon) {
        MarkerOptions markerOptions = new MarkerOptions();


        markerOptions.position(new LatLng(lat, lng))//mark出现的位置
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), icon)))       //mark图标
                .draggable(true)//mark可拖拽
                .animateType(MarkerOptions.MarkerAnimateType.drop)//从天而降的方式
        //.animateType(MarkerOptions.MarkerAnimateType.grow)//从地生长的方式
        ;
        //添加mark
        return (Marker) (mapView.getMap().addOverlay(markerOptions));//地图上添加mark

    }


    /**
     * 快速显示目标的位置
     *
     * @param lng
     * @param lat
     * @param mapView
     * @param zoom
     */
    public static void locationTarget(double lat, double lng, MapView mapView, float zoom) {
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(new LatLng(lat, lng)).zoom(zoom).build()));
    }


    /**
     * 进入和移出室内图,通过获取回调参数 mapBaseIndoorMapInfo 便可获取室内图信息，包含楼层信息，室内ID等
     * 详见 http://lbsyun.baidu.com/index.php?title=androidsdk/guide/create-map/indoormap
     *
     * @param mapView
     * @param onBaseIndoorMapListener
     */
    public static void setOnBaseIndoorMapListener(MapView mapView, BaiduMap.OnBaseIndoorMapListener onBaseIndoorMapListener) {
        mapView.getMap().setOnBaseIndoorMapListener(onBaseIndoorMapListener);
    }

    /**
     * 地图状态改变监听
     * 包含手势、设置地图状态或其他某种操作导致地图状态开始改变，地图状态变化中、地图状态改变结束等监听方法。
     * 详见 http://lbsyun.baidu.com/index.php?title=androidsdk/guide/interaction/event
     *
     * @param mapView
     * @param onMapStatusChangeListener
     */
    public static void setOnMapStatusChangeListener(MapView mapView, BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener) {
        mapView.getMap().setOnMapStatusChangeListener(onMapStatusChangeListener);
    }

    /**
     * 地图单击事件监听
     *
     * @param mapView
     * @param onMapClickListener
     */
    public static void setOnMapClickListener(MapView mapView, BaiduMap.OnMapClickListener onMapClickListener) {
        mapView.getMap().setOnMapClickListener(onMapClickListener);
    }

    /**
     * 地图创建完成时回调.
     *
     * @param mapView
     * @param onMapLoadedCallback
     */
    public static void setOnMapLoadedCallback(MapView mapView, BaiduMap.OnMapLoadedCallback onMapLoadedCallback) {
        mapView.getMap().setOnMapLoadedCallback(onMapLoadedCallback);
    }

    /**
     * 地图渲染完成回调.
     * 每次对地图有操作时，绘制完成时调用
     *
     * @param mapView
     * @param onMapRenderCallback
     */
    public static void setOnMapRenderCallback(MapView mapView, BaiduMap.OnMapRenderCallback onMapRenderCallback) {
        mapView.getMap().setOnMapRenderCallbadk(onMapRenderCallback);
    }

    /**
     * 地图双击事件监听
     *
     * @param mapView
     * @param onMapDoubleClickListener
     */
    public static void setOnMapDoubleClickListener(MapView mapView, BaiduMap.OnMapDoubleClickListener onMapDoubleClickListener) {
        mapView.getMap().setOnMapDoubleClickListener(onMapDoubleClickListener);
    }

    /**
     * 地图长按事件监听
     *
     * @param mapView
     * @param onMapLongClickListener
     */
    public static void setOnMapLongClickListener(MapView mapView, BaiduMap.OnMapLongClickListener onMapLongClickListener) {
        mapView.getMap().setOnMapLongClickListener(onMapLongClickListener);
    }

    /**
     * 地图 Marker 覆盖物点击事件监听
     *
     * @param mapView
     * @param onMarkerClickListener
     */
    public static void setOnOnMarkerClickListener(MapView mapView, BaiduMap.OnMarkerClickListener onMarkerClickListener) {
        mapView.getMap().setOnMarkerClickListener(onMarkerClickListener);
    }

    /**
     * 地图定位图标点击事件监听
     *
     * @param mapView
     * @param onMyLocationClickListener
     */
    public static void setOnMyLocationClickListener(MapView mapView, BaiduMap.OnMyLocationClickListener onMyLocationClickListener) {
        mapView.getMap().setOnMyLocationClickListener(onMyLocationClickListener);
    }

    /**
     * 触摸地图回调
     *
     * @param mapView
     * @param onMapTouchListener
     */
    public static void setOnMapTouchListener(MapView mapView, BaiduMap.OnMapTouchListener onMapTouchListener) {
        mapView.getMap().setOnMapTouchListener(onMapTouchListener);
    }


    public interface OnLocationCallBack {
        void onLocationSuccess(TargetInfoModel targetInfoModel);

        void onLocationFail(String errorInfo);
    }


}
