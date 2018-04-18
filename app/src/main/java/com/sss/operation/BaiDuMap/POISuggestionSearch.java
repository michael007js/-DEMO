package com.sss.operation.BaiDuMap;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

/**
 * POI热词建议检索
 * Created by leilei on 2018/3/26.
 */

public class POISuggestionSearch {
    SuggestionSearch suggestionSearch;

    public POISuggestionSearch() {
        if (suggestionSearch == null)
            suggestionSearch = SuggestionSearch.newInstance();
    }

    /**
     * 设置搜索监听
     * @param onGetSuggestionResultListener
     */
    public void setOnGetSuggestionResultListener(OnGetSuggestionResultListener onGetSuggestionResultListener){
        suggestionSearch.setOnGetSuggestionResultListener(onGetSuggestionResultListener);
    }

    /**
     * 搜索
     * @param suggestionSearchOption
     */
    public void requestSuggestion(SuggestionSearchOption suggestionSearchOption){
        suggestionSearch.requestSuggestion(suggestionSearchOption);
    }


    /**
     * 释放
     */
    public void destroy(){
        if (suggestionSearch!=null){
            suggestionSearch.destroy();
        }
        suggestionSearch=null;
    }
}
