package im.zego.zimkitconversation.ui;

import android.os.Bundle;
import android.view.View;

import im.zego.zimkitcommon.ZIMKitConstant;
import im.zego.zimkitcommon.ZIMKitRouter;
import im.zego.zimkitcommon.base.BaseActivity;
import im.zego.zimkitconversation.BR;
import im.zego.zimkitconversation.R;
import im.zego.zimkitconversation.databinding.ActivityConversationBinding;
import im.zego.zimkitconversation.databinding.LayoutSeletectChatTypeBinding;
import im.zego.zimkitconversation.viewmodel.ZIMKitConversationVM;
import im.zego.zimkitconversation.widget.CustomBottomSheet;

public class ZIMKitConversationActivity extends BaseActivity<ActivityConversationBinding, ZIMKitConversationVM> {
    private CustomBottomSheet<LayoutSeletectChatTypeBinding> mSelectChatBottomSheet;

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
        mBinding.titleBar.setLeftCLickListener(v -> {
            mViewModel.logout();
            finish();
        });
        mBinding.titleBar.setRightCLickListener(v -> {
            showSelectChatBottomSheet();
        });
        mBinding.btnStartChat.setOnClickListener(v -> showSelectChatBottomSheet());
    }

    private void showSelectChatBottomSheet() {
        if (mSelectChatBottomSheet != null && mSelectChatBottomSheet.getDialog() != null && mSelectChatBottomSheet.getDialog().isShowing()) {
            mSelectChatBottomSheet.dismiss();
        }
        if (mSelectChatBottomSheet == null) {
            mSelectChatBottomSheet = new CustomBottomSheet<>(R.layout.layout_seletect_chat_type, this::setBottomSheetItemListener);
        }
        mSelectChatBottomSheet.show(getSupportFragmentManager(), "chatType");
    }

    private void setBottomSheetItemListener(LayoutSeletectChatTypeBinding binding) {
        binding.tvCreateGroupChat.setOnClickListener(v -> {
            toGroupChat(ZIMKitConstant.GroupPageConstant.TYPE_CREATE_GROUP_MESSAGE);
        });
        binding.tvCreateSingleChat.setOnClickListener(v -> {
            toSingleChat();
        });
        binding.tvJoinGroupChat.setOnClickListener(v -> {
            toGroupChat(ZIMKitConstant.GroupPageConstant.TYPE_JOIN_GROUP_MESSAGE);
        });
    }

    private void toSingleChat() {
        dismissBottomSheet();
        ZIMKitRouter.to(this, ZIMKitConstant.RouterConstant.ROUTER_CREATE_SINGLE_CHAT, null);
    }

    private void toGroupChat(String type) {
        dismissBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString(ZIMKitConstant.GroupPageConstant.KEY_TYPE, type);
        ZIMKitRouter.to(this, ZIMKitConstant.RouterConstant.ROUTER_CREATE_AND_JOIN_GROUP, bundle);
    }

    @Override
    protected void onDestroy() {
        dismissBottomSheet();
        super.onDestroy();
    }

    private void dismissBottomSheet() {
        if (mSelectChatBottomSheet != null) {
            mSelectChatBottomSheet.dismiss();
            mSelectChatBottomSheet = null;
        }
    }

    @Override
    public void onBackPressed() {
        mViewModel.logout();
        super.onBackPressed();
    }

    @Override
    protected void initData() {

    }

    public void reloadConversation(View view) {

    }
}