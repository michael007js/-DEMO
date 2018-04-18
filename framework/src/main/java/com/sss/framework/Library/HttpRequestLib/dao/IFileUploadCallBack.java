package com.sss.framework.Library.HttpRequestLib.dao;

import java.util.List;

/**
 * Created by leilei on 2018/4/18.
 */

public interface IFileUploadCallBack {

    void onUpLoadCallBack(int position,String fileName,List<String> files);
}
