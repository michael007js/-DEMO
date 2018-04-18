package com.sss.operation.BaiDuMap;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

/**
 * POI检索专用类
 * Created by leilei on 2018/3/24.
 */

public class POISearch {

    PoiSearch poiSearch;

    public POISearch() {
        if (poiSearch == null) {
            poiSearch = PoiSearch.newInstance();
        }
    }

    /**
     * 设置搜索监听
     *
     * @param onGetPoiSearchResultListener
     */
    public void setOnGetPoiSearchResultListener(OnGetPoiSearchResultListener onGetPoiSearchResultListener) {
        poiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
    }

    /**
     * 搜索附近的POI
     *
     * @param keyword     关键字
     * @param center      圆心点坐标（搜索时会以该点为中心去搜索）
     * @param radius      半径
     * @param pageNum     返回的页数
     * @param poiSortType 排序方式
     */
    public void searchNearby(String keyword, LatLng center, int radius, int pageNum, PoiSortType poiSortType) {
        poiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword(keyword)
                .sortType(poiSortType)
                .location(center)
                .radius(radius)
                .pageNum(pageNum));

    }

    /**
     * 城市内搜索POI
     *
     * @param keyword      关键字
     * @param city         要搜索的城市
     * @param isReturnAddr 是否返回地址信息
     * @param pageNum      返回的页数
     */
    public void searchInCity(String keyword, String city, boolean isReturnAddr, int pageNum) {
        poiSearch.searchInCity(new PoiCitySearchOption()
                .keyword(keyword)
                .city(city)
                .isReturnAddr(isReturnAddr)
                .pageNum(pageNum));

    }

    /**
     * 释放
     */
    public void clear() {
        if (poiSearch != null) {
            poiSearch.setOnGetPoiSearchResultListener(null);
            poiSearch.destroy();
        }
        poiSearch = null;
    }

}
