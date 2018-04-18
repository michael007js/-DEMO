package com.sss.framework.Library.HttpRequestLib.dao;


import java.util.List;
import java.util.Map;

/**
 * 处理请求
 * Created by 61642 on 2018/4/16.
 */

public interface IHttpService {

    /**
     * 设置超时时间
     * @param time
     */
    void setTimeOut(long time);

    /**
     * 设置请求地址
     *
     * @param url
     */
    void setUrl(String url);

    /**
     * 设置请求数据
     *
     * @param requestData
     */
    void setRequestData(String requestData);

    /**
     * 设置请求数据
     *
     * @param requestData
     */
    void setRequestData(Map<String, String> requestData);

    /**
     * 提交请求
     */
    void execute(int httpRequestType);

    /**
     * 设置数据处理回调监听
     *
     * @param iHttpListener
     */
    void setIHttpListener(IHttpListener iHttpListener);

    /**
     * 设置要上传的文件的路径
     * @param uploadFilePaths
     */
    void setUploadFilePaths(List<String>uploadFilePaths);

    /**
     * 设置文件上传回调监听
     * @param iFileUploadCallBack
     */
    void setIFileUploadCallBack(IFileUploadCallBack iFileUploadCallBack);
}
