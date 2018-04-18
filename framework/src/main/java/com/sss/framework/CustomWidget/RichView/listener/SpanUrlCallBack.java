package com.sss.framework.CustomWidget.RichView.listener;

import android.view.View;

/**
 * url被点击的回掉
 * Created by shuyu on 2016/11/10.
 */

public interface SpanUrlCallBack {
    void phone(View view, String phone);

    void url(View view, String url);
}
