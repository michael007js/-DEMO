package com.sss.framework.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.sss.framework.Application.FrameworkApplication;


/**
 * Created by leilei on 2017/9/6.
 */

@SuppressWarnings("ALL")
public class ScreenLightUtils {
    /**
     * 获得当前屏幕亮度的模式
     *
     * @return 1 为自动调节屏幕亮度,0 为手动调节屏幕亮度,-1 获取失败
     */
    public static int getScreenMode(Context context) {
        int mode = -1;
        try {
            mode = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return mode;
    }

    /**
     * 获得当前屏幕亮度值
     *
     * @return 0--255
     */
    public static int getScreenBrightness(Context context) {
        int screenBrightness = -1;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return screenBrightness;
    }

    /**
     * 设置当前屏幕亮度的模式
     *
     * @param mode 1 为自动调节屏幕亮度,0 为手动调节屏幕亮度
     */
    public static void setScreenMode(int mode,Context context) {
        try {
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
            Uri uri = Settings.System
                    .getUriFor("screen_brightness_mode");
            context.getContentResolver().notifyChange(uri, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
     * 保存当前的屏幕亮度值，并使之生效
     *
     * @param paramInt 0-255
     */
    public static void setScreenBrightness(int paramInt,Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, paramInt);
        Uri uri = Settings.System
                .getUriFor("screen_brightness");
        context.getContentResolver().notifyChange(uri, null);
    }


    /**
     * 根据亮度值修改当前APPwindow亮度
     * @param context
     * @param brightness
     */
    public static void changeAppBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }
}
