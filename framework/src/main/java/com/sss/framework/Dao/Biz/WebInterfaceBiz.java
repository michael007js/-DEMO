package com.sss.framework.Dao.Biz;

import com.sss.framework.Dao.Web;
import com.sss.framework.Dao.WebInterfaceBizCallBack;
import com.sss.framework.Library.OKHttp.callback.StringCallback;
import com.sss.framework.Utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by leilei on 2018/2/22.
 */

public class WebInterfaceBiz {
    public static Call WebInterfaceBiz(String localClassName, String url, String send, final String meaning, final WebInterfaceBizCallBack webInterfaceBizCallBack) {
        return Web.requestByString(localClassName, url, send, meaning, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (webInterfaceBizCallBack != null) {
                    webInterfaceBizCallBack.onError(e.getLocalizedMessage());
                }
                if (!call.isCanceled()){
                    call.cancel();
                }
            }

            @Override
            public void onResponse(String response, int id) {
               if (StringUtils.isEmpty(response)){
                   if (webInterfaceBizCallBack != null) {
                       webInterfaceBizCallBack.onError("服务器返回错误！");
                   }
               }else {
                   if (webInterfaceBizCallBack != null) {
                       webInterfaceBizCallBack.onResponse(response,id);
                   }

               }
            }
        });
    }

}
