package com.sss.framework.Library.Update.listener;


import com.sss.framework.Library.Update.pojo.UpdateInfo;

/**
 * Created by Shelwee on 14-5-16.
 */
public interface OnUpdateListener {
    /**
     * on start check
     */
    public void onStartCheck();

    /**
     * on finish check
     */
    public void onFinishCheck(UpdateInfo info);

    public void onStartDownload();
    
    public void onDownloading(int progress);
    
    public void onFinshDownload();

}
