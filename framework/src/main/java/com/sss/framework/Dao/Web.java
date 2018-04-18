package com.sss.framework.Dao;

import android.content.Context;


import com.sss.framework.Library.OKHttp.OkHttpUtils;
import com.sss.framework.Library.OKHttp.builder.PostFormBuilder;
import com.sss.framework.Library.OKHttp.callback.StringCallback;
import com.sss.framework.Utils.AppUtils;
import com.sss.framework.Utils.ConvertUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

import static android.R.id.list;


/**
 * Created by leilei on 2017/8/8.
 */

public class Web {
    /**
     * 请求APP许可证
     * http://apply.nx.021dr.cn/admin/login/index.html
     *
     * @param localClassName
     * @param context
     * @param app_id                  服务器数据库中记录的APPID
     * @param stringCallback
     * @throws JSONException
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Call requestAPPLicense(String localClassName, Context context, String app_id, StringCallback stringCallback) throws JSONException, NoSuchAlgorithmException {
        return requestByString(localClassName,"http://apply.nx.021dr.cn/index.php/Api/Index/into"
                , new JSONObject()
                        .put("app_id", app_id)
                        .put("app_name", AppUtils.getAppName(context))
                        .put("app_key", ConvertUtils.md5Add(app_id + AppUtils.getAppName(context)))
                        .toString()
                , "请求APP许可"
                , stringCallback);
    }
    /**
     * 网络请求(String)
     *
     * @param localClassName
     * @param url
     * @param send
     * @param meaning
     * @param stringCallback
     * @return
     */
    public static Call requestByString(String localClassName,String url, String send, String meaning, StringCallback stringCallback) {
        return OkHttpUtils
                .getInstance()
                .setPrintLogEnable(true)
                .postString()
                .tag(localClassName)
                .url(url)
                .content(send)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .connTimeOut(30000)
                .writeTimeOut(30000)
                .readTimeOut(30000)
                .execute(meaning, send, stringCallback);
    }


    /**
     * 上传文件
     * @param key
     * @param files
     * @param localClassName
     * @param url
     * @param send
     * @param meaning
     * @param stringCallback
     */
    public static void  uploadFiles(String key, Map<String, File> files, String localClassName, String url, String send, String meaning, StringCallback stringCallback){
        OkHttpUtils.post()
                .files(key,files)
                .url(url)
                .params(null)
                .headers(null)
                .tag(localClassName)
                .build()
                .connTimeOut(60000)
                .writeTimeOut(60000)
                .readTimeOut(60000)
                .execute(meaning, send, stringCallback);
    }





}
