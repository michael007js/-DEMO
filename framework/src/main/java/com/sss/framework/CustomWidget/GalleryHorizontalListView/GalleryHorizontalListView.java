package com.sss.framework.CustomWidget.GalleryHorizontalListView;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.framework.Application.FrameworkApplication;
import com.sss.framework.CustomWidget.GalleryHorizontalListView.View.ItemPic;
import com.sss.framework.CustomWidget.ListView.HorizontalListViewProfession;
import com.sss.framework.Library.Fresco.FrescoUtils;
import com.sss.framework.Library.SSSAdapter.SSS_Adapter;
import com.sss.framework.Library.SSSAdapter.SSS_HolderHelper;
import com.sss.framework.R;
import com.sss.framework.Utils.APPOftenUtils;
import com.sss.framework.Utils.AppUtils;
import com.sss.framework.Utils.SizeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;



/*
step:
 List<String >temp=new ArrayList<>();
        temp.add(GalleryHorizontalListView.Holder);
        picture.setList(temp);
        picture.setHigth(getBaseActivityContext(), 70);
        picture.setWidth(getBaseActivityContext(), 70);
        picture.setOnGalleryHorizontalListViewCallBack(this);
* */


/**
 * 带关闭按钮的横向滑动照片浏览器
 * Created by leilei on 2018/1/3.
 */

@SuppressWarnings("ALL")
public class GalleryHorizontalListView extends HorizontalListViewProfession {
    public static String Holder = "HOLDER";//占位符
    private List<String> list = new ArrayList<>();//选中的图片列表
    private SSS_Adapter sss_adapter;//适配器
    private int width, higth; //图片宽高
    private float radius = 0f;//圆角弧度
    private boolean canOperation = true;//是否允许操作
    private boolean showClose=true;//是否显示XX按钮
    private List<String> temp = new ArrayList<>();//临时图片列表（点击图片时临时筛选保存的图片集合）
    private OnGalleryHorizontalListViewCallBack onGalleryHorizontalListViewCallBack;

    public void clear(){
        Holder=null;
        if (list!=null){
            list.clear();
        }
        list=null;
        if (sss_adapter!=null){
            sss_adapter.clear();
        }
        sss_adapter=null;
        if (temp!=null){
            temp.clear();
        }
        temp=null;onGalleryHorizontalListViewCallBack=null;
    }

    public void setOnGalleryHorizontalListViewCallBack(OnGalleryHorizontalListViewCallBack onGalleryHorizontalListViewCallBack) {
        this.onGalleryHorizontalListViewCallBack = onGalleryHorizontalListViewCallBack;
    }
    List<String> tempBackList=new ArrayList<>();
    public List<String> getList() {
        tempBackList.clear();
        for (int i = 0; i < list.size(); i++) {
            if (!Holder.equals(list.get(i))) {
                tempBackList.add(list.get(i));
            }
        }
        return tempBackList;
    }

    /**
     * 是否允许操作
     * @param canOperation
     */
    public void setCanOperation(boolean canOperation) {
        this.canOperation = canOperation;
    }

    /**
     * 图片宽度
     *
     * @param width
     */
    public void setWidth(Context context, int width) {
        this.width = SizeUtils.dp2px(context, width);
    }

    /**
     * 图片高度
     *
     * @param higth
     */
    public void setHigth(Context context, int higth) {
        this.higth = SizeUtils.dp2px(context, higth);
    }

    /**
     * 如果设大，则出来的效果是圆形图片
     *
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    public GalleryHorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        width = SizeUtils.dp2px(context, 60);
        higth = SizeUtils.dp2px(context, 60);
        sss_adapter = new SSS_Adapter<String>(context, R.layout.gallery_horizontal_listview) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, final String bean, SSS_Adapter instance) {
                helper.getView(R.id.ItemPic).setLayoutParams(new LinearLayout.LayoutParams(width, higth));
                showImage(bean, ((ItemPic) helper.getView(R.id.ItemPic)).setPosition(position)
                        .hideClose(Holder.equals(bean))
                        .setPosition(position)
                        .setShowClose(showClose)
                        .setUrl(bean)
                        .setOnItemPicClick(new ItemPic.OnItemPicClick() {
                            @Override
                            public void onClickItemPic(String url, SimpleDraweeView pic, final int position) {
                                if (canOperation) {
                                    if (Holder.equals(url)) {
                                        APPOftenUtils.createPhotoChooseDialog(0, 9, context, FrameworkApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
                                            @Override
                                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                                if (resultList == null || resultList.size() == 0) {
                                                    return;
                                                }
                                                for (int i = 0; i < resultList.size(); i++) {
                                                    list.add(resultList.get(i).getPhotoPath());
                                                }
                                                sss_adapter.setList(list);
                                            }

                                            @Override
                                            public void onHanlderFailure(int requestCode, String errorMsg) {

                                            }
                                        });
                                    } else {
                                        if (onGalleryHorizontalListViewCallBack != null) {
                                            temp.clear();
                                            int a = 0;
                                            for (int i = 0; i < list.size(); i++) {
                                                if (!Holder.equals(list.get(i))) {
                                                    temp.add(list.get(i));
                                                } else {
                                                    a++;
                                                }
                                            }
                                            onGalleryHorizontalListViewCallBack.onClickImage(pic, position , temp);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onClose(String url, int position) {
                                if (canOperation) {
                                    if (onGalleryHorizontalListViewCallBack != null) {
                                        list.remove(position);
                                        sss_adapter.setList(list);
                                    }
                                }
                            }
                        })
                        .getPic(), context);
            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }


        };

        this.setAdapter(sss_adapter);
    }


    /**
     * 设置数据集合
     *
     * @param list
     */
    public void setList(List<String> list) {
        this.list = list;
        sss_adapter.setList(list);
    }

    private SimpleDraweeView showImage(String url, SimpleDraweeView simpleDraweeView, Context context) {
        if (url.startsWith("/storage/")) {
            return FrescoUtils.showImage(false, width * 2, higth * 2, Uri.fromFile(new File(url)), simpleDraweeView, radius);
        } else if (url.startsWith("http")) {
            return FrescoUtils.showImage(false, width * 2, higth * 2, Uri.parse(url), simpleDraweeView, radius);
        } else {
            return FrescoUtils.showImage(false, width * 2, higth * 2, Uri.parse("res://" + AppUtils.getAppPackageName(context) + "/" + R.mipmap.photo_select_add_image), simpleDraweeView, radius);
        }
    }

    public interface OnGalleryHorizontalListViewCallBack {
        void onClickImage(SimpleDraweeView simpleDraweeView, int position, List<String> list);
    }
}
