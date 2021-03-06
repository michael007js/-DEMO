package com.sss.framework.Library.Fresco;

/**
 * Created by leilei on 2017/5/27.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import cn.finalteam.galleryfinal.widget.GFImageView;


/**
 * 配合GalleryFinal实现照片墙选择器
 * Desction:fresco image loader
 * Author:pengjianbo
 * Date:15/12/24 下午9:34
 */
public class FrescoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Context context;


    public FrescoImageLoader(Context context) {
        this.context = context;

    }

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, final Drawable defaultDrawable, int width, int height) {
        Resources resources = context.getResources();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources)
                .setFadeDuration(300)
                .setPlaceholderImage(defaultDrawable)
                .setFailureImage(defaultDrawable)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();

        final DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create(hierarchy, context);
        imageView.setOnImageViewListener(new GFImageView.OnImageViewListener() {
            @Override
            public void onDetach() {
                draweeHolder.onDetach();
            }

            @Override
            public void onAttach() {
                draweeHolder.onAttach();
            }

            @Override
            public boolean verifyDrawable(Drawable dr) {
                if (dr == draweeHolder.getHierarchy().getTopLevelDrawable()) {
                    return true;
                }
                return false;
            }

            @Override
            public void onDraw(Canvas canvas) {
                Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                if (drawable == null) {
                    imageView.setImageDrawable(defaultDrawable);
                } else {
                    imageView.setImageDrawable(drawable);
                }
            }
        });
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build();
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))//图片目标大小
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.getController())
                .setImageRequest(imageRequest)
                .build();
        draweeHolder.setController(controller);
    }

    @Override
    public void clearMemoryCache() {

    }
}