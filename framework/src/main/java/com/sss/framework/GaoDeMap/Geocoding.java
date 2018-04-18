package com.sss.framework.GaoDeMap;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;


/**
 * 地理编码
 * Created by leilei on 2017/4/20.
 */

public class Geocoding {
//    //地理编码监听者(作废)
//    private GeocodingSearchListener mGeocodingSearchListener;
    //地理编码对象
    private GeocodeSearch mGeocodeSearch;
    //地理编码查询
    private GeocodeQuery mGeocodeQuery;
    //逆向地理编码查询
    private RegeocodeQuery mRegeocodeQuery;

    /**
     * 初始化地理编码
     * @param context 上下文
     */
    public void init(Context context,GeocodingSearchListener geocodingSearchListener){
        //实例化地理编码对象
        mGeocodeSearch=new GeocodeSearch(context);
        //地理编码监听者
//        mGeocodingSearchListener=new GeocodingSearchListener(context);
        //为地理编码对象设置监听
        mGeocodeSearch.setOnGeocodeSearchListener(geocodingSearchListener);
    }

    /**
     * 地理编码查询
     * @param adress 查询地址
     * @param cityCode 查询城市代码
     */
    public void query(String adress,String cityCode){
        //实例化地理编码查询
        mGeocodeQuery=new  GeocodeQuery(adress, cityCode);
        //发起查询
        mGeocodeSearch.getFromLocationNameAsyn(mGeocodeQuery);
    }

    /**
     * 逆地理编码查询
     * @param latLonPoint 等同于Latlng
     * @param distance 距离
     */
    public void reverseQuery(LatLonPoint latLonPoint, float distance){
        //实例化逆向地理编码查询， 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        mRegeocodeQuery = new RegeocodeQuery(latLonPoint, distance,GeocodeSearch.AMAP);
        //发起查询
        mGeocodeSearch.getFromLocationAsyn(mRegeocodeQuery);
    }

}
