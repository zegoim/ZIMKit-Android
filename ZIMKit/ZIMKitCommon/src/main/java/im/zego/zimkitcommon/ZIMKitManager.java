package im.zego.zimkitcommon;

import android.app.Application;

import java.util.Map;

import im.zego.zim.ZIM;
import im.zego.zim.callback.ZIMLoggedInCallback;
import im.zego.zim.entity.ZIMUserInfo;
import im.zego.zim.enums.ZIMConnectionEvent;
import im.zego.zim.enums.ZIMConnectionState;
import im.zego.zimkitcommon.event.IZIMKitEventCallBack;
import im.zego.zimkitcommon.event.ZIMKitEventHandler;
import im.zego.zimkitcommon.utils.ToastUtils;
import im.zego.zimkitcommon.utils.ZIMKitDateUtils;
import im.zego.zimkitcommon.utils.ZLog;

public class ZIMKitManager {
    private static final String TAG = "ZIMManager";
    private ZIM mZim;
    private static ZIMKitManager sInstance;
    private Application mApplication;
    private ZIMUserInfo mUserInfo;

    private ZIMKitManager() {
    }

    public static ZIMKitManager share() {
        if (sInstance == null) {
            synchronized (ZIMKitManager.class) {
                if (sInstance == null) {
                    sInstance = new ZIMKitManager();
                }
            }
        }
        return sInstance;
    }

    public void setContext(Application application) {
        this.mApplication = application;
        ZIMKitDateUtils.setContext(application);
    }

    public Application getApplication() {
        return mApplication;
    }

    public ZIM zim() {
        if (mZim == null) {
            throw new IllegalArgumentException(mApplication.getString(R.string.common_create_zim_fail_log));
        }
        return mZim;
    }

    public void createZIM(Long appID) {
        if (mZim == null) {
            mZim = ZIM.create(appID, mApplication);
            mZim.setEventHandler(ZIMKitEventHandler.share());
            ZIMKitEventHandler.share().addEventListener(ZIMKitConstant.EventConstant.KEY_CONNECTION_STATE_CHANGED,
                    this, mKitEventCallBack);
        }
    }

    private final IZIMKitEventCallBack mKitEventCallBack = new IZIMKitEventCallBack() {
        @Override
        public void onCall(String key, Map<String, Object> event) {
            if (key.equals(ZIMKitConstant.EventConstant.KEY_CONNECTION_STATE_CHANGED)) {
                ZIMConnectionEvent connectionEvent = (ZIMConnectionEvent) event.get(ZIMKitConstant.EventConstant.PARAM_EVENT);
                ZIMConnectionState connectionState = (ZIMConnectionState) event.get(ZIMKitConstant.EventConstant.PARAM_STATE);
                if (connectionState == ZIMConnectionState.DISCONNECTED && connectionEvent == ZIMConnectionEvent.KICKED_OUT) {
                    ToastUtils.showToast(mApplication, mApplication.getString(R.string.common_user_kick_out));
                }
            }
        }
    };

    public synchronized void loginUserInfo(ZIMUserInfo userInfo, String token, ZIMLoggedInCallback callback) {
        if (mZim == null) {
            ZLog.e(TAG, mApplication.getString(R.string.common_login_room_fail_zim_not_create_log));
            return;
        }
        mUserInfo = userInfo;
        mZim.login(userInfo, token, callback);
    }

    public ZIMUserInfo getUserInfo() {
        return mUserInfo;
    }

    public synchronized void logout() {
        if (mZim != null) {
            mZim.logout();
        }
    }

    public void destroy() {
        if (mZim != null) {
            ZIMKitEventHandler.share().removeEventListener(ZIMKitConstant.EventConstant.KEY_CONNECTION_STATE_CHANGED, this);
            mZim.destroy();
            mZim = null;
        }
    }
}
