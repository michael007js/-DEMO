package com.sss.framework.Activity;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.sss.framework.Adapater.AutoAdapter.AdapterVirtualButton;
import com.sss.framework.Adapater.AutoAdapter.AutoUtils;
import com.sss.framework.Bar.Immersion.Eyes;
import com.sss.framework.BroadcastReceiver.NetWorkStateReceiver;
import com.sss.framework.CustomWidget.Dialog.YWLoadingDialog;
import com.sss.framework.CustomWidget.ImageView.FrescoGif;
import com.sss.framework.CustomWidget.Layout.LayoutLoading.LoadingLayout;
import com.sss.framework.CustomWidget.Layout.LayoutSwipeback.lib.app.SwipeBackActivity;
import com.sss.framework.CustomWidget.Loading.BaiduloadingView;
import com.sss.framework.CustomWidget.Loading.Windows8LoadingView;
import com.sss.framework.Dao.OnNetStatusChangedListener;
import com.sss.framework.Library.Fresco.FrescoUtils;
import com.sss.framework.Library.Glid.GlidUtils;
import com.sss.framework.Library.OKHttp.OkHttpUtils;
import com.sss.framework.R;
import com.sss.framework.Utils.ActivityManagerUtils;
import com.sss.framework.Utils.AppUtils;
import com.sss.framework.Utils.KeyboardUtils;
import com.sss.framework.Utils.PermissionUtils;
import com.sss.framework.Utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by 61642 on 2017/11/23.
 */

@SuppressWarnings("ALL")
public abstract class BaseActivity extends SwipeBackActivity {
    private boolean isRegisterEventBus = false;
    private List<ImageView> imageViewList = new ArrayList<>();
    public WeakReference<BaseActivity> weakReferenceActivity;
    private YWLoadingDialog ywLoadingDialog;
    private OnNetStatusChangedListener onNetStatusChangedListener;
    public String tag = "";
    private NetWorkStateReceiver netWorkStateReceiver;
    private boolean isRegisterNetListener = false;

    public BaseActivity getBaseActivity() {
        if (weakReferenceActivity != null) {
            return weakReferenceActivity.get();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        super.onCreate(savedInstanceState);
        tag = this.getLocalClassName();
        weakReferenceActivity = new WeakReference<>(this);
        ActivityManagerUtils.getActivityManager().addActivity(this);
        /*调整尺寸*/
        AutoUtils.setSize(this, true);
        PermissionUtils.requestRunTimePermission(getBaseActivity(), permissionItems(), permissionCallback());
        onCreate();
    }

    /**
     * 权限集合 String permission, String permissionName, int permissionIconRes
     * @return
     */
    abstract protected List<PermissionItem> permissionItems();

    /**
     * 权限回调
     * @return
     */
    abstract protected PermissionCallback permissionCallback();


    abstract protected void onCreate();

    /**
     * 设置网络监听
     */
    public void setOnNetStatusChangedListener(OnNetStatusChangedListener onNetStatusChangedListener) {
        isRegisterNetListener=true;
        if (netWorkStateReceiver != null) {
            unregisterReceiver(netWorkStateReceiver);
        }
        registerNetListener(onNetStatusChangedListener);
    }


    /**
     * 注册网络监听广播
     */
    private void registerNetListener(OnNetStatusChangedListener onNetStatusChangedListener) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        netWorkStateReceiver.setOnNetStatusChangedListener(onNetStatusChangedListener);
    }

