package com.sss.framework.Library.Glid;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.sss.framework.Library.Log.LogUtils;
import com.sss.framework.R;

import cn.finalteam.galleryfinal.widget.GFImageView;


/**
 * Created by leilei on 2017/8/15.
 */

public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {


    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true)
                .centerCrop()
                .into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                        imageView.setTag(R.id.glide_tag, request);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) imageView.getTag(R.id.glide_tag);
                    }
                });
    }

    @Override
    public void clearMemoryCache() {
        LogUtils.e("clearMemoryCache");
    }
}
