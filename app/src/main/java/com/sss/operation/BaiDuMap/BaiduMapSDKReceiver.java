package com.sss.operation.BaiDuMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;

/**
 * 百度地图SDK广播接收
 * Created by leilei on 2018/3/24.
 */

public class BaiduMapSDKReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
            // permission check ok
        } else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
            // permission check error
        } else if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
            // network error
        } else if (action.equals(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE)) {
            // error_code
        }
    }


}