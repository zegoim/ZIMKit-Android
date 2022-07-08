package im.zego.zimkitconversation.ui;

import androidx.fragment.app.Fragment;

import im.zego.zimkitcommon.base.BaseActivity;
import im.zego.zimkitconversation.BR;
import im.zego.zimkitconversation.R;
import im.zego.zimkitconversation.databinding.ActivityConversationBinding;
import im.zego.zimkitconversation.viewmodel.ZIMKitConversationVM;

public class ZIMKitConversationActivity extends BaseActivity<ActivityConversationBinding, ZIMKitConversationVM> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_conversation;
    }

    @Override
    protected int getViewModelId() {
        return BR.vm;
    }

    @Override
    protected void initView() {
        mBinding.titleBar.setLeftImg(R.mipmap.ic_logout);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag);
        mBinding.titleBar.setLeftCLickListener(v -> {
            mViewModel.logout();
            finish();
        });
        mBinding.titleBar.setRightCLickListener(v -> {
            if (fragment instanceof ZIMKitConversationFragment)
                ((ZIMKitConversationFragment) fragment).showSelectChatBottomSheet();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mViewModel.logout();
        super.onBackPressed();
    }

    @Override
    protected void initData() {

    }
}