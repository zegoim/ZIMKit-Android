package im.zego.zimkitgroup.ui;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import im.zego.zim.enums.ZIMErrorCode;
import im.zego.zimkitcommon.ZIMKitConstant;
import im.zego.zimkitcommon.ZIMKitRouter;
import im.zego.zimkitcommon.base.BaseActivity;
import im.zego.zimkitcommon.utils.ToastUtils;
import im.zego.zimkitgroup.BR;
import im.zego.zimkitgroup.R;
import im.zego.zimkitgroup.databinding.ActivityCreateAndJoinGroupBinding;
import im.zego.zimkitgroup.viewmodel.ZIMKitCreateAndJoinGroupVM;

public class ZIMKitCreateAndJoinGroupActivity extends BaseActivity<ActivityCreateAndJoinGroupBinding, ZIMKitCreateAndJoinGroupVM> {
    private String mType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_and_join_group;
    }

    @Override
    protected int getViewModelId() {
        return BR.vm;
    }

    @Override
    protected void initView() {
        Bundle bundle = this.getIntent().getBundleExtra(ZIMKitConstant.RouterConstant.KEY_BUNDLE);
        mType = bundle.getString(ZIMKitConstant.GroupPageConstant.KEY_TYPE);
        String title = "";
        switch (mType) {
            case ZIMKitConstant.GroupPageConstant.TYPE_CREATE_GROUP_MESSAGE:
                title = getString(R.string.group_create_group_chat_title);
                break;
            case ZIMKitConstant.GroupPageConstant.TYPE_JOIN_GROUP_MESSAGE:
                title = getString(R.string.group_join_group_chat);
                break;
        }
        mBinding.titleBar.setTitle(title);
        mBinding.titleBar.setLeftImg(R.mipmap.ic_group_close);
        mBinding.titleBar.hideRightButton();
    }

    @Override
    protected void initData() {
        mViewModel.setType(mType);
        mViewModel.toChatLiveData.observe(this, pair -> {
            if (pair.first == ZIMErrorCode.SUCCESS) {
                Bundle data = (Bundle) pair.second;
                data.putString(ZIMKitConstant.MessagePageConstant.KEY_TYPE, ZIMKitConstant.MessagePageConstant.TYPE_GROUP_MESSAGE);
                ZIMKitRouter.toAndFinish(ZIMKitCreateAndJoinGroupActivity.this,
                        ZIMKitConstant.RouterConstant.ROUTER_MESSAGE, data);
            } else {
                String errorMsg = (String) pair.second;
                if (pair.first == ZIMErrorCode.DOES_NOT_EXIST && mType.equals(ZIMKitConstant.GroupPageConstant.TYPE_CREATE_GROUP_MESSAGE)) {
                    showDialog(R.string.group_user_not_exit, errorMsg);
                } else if (pair.first == ZIMErrorCode.DOES_NOT_EXIST && mType.equals(ZIMKitConstant.GroupPageConstant.TYPE_JOIN_GROUP_MESSAGE)) {
                    showDialog(R.string.group_group_not_exit, errorMsg);
                } else {
                    ToastUtils.showToast(getApplication(), errorMsg);
                }
            }
        });
    }

    private void showDialog(int titleRes, String msg) {
        new AlertDialog.Builder(this).setTitle(titleRes)
                .setPositiveButton("ok", (dialog, id) -> dialog.cancel())
                .setMessage(msg).show();
    }
}