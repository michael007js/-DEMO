package com.sss.framework.Dao;

import android.content.Context;

/**
 * 定位出错后重新定位回调接口
 * Created by leilei on 2017/5/8.
 */

public interface OnLocationStatusListener {
    void onReLocation(Context context);
}
