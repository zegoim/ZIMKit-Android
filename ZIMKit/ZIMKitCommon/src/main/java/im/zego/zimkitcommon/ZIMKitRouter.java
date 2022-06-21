package im.zego.zimkitcommon;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现 Activity / fragment 路由跳转
 */
public class ZIMKitRouter {
    private static final Map<String, String> routerMap = new HashMap<>();

    private ZIMKitRouter() {
    }

    public static void initRouter(Context context) {
        ActivityInfo[] activityInfos = null;
        List<String> activityNames = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            activityInfos = packageInfo.activities;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (activityInfos != null) {
            for (ActivityInfo activityInfo : activityInfos) {
                activityNames.add(activityInfo.name);
            }
        }
        for (String activityName : activityNames) {
            if (activityName.contains("im.zego.zimkit")) {
                String[] splitStr = activityName.split("\\.");
                routerMap.put(splitStr[splitStr.length - 1], activityName);
            }
        }
    }

    /**
     * to activity or fragment page based on path
     *
     * @param context the context
     * @param path    the path
     */
    public static void to(Context context, String path, Bundle bundle) {
        String activityName = routerMap.get(path);
        if (activityName == null) {
            throw new IllegalArgumentException(String.format("the %s not found activity,please check", path));
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, activityName));
        intent.putExtra(ZIMKitConstant.RouterConstant.KEY_BUNDLE, bundle);
        ActivityCompat.startActivity(context, intent, bundle);
    }

    /**
     * to activity or fragment page based on path And finish context activity
     *
     * @param context the context
     * @param path    the path
     */
    public static void toAndFinish(Context context, String path, Bundle bundle) {
        to(context, path, bundle);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}