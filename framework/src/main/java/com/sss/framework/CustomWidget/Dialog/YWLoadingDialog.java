package com.sss.framework.CustomWidget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sss.framework.R;


/**
 * 加载dialog
 *
 * @author Driver
 * @version V1.0
 * @Date 2015-04-01
 */
public class YWLoadingDialog extends Dialog {
    private ImageView iv_load_result;// 加载的结果图标显示
    private TextView tv_load;// 加载的文字展示
    private ProgressBar pb_loading;// 加载中的图片

    public YWLoadingDialog(Context context) {
        super(context, R.style.YWLoadingDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commom_loading_layout);
        setCanceledOnTouchOutside(false);//防止触碰到阴影地方关闭
//        iv_load_result = (ImageView) findViewById(R.id.iv_load_result);
        tv_load = (TextView) findViewById(R.id.tv_load);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
    }

    // 加载成功
    public void dimissSuc(String str) {
        if (pb_loading != null) {
            pb_loading.setVisibility(View.GONE);
        }
        if (tv_load != null) {
            tv_load.setText(str);
        }
        if (iv_load_result != null) {
            iv_load_result.setVisibility(View.VISIBLE);
            iv_load_result.setImageResource(R.drawable.load_suc_icon);
        }
        dismiss();
    }

    // 加载失败
    public void dimissFail(String str) {
        if (pb_loading != null) {
            pb_loading.setVisibility(View.GONE);
        }
        if (tv_load != null) {
            tv_load.setText(str);
        }
        if (iv_load_result != null) {
            iv_load_result.setVisibility(View.VISIBLE);
            iv_load_result.setImageResource(R.drawable.load_fail_icon);
        }
        dismiss();
    }

    //立即关闭
    public void disMiss() {
        dismiss();
        iv_load_result = null;
        tv_load = null;
        pb_loading = null;
    }

    //立即设置标题
    public void setTitle(String str) {
        if (tv_load != null) {
            tv_load.setText(str);
        }

    }
}

