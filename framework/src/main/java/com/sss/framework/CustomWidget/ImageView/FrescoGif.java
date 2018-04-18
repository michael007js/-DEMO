package com.sss.framework.CustomWidget.ImageView;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by leilei on 2018/2/28.
 */

public class FrescoGif extends SimpleDraweeView{

    public FrescoGif(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public FrescoGif(Context context) {
        super(context);
    }

    public FrescoGif(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrescoGif(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FrescoGif(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FrescoGif setGif(Uri uri) {
        setController( Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true).setUri(uri).build());
        return this;
    }

}
