package com.sss.operation;

import android.view.View;

import com.baidu.mapapi.map.MapView;
import com.sss.framework.Activity.BaseActivity;
import com.sss.framework.CustomWidget.Layout.LayoutLoading.LoadingLayout;
import com.sss.framework.CustomWidget.ScollView.BounceZoomScrollView;
import com.sss.framework.CustomWidget.TextView.Marqueen.SimpleMF;
import com.sss.framework.CustomWidget.TextView.Marqueen.SimpleMarqueeView;
import com.sss.framework.CustomWidget.TextView.Marqueen.util.OnItemClickListener;
import com.sss.framework.Library.Log.LogUtils;
import com.sss.framework.Model.TargetInfoModel;
import com.sss.framework.Utils.PermissionUtils;
import com.sss.framework.Utils.ToastUtils;
import com.sss.operation.BaiDuMap.BaiDuUtils;
import com.sss.operation.BaiDuMap.LocationBaiduClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class MainActivity extends BaseActivity {
    TargetInfoModel targetInfoModel = new TargetInfoModel();
    LocationBaiduClient locationBaiduClient;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.scrollview)
    BounceZoomScrollView scrollview;
    @BindView(R.id.marqueeView)
    SimpleMarqueeView marqueeView;

    @Override
    protected List<PermissionItem> permissionItems() {
        List<PermissionItem> list = new ArrayList<>();
        list.add(new PermissionItem(PermissionUtils.ACCESS_FINE_LOCATION, "获取精确位置", R.mipmap.ic_launcher));
        list.add(new PermissionItem(PermissionUtils.ACCESS_COARSE_LOCATION, "获取粗略位置", R.mipmap.ic_launcher));
        list.add(new PermissionItem(PermissionUtils.ACCESS_NETWORK_STATE, "获取设备网络状态", R.mipmap.ic_launcher));
        list.add(new PermissionItem(PermissionUtils.INTERNET, "网络权限", R.mipmap.ic_launcher));
        list.add(new PermissionItem(PermissionUtils.READ_PHONE_STATE, "读取硬件", R.mipmap.ic_launcher));
        list.add(new PermissionItem(PermissionUtils.ACCESS_WIFI_STATE, "设备的WIFI状态", R.mipmap.ic_launcher));
        list.add(new PermissionItem(PermissionUtils.WRITE_EXTERNAL_STORAGE, "设备读写", R.mipmap.ic_launcher));
//        list.add(new PermissionItem(PermissionUtils.WRITE_SETTINGS,"获取统计数据",R.mipmap.ic_launcher));
        list.add(new PermissionItem(PermissionUtils.CAMERA, "配置Camera权限", R.mipmap.ic_launcher));
        return list;
    }


    @Override
    protected PermissionCallback permissionCallback() {
        return new PermissionCallback() {
            @Override
            public void onClose() {
                ToastUtils.showShortToast(getBaseActivity(), "您未授权必要的权限");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onDeny(String permission, int position) {
                ToastUtils.showShortToast(getBaseActivity(), "您未授权必要的权限");
            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        };
    }

    @Override
    protected void onCreate() {
        BaiDuUtils.init(getBaseActivity());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BaiDuUtils.setOnTouchListener(map, scrollview);
        init();
        final List<String> datas = Arrays.asList("《赋得古原草送别》", "离离原上草，一岁一枯荣。", "野火烧不尽，春风吹又生。", "远芳侵古道，晴翠接荒城。", "又送王孙去，萋萋满别情。", "测试测试测试测试测试测试测试测试测试测试测试");

        SimpleMF<String> marqueeFactory = new SimpleMF(MainActivity.this);
        marqueeFactory.setData(datas);
        marqueeView.setMarqueeFactory(marqueeFactory);
        marqueeView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View mView, Object mData, int mPosition) {
                ToastUtils.showShortToast(getBaseActivity(),""+mData);
            }
        });
        scrollview.setOnBounceZoomScrollViewCallBack(new BounceZoomScrollView.OnBounceZoomScrollViewCallBack() {
            @Override
            public void onScrollX(int x, boolean isScaling, boolean upDownSlide, boolean isToTop) {

            }

            @Override
            public void onScrollY(int y, boolean isScaling, boolean upDownSlide, boolean isToBottom) {

            }
        });
//        AMSHookUtil.hookStartActivity(this);
//        startActivity(new Intent(getBaseActivity(),Ac.class));
    }


    @Override
    protected void cacheData() {

    }

    @Override
    protected void destroy() {
        map.onDestroy();
        locationBaiduClient.stop();
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
    protected void onResume() {
        super.onResume();
        map.onResume();
        locationBaiduClient.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        locationBaiduClient.stop();
    }


    private void init() {
        BaiDuUtils.initUI(map);
        locationBaiduClient = new LocationBaiduClient(getBaseActivity(), 3000, false, BaiDuUtils.initLocationListerer(targetInfoModel, new BaiDuUtils.OnLocationCallBack() {
            @Override
            public void onLocationSuccess(TargetInfoModel targetInfoModel) {
                LogUtils.e(targetInfoModel.toString());
                BaiDuUtils.locationTarget(targetInfoModel.getLatitude(), targetInfoModel.getLongitude(), map, 17f);
                BaiDuUtils.showTargetMarker(map, targetInfoModel.getLatitude(), targetInfoModel.getLongitude(), getBaseContext(), R.mipmap.ic_launcher);
            }

            @Override
            public void onLocationFail(String errorInfo) {
                LogUtils.e(errorInfo);
            }
        }));
        locationBaiduClient.start();
    }

}
