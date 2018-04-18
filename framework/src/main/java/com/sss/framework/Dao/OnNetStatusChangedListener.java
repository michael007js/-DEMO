package com.sss.framework.Dao;

/**
 * Created by leilei on 2018/3/21.
 */

public interface OnNetStatusChangedListener {
    void Wifi();
    void Mobile_4G();
    void Mobile_3G();
    void Mobile_2G();
    void NoNet();
}
