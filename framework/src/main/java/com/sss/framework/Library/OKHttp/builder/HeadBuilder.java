package com.sss.framework.Library.OKHttp.builder;


import com.sss.framework.Library.OKHttp.OkHttpUtils;
import com.sss.framework.Library.OKHttp.request.OtherRequest;
import com.sss.framework.Library.OKHttp.request.RequestCall;


/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
