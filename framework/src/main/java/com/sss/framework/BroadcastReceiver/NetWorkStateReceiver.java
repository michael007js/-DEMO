package com.sss.framework.BroadcastReceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.sss.framework.Dao.OnNetStatusChangedListener;
import com.sss.framework.Library.Log.LogUtils;
import com.sss.framework.Utils.NetworkUtils;

/**
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <p>
 * <p>
 * <receiver android:name="com.sss.framework.BroadcastReceiver.NetWorkStateReceiver">
 * <intent-filter>
 * <!--检测网络变化的acton-->
 * <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
 * <category android:name="android.intent.category.DEFAULT" />
 * </intent-filter>
 * </receiver>
 * </receiver>
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    OnNetStatusChangedListener onNetStatusChangedListener;

    public void  setOnNetStatusChangedListener(OnNetStatusChangedListener onNetStatusChangedListener) {
        this.onNetStatusChangedListener = onNetStatusChangedListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                //WIFI已连接,移动数据已连接
                if (onNetStatusChangedListener != null) {
                    onNetStatusChangedListener.Wifi();
                }

            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                //WIFI已连接,移动数据已断开

            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                //WIFI已断开,移动数据已连接
                if (NetworkUtils.getNetworkType(context) == NetworkUtils.NetworkType.NETWORK_4G) {
                    if (onNetStatusChangedListener != null) {
                        onNetStatusChangedListener.Mobile_4G();
                    }
                } else if (NetworkUtils.getNetworkType(context) == NetworkUtils.NetworkType.NETWORK_3G) {
                    if (onNetStatusChangedListener != null) {
                        onNetStatusChangedListener.Mobile_3G();
                    }
                } else if (NetworkUtils.getNetworkType(context) == NetworkUtils.NetworkType.NETWORK_2G) {
                    if (onNetStatusChangedListener != null) {
                        onNetStatusChangedListener.Mobile_2G();
                    }
                }
            } else {
                //WIFI已断开,移动数据已断开
                if (onNetStatusChangedListener != null) {
                    onNetStatusChangedListener.NoNet();
                }
            }


        } else {
            //API大于23时使用下面的方式进行网络监听
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            if (networks.length == 0) {
                //WIFI已断开,移动数据已断开
                if (onNetStatusChangedListener != null) {
                    onNetStatusChangedListener.NoNet();
                }
                return;
            }
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                LogUtils.e(networkInfo.getType());
                if (networkInfo.getType() == 1 && networkInfo.isConnected()) {
                    //wifi已连接
                    if (onNetStatusChangedListener != null) {
                        onNetStatusChangedListener.Wifi();
                    }
                    return;
                } else if (networkInfo.getType() == 0 && networkInfo.isConnected()) {
                    //移动数据已连接
                    if (NetworkUtils.getNetworkType(context) == NetworkUtils.NetworkType.NETWORK_4G) {
                        if (onNetStatusChangedListener != null) {
                            onNetStatusChangedListener.Mobile_4G();
                        }
                    } else if (NetworkUtils.getNetworkType(context) == NetworkUtils.NetworkType.NETWORK_3G) {
                        if (onNetStatusChangedListener != null) {
                            onNetStatusChangedListener.Mobile_3G();
                        }
                    } else if (NetworkUtils.getNetworkType(context) == NetworkUtils.NetworkType.NETWORK_2G) {
                        if (onNetStatusChangedListener != null) {
                            onNetStatusChangedListener.Mobile_2G();
                        }
                    }
                    return;
                }
            }
        }
    }
}
