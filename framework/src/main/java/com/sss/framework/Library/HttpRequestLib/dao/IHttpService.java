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
     * @param url 请求地址
     */
    void setUrl(String url);

    /**
     * 设置请求数据
     *
     * @param requestData 字符串请求参数
     */
    void setRequestData(String requestData);

    /**
     * 设置请求数据(键值对)
     *
     * @param requestData 键值对请求参数
     */
    void setRequestData(Map<String, String> requestData);

    /**
     * 提交请求
     * @param httpRequestType 请求类型（详情参见HttpRequestType）
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
