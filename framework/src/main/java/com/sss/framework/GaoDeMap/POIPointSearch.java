package com.sss.framework.GaoDeMap;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;


/**
 * POI兴趣点搜索
 * Created by leilei on 2017/4/20.
 */

public class POIPointSearch implements PoiSearch.OnPoiSearchListener {
    //POI搜索结果
    private final int search_result = 0x001A;
    //POI搜索对象
    private PoiSearch poiSearch;
    //POI请求对象
    private PoiSearch.Query query;
    //查询页码
    private int currentPage = 1;

    private OnPOISearchCallBack onPOISearchCallBack;
    private OnPOIItemSearchCallBack onPOIItemSearchCallBack;

    public void setOnPOISearchCallBack(OnPOISearchCallBack onPOISearchCallBack) {
        this.onPOISearchCallBack = onPOISearchCallBack;
    }

    public void setOnPOIItemSearchCallBack(OnPOIItemSearchCallBack onPOIItemSearchCallBack) {
        this.onPOIItemSearchCallBack = onPOIItemSearchCallBack;
    }

    /**
     * 搜索目标
     *
     * @param context  上下文
     * @param Target   搜索目标
     * @param cityCode 城市代码
     */
    //Target表示搜索字符串，
    //第二个参数表示POI搜索类型，二者选填其一，
    //POI搜索类型共分为以下20种：汽车服务|汽车销售|
    //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
    //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
    //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
    //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
    public void searchTarget(Context context, String Target, String cityCode) {
        //构造POI请求对象
        query = new PoiSearch.Query(Target, "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施", cityCode);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        //设置查询页码
        query.setPageNum(currentPage);
        //构造POI搜索对象
        poiSearch = new PoiSearch(context, query);
        //设置监听
        poiSearch.setOnPoiSearchListener(this);
        //发送搜索请求
        poiSearch.searchPOIAsyn();
    }

    /**
     * 设置查询页码
     *
     * @param currentPage 要查询的页码
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


    /**
     * @param context   上下文
     * @param latitude  维度
     * @param longitude 经度
     * @param distance  距离
     * @param Target    搜索目标
     * @param cityCode  城市代码
     */
    public void searchTargetRound(Context context, double latitude, double longitude, int distance, String Target, String cityCode) {
        //构造POI请求对象
        query = new PoiSearch.Query(Target, "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施", cityCode);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        //设置查询页码
        query.setPageNum(currentPage);
        //构造POI搜索对象
        poiSearch = new PoiSearch(context, query);
        //设置监听
        poiSearch.setOnPoiSearchListener(this);
        //发送搜索请求
        poiSearch.searchPOIAsyn();
        //搜索某一点的周边搜索的中心点以及半径
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), distance));//设置周边搜索的中心点以及半径
        //发送搜索请求
        poiSearch.searchPOIAsyn();
    }


    /**
     * @param poiResult 搜索结果
     * @param i         搜索返回代码1000成功，其余失败
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000) {//success
            if (onPOISearchCallBack != null) {
                onPOISearchCallBack.onPoiSearchedSuccess(poiResult.getPois());
            }
        } else {
            if (onPOISearchCallBack != null) {
                onPOISearchCallBack.onPoiSearchedFail(i);
            }
        }
    }

    /**
     * @param poiItem 搜索结果
     * @param i       搜索返回代码1000成功，其余失败
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        if (onPOIItemSearchCallBack != null) {
            onPOIItemSearchCallBack.onPoiItemSearched(poiItem, i);
        }

    }


    public interface OnPOISearchCallBack {
        void onPoiSearchedSuccess(ArrayList<PoiItem> data);

        void onPoiSearchedFail(int errCode);
    }

    public interface OnPOIItemSearchCallBack {
        void onPoiItemSearched(PoiItem poiItem, int i);

    }
}
