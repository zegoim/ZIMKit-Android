package im.zego.zimkit;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

import im.zego.zimkit.constant.AppConfig;
import im.zego.zimkitcommon.ZIMKitManager;
import im.zego.zimkitcommon.ZIMKitRouter;

public class MyApplication extends Application {
    public static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        CrashReport.initCrashReport(getApplicationContext(), "6c845a0007", false);
        CrashReport.setIsDevelopmentDevice(getApplicationContext(),BuildConfig.DEBUG);
        ZIMKitRouter.initRouter(this);
        ZIMKitManager.share().setContext(this);
        ZIMKitManager.share().createZIM(AppConfig.APP_ID);
    }
}