    /**
     * 设置loading页面（如果有这项业务需求的话）
     *
     * @param loading
     * @param res
     */
    public void setLoading(final LoadingLayout loading, int res) {
        loading.setLoadingPage(new FrescoGif(getBaseActivity()).setGif(Uri.parse("res://" + AppUtils.getAppPackageName(getBaseActivity()) + "/" + res)));
        loading.setStatus(LoadingLayout.Loading);
        loading.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loading.setStatus(LoadingLayout.Loading);
                setReLoadingCallBack(loading);
            }
        });
        loading.setStatus(LoadingLayout.Loading);
    }

    /**
     * 设置loading页面（如果有这项业务需求的话）
     *
     * @param loading
     * @param res
     */
    public void setLoading(final LoadingLayout loading) {
        loading.setLoadingPage(new Windows8LoadingView(getBaseActivity()));
        loading.setStatus(LoadingLayout.Loading);
        loading.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loading.setStatus(LoadingLayout.Loading);
                setReLoadingCallBack(loading);
            }
        });
        loading.setStatus(LoadingLayout.Loading);
    }



    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("注册");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRegisterNetListener) {
            if (netWorkStateReceiver != null) {
                unregisterReceiver(netWorkStateReceiver);
            }
            registerNetListener(onNetStatusChangedListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterNetListener) {
            if (netWorkStateReceiver != null) {
                unregisterReceiver(netWorkStateReceiver);
            }
            onNetStatusChangedListener = null;
        }
        cacheData();
        setContentView(R.layout.null_view);
        destroy();
        if (weakReferenceActivity != null) {
            weakReferenceActivity.clear();
        }
        weakReferenceActivity = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        OkHttpUtils.getInstance().cancelTag(tag);
        FrescoUtils.clearCache();
        clearImageViewList();
        ActivityManagerUtils.getActivityManager().finishActivity(this);
        Runtime.getRuntime().gc();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                /**
                 * 回调只有当我们程序中的所有UI组件全部不可见的时候才会触发，
                 * 这和onStop()方法还是有很大区别的，
                 * 因为onStop()方法只是当一个Activity完全不可见的时候就会调用，
                 * 比如说用户打开了我们程序中的另一个Activity。因此，
                 * 我们可以在onStop()方法中去释放一些Activity相关的资源，
                 * 比如说取消网络连接或者注销广播接收器等，
                 * 但是像UI相关的资源应该一直要等到onTrimMemory(uiHidden)这个回调之后才去释放，
                 * 这样可以保证如果用户只是从我们程序的一个Activity回到了另外一个Activity，
                 * 界面相关的资源都不需要重新加载，
                 * 从而提升响应速度。
                 * */

                uiHidden();
                break;
            case TRIM_MEMORY_RUNNING_MODERATE:
                /**
                 * 表示应用程序正常运行，
                 * 并且不会被杀掉。
                 * 但是目前手机的内存已经有点低了，
                 * 系统可能会开始根据LRU缓存规则来去杀死进程了。
                 * */

                trimMemoryRunningModerate();
                break;
            case TRIM_MEMORY_RUNNING_LOW:
                /**
                 * 表示应用程序正常运行，
                 * 并且不会被杀掉。
                 * 但是目前手机的内存已经非常低了，
                 * 我们应该去释放掉一些不必要的资源以提升系统的性能，
                 * 同时这也会直接影响到我们应用程序的性能。
                 * */

                trimMemoryRunningLow();
                break;

            case TRIM_MEMORY_RUNNING_CRITICAL:
                /**
                 * 表示应用程序仍然正常运行，
                 * 但是系统已经根据LRU缓存规则杀掉了大部分缓存的进程了。
                 * 这个时候我们应当尽可能地去释放任何不必要的资源，
                 * 不然的话系统可能会继续杀掉所有缓存中的进程，
                 * 并且开始杀掉一些本来应当保持运行的进程，
                 * 比如说后台运行的服务。
                 * */
                trimMemoryRunningCritical();
                break;
            case TRIM_MEMORY_BACKGROUND:
                /**
                 *  表示手机目前内存已经很低了，
                 *  系统准备开始根据LRU缓存来清理进程。
                 *  这个时候我们的程序在LRU缓存列表的最近位置，
                 *  是不太可能被清理掉的，
                 *  但这时去释放掉一些比较容易恢复的资源能够让手机的内存变得比较充足，
                 *  从而让我们的程序更长时间地保留在缓存当中，
                 *  这样当用户返回我们的程序时会感觉非常顺畅，
                 *  而不是经历了一次重新启动的过程。
                 * */
                memoryWarningYellow();
                break;
            case TRIM_MEMORY_MODERATE:
                /**
                 * 表示手机目前内存已经很低了，
                 * 并且我们的程序处于LRU缓存列表的中间位置，
                 * 如果手机内存还得不到进一步释放的话，
                 * 那么我们的程序就有被系统杀掉的风险了。
                 * */
                memoryWarningOrange();
                break;
            case TRIM_MEMORY_COMPLETE:
                /**
                 * 表示手机目前内存已经很低了，
                 * 并且我们的程序处于LRU缓存列表的最边缘位置，
                 * 系统会最优先考虑杀掉我们的应用程序，
                 * 在这个时候应当尽可能地把一切可以释放的东西都进行释放。
                 * */
                memoryWarningRed();
                break;
        }
    }

    /**
     * 自定义
     *
     * @param view
     * @param isSwipeBack        是否侧滑返回
     * @param isRegisterEventBus 是否注册eventBus
     * @param isHideKeyBroad     是否点击其他位置隐藏键盘
     * @param statusColor        状态栏颜色 R.color.mainColor -1为不设置
     */
    public void customInit(View view, boolean isSwipeBack, boolean isRegisterEventBus, boolean isHideKeyBroad, int statusColor) {
        this.isRegisterEventBus = isRegisterEventBus;
        setSwipeBackEnable(isSwipeBack);
        /*点击edittext之外的地方隐藏键盘*/
        if (isHideKeyBroad) {
            KeyboardUtils.wrap(this);
        }

        if (this.isRegisterEventBus) {
            EventBus.getDefault().register(this);
        }
        if (-1 != statusColor) {
            Eyes.setStatusBarLightMode(getBaseActivity(), ContextCompat.getColor(getBaseContext(), statusColor));
        }
        if (view != null) {
           /*适配虚拟按键                android:fitsSystemWindows="true"  */
            AdapterVirtualButton.assistActivity(view, this);
        }
    }

    /**
     * 状态栏颜色
     *
     * @param isLight     状态栏文字是否高亮（白色）
     * @param statusColor 状态栏颜色 R.color.mainColor -1为不设置
     */
    public void customStatusBarColor(boolean isLight, int statusColor) {
        if (-1 != statusColor) {
            if (isLight) {
                Eyes.setStatusBarColor(getBaseActivity(), ContextCompat.getColor(getBaseContext(), statusColor));

            } else {
                Eyes.setStatusBarLightMode(getBaseActivity(), ContextCompat.getColor(getBaseContext(), statusColor));
            }
        }
    }

    /**
     * 关闭等待弹窗
     *
     * @param listener
     */
    public void dissMissLoading(DialogInterface.OnDismissListener listener) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.setOnDismissListener(listener);
            ywLoadingDialog.dismiss();
        }
    }

    /**
     * 显示等待弹窗
     *
     * @param msg 提示
     */
    public void showLoading(String msg) {
        if (ywLoadingDialog == null) {
            if (weakReferenceActivity != null) {
                if (weakReferenceActivity.get() != null) {
                    ywLoadingDialog = new YWLoadingDialog(weakReferenceActivity.get());
                    ywLoadingDialog.show();
                }
            }
        } else {
            ywLoadingDialog.dismiss();
            ywLoadingDialog.show();
            if (!StringUtils.isEmpty(msg)) {
                ywLoadingDialog.setTitle(msg);
            }
        }

    }

    /**
     * 保存图片引用
     *
     * @param imageView
     */
    public void addImageViewList(ImageView imageView) {
        if (imageViewList != null) {
            imageViewList.add(imageView);
        }
    }


    /**
     * 清除图片引用
     */
    public void clearImageViewList() {
        if (imageViewList != null) {
            for (int i = 0; i < imageViewList.size(); i++) {
                if (imageViewList.get(i) != null) {
                    GlidUtils.clearImg(imageViewList.get(i));
                    imageViewList.set(i, null);
                }
            }
            if (imageViewList != null) {
                imageViewList.clear();
            }
            imageViewList = null;
        }

    }

    /**
     * 缓存（在OnDestroy中调用）
     */
    protected abstract void cacheData();

    /**
     * 生命周期销毁
     */
    protected abstract void destroy();

    /**
     * UI不可见时回调
     */
    protected abstract void uiHidden();

    protected void trimMemoryRunningModerate() {
    }

    protected void trimMemoryRunningLow() {
    }

    protected void trimMemoryRunningCritical() {

    }

    /**
     * 内存过低通知
     */
    protected abstract void memoryWarningYellow();

    /**
     * 内存过低警告
     */
    protected abstract void memoryWarningOrange();

    /**
     * 内存过低严重警告
     */
    protected abstract void memoryWarningRed();

    /**
     * 重载回调
     */
    protected abstract void setReLoadingCallBack(LoadingLayout loading);


}
