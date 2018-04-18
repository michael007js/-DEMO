package com.sss.operation.BaiDuMap;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 地理编码/逆地理编码专用类
 * Created by leilei on 2018/3/26.
 */

public class GeoCoderSearch {
    GeoCoder geoCoder;

    public GeoCoderSearch() {
        if (geoCoder==null){
            geoCoder=GeoCoder.newInstance();
        }
    }

    /**
     * 设置监听
     * @param onGeoCoderSearchCallBack
     */
    public void setOnGetGeoCoderResultListener(final OnGeoCoderSearchCallBack onGeoCoderSearchCallBack) {
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    if (onGeoCoderSearchCallBack!=null){
                        onGeoCoderSearchCallBack.onFail(geoCodeResult.error);
                    }
                    return;
                }
                if (onGeoCoderSearchCallBack!=null){
                    onGeoCoderSearchCallBack.onGetGeoCodeResult(geoCodeResult);
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    if (onGeoCoderSearchCallBack!=null){
                        onGeoCoderSearchCallBack.onFail(reverseGeoCodeResult.error);
                    }
                    return;
                }
                if (onGeoCoderSearchCallBack!=null){
                    onGeoCoderSearchCallBack.onGetReverseGeoCodeResult(reverseGeoCodeResult);
                }
            }
        });
    }

    /**
     * 地址转坐标
     * @param city
     * @param address
     */
    public void geocode(String city, String address){
        geoCoder.geocode( new GeoCodeOption().city(city)
                .address(address));
    }


    /**
     * 坐标转地址
     * @param latLng
     */
    public void reverseGeoCode(LatLng latLng){
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
    }


    public interface OnGeoCoderSearchCallBack{

        void onGetGeoCodeResult(GeoCodeResult geoCodeResult);

        void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult);

        void onFail(SearchResult.ERRORNO errorMsg);
    }

}
