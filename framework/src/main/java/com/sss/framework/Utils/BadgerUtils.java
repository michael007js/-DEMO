package com.sss.framework.Utils;

import android.content.Context;

import com.sss.framework.Library.Log.LogUtils;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 角标
 * https://github.com/leolin310148/ShortcutBadger
 *
 * https://github.com/lixiangers/BadgeUtil
 *
 * Created by leilei on 2017/8/8.
 */

public class BadgerUtils {

    public static void addBadger(Context context,int badgeCount){
        if (EmptyUtils.isNotEmpty(context)){
            ShortcutBadger.applyCount(context, badgeCount);
            LogUtils.e("addBadger"+badgeCount);
        }
    }

    public static void removeCount(Context context ){
        if (EmptyUtils.isNotEmpty(context)){
            ShortcutBadger.removeCount(context);
        }
    }

    public static void applyCount(Context context ,int badgeCount){
        if (EmptyUtils.isNotEmpty(context)){
            ShortcutBadger.applyCount(context,badgeCount);
        }
    }

}
