package im.zego.zimkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

import im.zego.zim.entity.ZIMUserInfo;
import im.zego.zim.enums.ZIMErrorCode;
import im.zego.zimkit.constant.UserNames;
import im.zego.zimkit.token.TokenManager;
import im.zego.zimkitcommon.ZIMKitManager;

public class LoginViewModel extends ViewModel {
    public ObservableField<String> mUserId = new ObservableField<>();
    public MutableLiveData<Boolean> mLoginButtonEnableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isShowErrorTips = new MutableLiveData<>(true);
    public MutableLiveData<Pair<Boolean, String>> mLoginStateLiveData = new MutableLiveData<>();
    public ObservableField<String> mUserName = new ObservableField<>();
    private SharedPreferences sp = MyApplication.sInstance.getSharedPreferences("imkit", Context.MODE_PRIVATE);

    public LoginViewModel() {
        mUserName.set("");
    }

    public TextWatcher onEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String id = s.toString().trim();
                boolean isLengthCorrect = s.length() >= 6 && s.length() <= 12;
                mLoginButtonEnableLiveData.postValue(!id.isEmpty() && isLengthCorrect);
                isShowErrorTips.postValue(id.isEmpty() || !isLengthCorrect);
                mUserName.set(getUserName(s.toString().trim()));
            }
        };
    }

    public void login() {
        ZIMUserInfo userInfo = new ZIMUserInfo();
        userInfo.userID = mUserId.get();
        userInfo.userName = mUserName.get();
        ZIMKitManager.share().loginUserInfo(userInfo, TokenManager.genToken(mUserId.get()), errorInfo -> {
            isShowErrorTips.postValue(errorInfo.code != ZIMErrorCode.SUCCESS);
            if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                saveUserName(userInfo.userID, userInfo.userName);
                mLoginStateLiveData.postValue(new Pair<>(errorInfo.code == ZIMErrorCode.SUCCESS, errorInfo.message));
            }
        });
    }

    private String getUserName(String userId) {
        String spValue = sp.getString(userId, "");
        if (spValue.isEmpty()) {
            return genRandomUserName();
        } else {
            return spValue;
        }
    }

    public void cleanUserId() {
        mUserId.set("");
    }

    private void saveUserName(String userId, String userName) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(userId, userName);
        edit.apply();
    }

    private String genRandomUserName() {
        int length = UserNames.userNames.length;
        Random ra = new Random();
        int i = ra.nextInt(length - 1);//生成[0-length-1)的随机数
        return UserNames.userNames[i];
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
