package com.sss.framework.GaoDeMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.ScrollView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.sss.framework.Library.Log.LogUtils;
import com.sss.framework.Model.TargetInfoModel;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by leilei on 2017/12/18.
 */

@SuppressWarnings("ALL")
public class GaoDeUtils {


    /**
     * 初始化定位蓝点样式需求
     *
     * @param mapView
     * @param context
     * @param mode    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
     *                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
     *                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
     *                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
     *                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
     *                //以下三种模式从5.1.0版本开始提供
     *                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
     *                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
     *                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
     * @param icon
     * @param zoom    缩放
     */
    public static void locationStyle(MapView mapView, Context context, int mode, int icon, int zoom) {
        //设置定位蓝点的Style
        mapView.getMap().setMyLocationStyle(new LocationStyle(mode, context, icon).getMyLocationStyle().showMyLocation(false)/*设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。*/);
        // 触发定位并显示当前位置,设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mapView.getMap().setMyLocationEnabled(false);
        //设置默认缩放级别
        mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(zoom));
        mapView.getMap().setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
    }

    /**
     * 设置地图UI
     *
     * @param mapView
     */
    public static void initUI(MapView mapView) {
        UIsetting.init(mapView.getMap().getUiSettings());
    }


    /**
     * 初始化定位监听
     *
     * @param activity
     * @param mainBiz         错误代码:
     *                        0定位成功。
     *                        可以在定位回调里判断定位返回成功后再进行业务逻辑运算。
     *                        1一些重要参数为空，如context；
     *                        请对定位传递的参数进行非空判断。
     *                        2定位失败，由于仅扫描到单个wifi，且没有基站信息。
     *                        请重新尝试。
     *                        3获取到的请求参数为空，可能获取过程中出现异常。
     *                        请对所连接网络进行全面检查，请求可能被篡改。
     *                        4请求服务器过程中的异常，多为网络情况差，链路不通导致
     *                        请检查设备网络是否通畅，检查通过接口设置的网络访问超时时间，建议采用默认的30秒。
     *                        5请求被恶意劫持，定位结果解析失败。
     *                        您可以稍后再试，或检查网络链路是否存在异常。
     *                        6定位服务返回定位失败。
     *                        请获取errorDetail（通过getLocationDetail()方法获取）信息并参考定位常见问题进行解决。
     *                        7KEY鉴权失败。
     *                        请仔细检查key绑定的sha1值与apk签名sha1值是否对应，或通过高频问题查找相关解决办法。
     *                        8Android exception常规错误
     *                        请将errordetail（通过getLocationDetail()方法获取）信息通过工单系统反馈给我们。
     *                        9定位初始化时出现异常。
     *                        请重新启动定位。
     *                        10定位客户端启动失败。
     *                        请检查AndroidManifest.xml文件是否配置了APSService定位服务
     *                        11定位时的基站信息错误。
     *                        请检查是否安装SIM卡，设备很有可能连入了伪基站网络。
     *                        12缺少定位权限。
     *                        请在设备的设置中开启app的定位权限。
     *                        13定位失败，由于设备未开启WIFI模块或未插入SIM卡，且GPS当前不可用。
     *                        建议开启设备的WIFI模块，并将设备中插入一张可以正常工作的SIM卡，或者检查GPS是否开启；如果以上都内容都确认无误，请您检查App是否被授予定位权限。
     *                        14GPS 定位失败，由于设备当前 GPS 状态差。
     *                        建议持设备到相对开阔的露天场所再次尝试。
     *                        15定位结果被模拟导致定位失败
     *                        如果您希望位置被模拟，请通过setMockEnable(true);方法开启允许位置模拟
     *                        16当前POI检索条件、行政区划检索条件下，无可用地理围栏
     *                        建议调整检索条件后重新尝试，例如调整POI关键字，调整POI类型，调整周边搜区域，调整行政区关键字等。
     * @param targetInfoModel
     * @return
     */
    public static AMapLocationListener initLocationListerer(final TargetInfoModel targetInfoModel, final OnLocationCallBack onLocationCallBack) {
        return new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LogUtils.d(aMapLocation.toString());
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {//success
                        //可在其中解析amapLocation获取相应内容。
                        targetInfoModel.setLocationType(aMapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        targetInfoModel.setLatitude(aMapLocation.getLatitude());//获取纬度
                        targetInfoModel.setLongitude(aMapLocation.getLongitude());//获取经度
                        targetInfoModel.setAccuracy(aMapLocation.getAccuracy());//获取精度信息
                        targetInfoModel.setAddress(aMapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        targetInfoModel.setCountry(aMapLocation.getCountry());//国家信息
                        targetInfoModel.setProvince(aMapLocation.getProvince());//省信息
                        targetInfoModel.setCity(aMapLocation.getCity());
                        //城市信息
                        targetInfoModel.setDistrict(aMapLocation.getDistrict());//城区信息
                        targetInfoModel.setStreet(aMapLocation.getStreet());//街道信息
                        targetInfoModel.setStreetNum(aMapLocation.getStreetNum());
                        //街道门牌号信息
                        targetInfoModel.setCityCode(aMapLocation.getCityCode());//城市编码
                        targetInfoModel.setAdCode(aMapLocation.getAdCode());//地区编码
                        targetInfoModel.setAoiName(aMapLocation.getAoiName());//获取当前定位点的AOI信息
                        targetInfoModel.setBuildingId(aMapLocation.getBuildingId());//获取当前室内定位的建筑物Id
                        targetInfoModel.setFloor(aMapLocation.getFloor());//获取当前室内定位的楼层
                        targetInfoModel.setGetGpsAccuracyStatus(aMapLocation.getGpsAccuracyStatus());//获取GPS的精度状态
                        targetInfoModel.setGetAltitude(aMapLocation.getAltitude());//获得高度
                        targetInfoModel.setGetBearing(aMapLocation.getBearing());//获得方位
                        targetInfoModel.setGetLocationDetail(aMapLocation.getLocationDetail());//获得具体位置
                        targetInfoModel.setGetRoad(aMapLocation.getRoad());//获得道路
                        targetInfoModel.setGetSpeed(aMapLocation.getSpeed());//获得当前速度
                        targetInfoModel.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(aMapLocation.getTime()))); //获取定位时间
                        if (onLocationCallBack != null) {
                            onLocationCallBack.onLocationSuccess(targetInfoModel);
                        }
                        LogUtils.d(targetInfoModel.toString());
                    } else {//fail
                        if (onLocationCallBack != null) {
                            onLocationCallBack.onLocationFail(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
                        }
                        LogUtils.d("sssss", "location_1 Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
    }


    /**
     * 初始化屏幕最中间的那个点
     *
     * @param mapView 地图
     * @return
     */
    public static Marker initCenter(final MapView mapView, final Context context, final int icon) {
        final Marker[] marker = new Marker[1];
        mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (-1 == icon) {
                    marker[0] = mapView.getMap().addMarker(new MarkerOptions());
                    marker[0].setPositionByPixels(mapView.getWidth() / 2, mapView.getHeight() / 2);
                    mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    marker[0] = mapView.getMap().addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), icon))));
                    marker[0].setPositionByPixels(mapView.getWidth() / 2, mapView.getHeight() / 2);
                    mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        return marker[0];
    }

    /**
     * 显示Mark
     *
     * @param converter   不同坐标系的转换类，可空，如果为空，则后面coordType无意义，可传null
     * @param coordType   不同坐标系的待转坐标类型，如需要将百度坐标系转成高德坐标系，则CoordinateConverter.CoordType.BAIDU
     * @param mapView
     * @param lat
     * @param lng
     * @param context
     * @param icon        marker图标
     * @param title       marker标题
     * @param snippet     marker摘要
     * @param obj         marker对象
     * @param isDraggable marker是否允许拖动
     * @param rotateAngle 修复marker在地图上图标偏转的问题
     * @return
     */
    public static Marker showTargetMarker(CoordinateConverter converter, CoordinateConverter.CoordType coordType, MapView mapView, Double lat, Double lng, Context context, int icon, String title, String snippet, Object obj, boolean isDraggable, int rotateAngle) {
        Marker marker = null;
        MarkerOptions markerOptions = new MarkerOptions();
        if (converter != null) {
            if (coordType==null){
                throw new NullPointerException("待转坐标类型为空");
            }
            converter.from(coordType);
            // sourceLatLng待转换坐标点 LatLng类型
            converter.coord(new com.amap.api.maps2d.model.LatLng(lat, lng));
            // 执行转换操作
            com.amap.api.maps2d.model.LatLng temp = converter.convert();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), icon)))
                    .anchor(1.0f, 1.0f)
                    .position(new LatLng(temp.latitude, temp.longitude));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), icon)))
                    .anchor(1.0f, 1.0f)
                    .position(new LatLng(lat, lng));
        }
        marker = mapView.getMap().addMarker(markerOptions);
        marker.setDraggable(isDraggable);
        marker.setTitle(title);
        marker.setSnippet(snippet);
        marker.setRotateAngle(rotateAngle);
        marker.setObject(obj);
        return marker;
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
     * 设置视角改变监听事件
     *
     * @param mapView
     * @param onCameraChange
     */
    public static void setOnCameraChangeListener(MapView mapView, final AMap.OnCameraChangeListener onCameraChangeListener) {
        mapView.getMap().setOnCameraChangeListener(onCameraChangeListener);
    }

    /**
     * 地图拖动事件
     *
     * @param mapView
     * @param onMapTouchListener
     */
    public static void setOnMapTouchListener(MapView mapView, AMap.OnMapTouchListener onMapTouchListener) {
        mapView.getMap().setOnMapTouchListener(onMapTouchListener);
    }

    /**
     * 地图点击事件
     *
     * @param mapView
     * @param onMapTouchListener
     */
    public static void setOnMapClickListener(MapView mapView, AMap.OnMapClickListener onMapClickListener) {
        mapView.getMap().setOnMapClickListener(onMapClickListener);
    }

    /**
     * 地图MARKER点击事件
     *
     * @param mapView
     * @param onMapTouchListener
     */
    public static void setOnMarkerClickListener(MapView mapView, AMap.OnMarkerClickListener onMarkerClickListener) {
        mapView.getMap().setOnMarkerClickListener(onMarkerClickListener);
    }

    /**
     * 快速显示目标的位置
     */
    public static void locationTarget(double lat, double lng, MapView mapView, float zoom) {
        mapView.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        mapView.invalidate();//刷新地图
    }


    /**
     * 地图缩放
     *
     * @param zoom 缩放级别
     */
    public static void zoom(MapView mapView, float zoom) {
        mapView.getMap().animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    /**
     * 设置地图显示模式
     *
     * @param type AMap.MAP_TYPE_NAVI 导航地图，AMap.MAP_TYPE_NIGHT夜景地图，AMap.MAP_TYPE_NORMAL白昼地图（即普通地图）， AMap.MAP_TYPE_SATELLITE卫星图
     */
    public static void changeShowType(MapView mapView, int type) {
        mapView.getMap().setMapType(type);// 设置卫星地图模式，aMap是地图控制器对象。
    }

    /**
     * 地理编码
     * @param geocoding 地理编码类
     * @param distance  距离
     *                  <p>
     *                  regeocodeResult.getRegeocodeAddress().getFormatAddress()//解析地址
     *                  <p>
     *                  regeocodeResult.getRegeocodeQuery().getPoint().getLatitude()
     *                  regeocodeResult.getRegeocodeQuery().getPoint().getLongitude()
     * @Override public void onGeocodingSearchFail(RegeocodeResult regeocodeResult, int i) {
     * <p>
     * }
     * })
     */
    public static void resolve(Context context, Geocoding geocoding, final double lat, final double lng, float distance, GeocodingSearchListener.GeocodingSearchCallBack geocodingSearchCallBack) {
        //初始化地理编码
        geocoding.init(context, new GeocodingSearchListener(context, geocodingSearchCallBack));
        geocoding.reverseQuery(new LatLonPoint(lat, lng), 0f);
    }

    public interface OnLocationCallBack {
        void onLocationSuccess(TargetInfoModel targetInfoModel);

        void onLocationFail(int errorCode, String errorInfo);
    }


}
