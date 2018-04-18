package com.sss.framework.GaoDeMap;

/**
 * Created by leilei on 2017/7/26.
 */

import android.content.Context;

import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * 地理编码监听
 */
public class GeocodingSearchListener implements GeocodeSearch.OnGeocodeSearchListener {
    public interface GeocodingSearchCallBack{
        void onGeocodingSearchSuccess(RegeocodeResult regeocodeResult, int i);
        void onGeocodingSearchFail(RegeocodeResult regeocodeResult, int i);
    }

    //上下文
    private Context mContext;
    private GeocodingSearchCallBack geocodingSearchCallBack;
    /**
     * 构造地理编码监听
     * @param mContext 上下文
     */
    public GeocodingSearchListener(Context mContext,GeocodingSearchCallBack geocodingSearchCallBack) {
        this.geocodingSearchCallBack=geocodingSearchCallBack;
        this.mContext = mContext;
    }

    public void onDestroy(){
        this.geocodingSearchCallBack=null;
        mContext=null;
    }

    /**
     * 查询结果
     * @param regeocodeResult 查询结果
     * @param i 查询成功与否代码 1000成功，其余失败
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if ( i==1000){
            geocodingSearchCallBack.onGeocodingSearchSuccess(regeocodeResult,i);
//            Toast.makeText(mContext,regeocodeResult.getRegeocodeAddress().getFormatAddress(),Toast.LENGTH_SHORT).show();
        }else {
            geocodingSearchCallBack.onGeocodingSearchFail(regeocodeResult,i);
//            if (i==1806){
//                Toast.makeText(mContext,"查询失败errCode:"+i+"http或socket连接失败 - ConnectionException,\n请检查网络状况以及网络的稳定性",Toast.LENGTH_SHORT).show();
//            }
        }

    }
    /**
     * 查询结果
     * @param geocodeResult 查询结果
     * @param i 查询成功与否代码 1000成功，其余失败
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }
}
