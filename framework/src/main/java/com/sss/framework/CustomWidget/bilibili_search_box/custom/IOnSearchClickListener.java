package com.sss.framework.CustomWidget.bilibili_search_box.custom;

/**
 * Created by Won on 2017/1/13.
 */

public interface IOnSearchClickListener {

    void OnSearchClick(String keyword);

    void onAttadchToWindow();

    void onDetachFromWindow();

}
