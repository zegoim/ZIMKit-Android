package im.zego.zimkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import im.zego.zimkit.databinding.ActivityLoginBinding;
import im.zego.zimkitcommon.ZIMKitConstant;
import im.zego.zimkitcommon.ZIMKitRouter;
import im.zego.zimkitcommon.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewDataBinding.setLoginRoomVM(mViewModel);
        viewDataBinding.setLifecycleOwner(this);
        mViewModel.mLoginStateLiveData.observe(this, booleanStringPair -> {
            if (booleanStringPair.first) {
                ZIMKitRouter.to(this, ZIMKitConstant.RouterConstant.ROUTER_CONVERSATION, null);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mViewModel.cleanUserId();
    }
}