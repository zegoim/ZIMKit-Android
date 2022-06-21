package im.zego.zimkitmessages;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;

import im.zego.zimkitcommon.ZIMKitConstant;
import im.zego.zimkitcommon.ZIMKitRouter;
import im.zego.zimkitcommon.base.BaseActivity;
import im.zego.zimkitmessages.databinding.ActivityMessageBinding;
import im.zego.zimkitmessages.fragment.ZIMKitMessageFragment;

public class ZIMKitMessageActivity extends BaseActivity<ActivityMessageBinding, ViewModel> {

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getBundleExtra(ZIMKitConstant.RouterConstant.KEY_BUNDLE);
        String title = bundle.getString(ZIMKitConstant.MessagePageConstant.KEY_TITLE);
        String type = bundle.getString(ZIMKitConstant.MessagePageConstant.KEY_TYPE);
        mBinding.titleBar.setTitle(title);
        if (type.equals(ZIMKitConstant.MessagePageConstant.TYPE_GROUP_MESSAGE)) {
            mBinding.titleBar.setRightImg(R.mipmap.ic_more);
            mBinding.titleBar.setRightCLickListener(v -> ZIMKitRouter.to(ZIMKitMessageActivity.this, ZIMKitConstant.RouterConstant.ROUTER_GROUP_MANAGER, bundle));
        }else if (type.equals(ZIMKitConstant.MessagePageConstant.TYPE_SINGLE_MESSAGE)){
            mBinding.titleBar.hideRightButton();
        }
        ZIMKitMessageFragment fragment = new ZIMKitMessageFragment();
        replaceFragment(fragment, bundle);
    }

    private void replaceFragment(Fragment fragment, Bundle arg) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(arg);
        transaction.replace(R.id.fra_message, fragment);
        transaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected int getViewModelId() {
        return 0;
    }

    @Override
    protected void initData() {

    }
}