package com.sss.framework.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sss.framework.CustomWidget.Dialog.YWLoadingDialog;
import com.sss.framework.CustomWidget.ImageView.FrescoGif;
import com.sss.framework.CustomWidget.Layout.LayoutLoading.LoadingLayout;
import com.sss.framework.CustomWidget.Loading.BaiduloadingView;
import com.sss.framework.CustomWidget.Loading.Windows8LoadingView;
import com.sss.framework.Library.Fresco.FrescoUtils;
import com.sss.framework.Library.Glid.GlidUtils;
import com.sss.framework.Library.OKHttp.OkHttpUtils;
import com.sss.framework.Utils.AppUtils;
import com.sss.framework.Utils.PermissionUtils;
import com.sss.framework.Utils.StringUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Fragment预加载问题的解决方案：
 * 1.可以懒加载的Fragment
 * 2.切换到其他页面时停止加载数据（可选）
 * Created by leilei on 2017/7/27.
 */

@SuppressWarnings("ALL")
public abstract class BaseFragment extends Fragment {
    /**
     * 视图是否已经初初始化
     */

    public boolean isInit = false;
    public boolean isLoad = false;
    public boolean isVisibleToUser = false;
   private List<ImageView> imageViewList = new ArrayList<>();
    private WeakReference<Context> weakReference;
    private WeakReference<BaseFragment> weakReferenceBaseFragment;
    private View view;
    private  YWLoadingDialog ywLoadingDialog;
    public String tag="";

    public Context getBaseFragmentActivityContext() {
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public BaseFragment getBaseFragment() {
        if (weakReferenceBaseFragment != null) {
            return weakReferenceBaseFragment.get();
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        weakReference = new WeakReference(getActivity());
        weakReferenceBaseFragment = new WeakReference<>(this);
        view= inflater.inflate(setContentView(), container, false);
        isInit = true;
        /**初始化的时候去加载数据**/
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tag=this.getBaseFragment().toString();
        isCanLoadData();
    }

    /**
     * 设置loading页面（如果有这项业务需求的话）
     * @param loading
     * @param res
     */
    public void setLoading(final LoadingLayout loading, int res ){
        loading.setLoadingPage(new FrescoGif(getBaseFragmentActivityContext()).setGif(Uri.parse("res://"+ AppUtils.getAppPackageName(getBaseFragmentActivityContext())+"/"+res)));
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
     * @param loading
     * @param res
     */
    public void setLoading(final LoadingLayout loading ){
        loading.setLoadingPage(new Windows8LoadingView(getBaseFragmentActivityContext()));
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
     * 请求权限
     * @param permissionItems  String permission, String permissionName, int permissionIconRes
     * @param callback
     */
    public void requestRunTimePermission(List<PermissionItem> permissionItems, PermissionCallback callback){
        PermissionUtils.requestRunTimePermission(getBaseFragmentActivityContext(), permissionItems,  callback);
    }


    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        isCanLoadData();
        onFragmentVisibleChange(isVisibleToUser);
    }

    /**
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopLoad();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            if (isAdded()){
              if (!isLoad){
                  new Thread(){
                      @Override
                      public void run() {
                          super.run();
                          try {
                              sleep(1000);
                              getActivity().runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      lazyLoad();
                                      isLoad = true;
                                  }
                              });
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  }.start();
              }

            }
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
    }


    @Override
    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(tag);
        cacheData();
        tag=null;
        try {
            super.onDestroy();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            clear();
        }
        destroy();

    }

    void clear() {
        if (ywLoadingDialog!=null){
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog=null;
        if (weakReferenceBaseFragment != null) {
            weakReferenceBaseFragment.clear();
        }
        weakReferenceBaseFragment = null;
        if (weakReference != null) {
            weakReference.clear();
        }
        weakReference = null;

        FrescoUtils.clearCache();
        clearImageViewList();
        Runtime.getRuntime().gc();
    }


    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract int setContentView();

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }

    /**
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected abstract void stopLoad();


    /**************************************************************
     *  自定义的回调方法，子类可根据需求重写
     *************************************************************/

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
        visibleChange(isVisible);
    }



    /**
     * 关闭等待弹窗
     * @param listener
     */
    public void dissMissLoading(DialogInterface.OnDismissListener listener){
        if (ywLoadingDialog!=null){
            ywLoadingDialog.setOnDismissListener(listener);
            ywLoadingDialog.dismiss();
        }
    }

    /**
     * 显示等待弹窗
     * @param msg 提示
     */
    public void showLoading(String msg){
        if (ywLoadingDialog==null){
            if (weakReference!=null){
                if (weakReference.get()!=null){
                    ywLoadingDialog=new YWLoadingDialog(weakReference.get());
                }
            }
        }else {
            ywLoadingDialog.dismiss();
            ywLoadingDialog.show();
            if (!StringUtils.isEmpty(msg)){
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

    protected abstract void visibleChange(boolean isVisible);

    /**
     * 缓存（在OnDestroy中调用）
     */
    protected abstract void cacheData();

    /**
     * 生命周期销毁
     */
    protected abstract void destroy();

    /**
     * 重载回调
     */
    protected abstract void setReLoadingCallBack(LoadingLayout loading);

}
