package com.sss.framework.Library.HttpRequestLib.constant;


/**
 * 本类负责储存错误代码，对应的错误提示语句在ErrorTipConstant中
 * Created by 61642 on 2018/4/16.
 */

public class ErrorCodeConstant {
    /**
     * 协议、格式错误
     */
    public static int MalformedURLException = -1;

    /**
     * IO错误
     */
    public static int IOException = -2;
    /**
     * 编码异常
     */
    public static int UnsupportedEncodingException = -3;

    /**
     * 协议出现错误
     */
    public static int ProtocolException = -4;

    /**
     * 请求地址错误
     */
    public static int ErrorUrl = -5;
    /**
     * 上传的文件数量为空
     */
    public static int NoFiles = -6;
}
