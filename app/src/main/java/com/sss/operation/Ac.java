package com.sss.operation;

import android.os.Bundle;
import android.widget.TextView;

import com.sss.framework.Activity.BaseActivity;
import com.sss.framework.CustomWidget.Layout.LayoutLoading.LoadingLayout;

import java.util.List;

import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by leilei on 2018/3/26.
 */

public class Ac extends BaseActivity{
    @Override
    protected List<PermissionItem> permissionItems() {
        return null;
    }

    @Override
    protected PermissionCallback permissionCallback() {
        return null;
    }

    @Override
    protected void onCreate() {

    }

    @Override
    protected void cacheData() {

    }

    @Override
    protected void destroy() {

    }

    @Override
    protected void uiHidden() {

    }

    @Override
    protected void memoryWarningYellow() {

    }

    @Override
    protected void memoryWarningOrange() {

    }

    @Override
    protected void memoryWarningRed() {

    }

    @Override
    protected void setReLoadingCallBack(LoadingLayout loading) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView=new TextView(getBaseActivity());
        textView.setText("wei  zhu  ce");
        setContentView(textView);
    }
}
