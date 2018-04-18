package com.sss.framework.CustomWidget.ViewPager.AnimationViewPager;

/**
 * Created by leilei on 2018/2/2.
 */
import android.content.res.Resources;
import android.util.TypedValue;

public class Util {

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

}