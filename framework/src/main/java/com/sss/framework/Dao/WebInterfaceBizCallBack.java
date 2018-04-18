package com.sss.framework.Dao;

import org.json.JSONObject;

/**
 * Created by leilei on 2018/2/22.
 */

public interface WebInterfaceBizCallBack {

     void onError(String msg);

     void onResponse(String response, int id) ;
}
