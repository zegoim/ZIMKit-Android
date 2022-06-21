package im.zego.zimkitconversation.ui;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import im.zego.zim.entity.ZIMError;
import im.zego.zim.enums.ZIMConversationType;
import im.zego.zim.enums.ZIMErrorCode;
import im.zego.zimkitcommon.ZIMKitConstant;
import im.zego.zimkitcommon.ZIMKitRouter;
import im.zego.zimkitcommon.base.BaseFragment;
import im.zego.zimkitcommon.utils.ToastUtils;
import im.zego.zimkitconversation.BR;
import im.zego.zimkitconversation.ZIMKitConversationListAdapter;
import im.zego.zimkitconversation.R;
import im.zego.zimkitconversation.databinding.FragmentConversationListBinding;
import im.zego.zimkitconversation.databinding.LayoutDeleteConversationBinding;
import im.zego.zimkitconversation.model.ZIMKitConversationModel;
import im.zego.zimkitconversation.viewmodel.ZIMKitConversationVM;
import im.zego.zimkitconversation.widget.CustomBottomSheet;

public class ZIMKitConversationFragment extends BaseFragment<FragmentConversationListBinding, ZIMKitConversationVM> {
    private ZIMKitConversationListAdapter mListAdapter;
    private CustomBottomSheet<LayoutDeleteConversationBinding> mDeleteConversationBottomSheet;
    private ZIMKitConversationModel mCurrentSelectModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_conversation_list;
    }

    @Override
    protected int getViewModelId() {
        return BR.vm;
    }

    @Override
    protected void initView() {
        mListAdapter = new ZIMKitConversationListAdapter();
        mListAdapter.setLongClickListener(model -> {
            if (mDeleteConversationBottomSheet != null && mDeleteConversationBottomSheet.getDialog() != null && mDeleteConversationBottomSheet.getDialog().isShowing()) {
                mDeleteConversationBottomSheet.dismiss();
            }
            if (mDeleteConversationBottomSheet == null) {
                mDeleteConversationBottomSheet = new CustomBottomSheet<>(R.layout.layout_delete_conversation, this::setBottomSheetItemListener);
            }
            mCurrentSelectModel = model;
            mDeleteConversationBottomSheet.show(ZIMKitConversationFragment.this.getParentFragmentManager(), "delete_conversation");
        });
        mListAdapter.setItemClickListener(model -> {
            mViewModel.clearConversationUnreadMessageCount(model.getConversationID(), model.getType());
            toChat(model.getType(), model.getName(), model.getConversationID());
        });

        mBinding.rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.rvList.setAdapter(mListAdapter);
        mBinding.refreshLayout.setEnableRefresh(false);
        mBinding.refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mViewModel.loadNextPage();
        });
        mBinding.btnReload.setOnClickListener(v -> mViewModel.loadConversation());
    }

    private void setBottomSheetItemListener(LayoutDeleteConversationBinding binding) {
        binding.teCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteConversationBottomSheet.dismiss();
            }
        });
        binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteConversation(mCurrentSelectModel.getConversation());
                mDeleteConversationBottomSheet.dismiss();
            }
        });
    }

    private void toChat(ZIMConversationType type, String name, String conversationId) {
        Bundle data = new Bundle();
        if (type == ZIMConversationType.GROUP) {
            data.putString(ZIMKitConstant.MessagePageConstant.KEY_TYPE, ZIMKitConstant.MessagePageConstant.TYPE_GROUP_MESSAGE);
        } else if (type == ZIMConversationType.PEER) {
            data.putString(ZIMKitConstant.MessagePageConstant.KEY_TYPE, ZIMKitConstant.MessagePageConstant.TYPE_SINGLE_MESSAGE);
        }
        data.putString(ZIMKitConstant.MessagePageConstant.KEY_ID, conversationId);
        data.putString(ZIMKitConstant.MessagePageConstant.KEY_TITLE, name);
        ZIMKitRouter.to(this.getContext(),
                ZIMKitConstant.RouterConstant.ROUTER_MESSAGE, data);
        dismissBottomSheet();
    }

    @Override
    public void onDestroy() {
        dismissBottomSheet();
        super.onDestroy();
    }

    private void dismissBottomSheet() {
        if (mDeleteConversationBottomSheet != null) {
            mDeleteConversationBottomSheet.dismiss();
            mDeleteConversationBottomSheet = null;
        }
    }

    @Override
    protected void initData() {
        mViewModel.setOnLoadConversationListener(new ZIMKitConversationVM.OnLoadConversationListener() {
            @Override
            public void onSuccess(ZIMKitConversationVM.LoadData loadData) {
                if (loadData.currentLoadIsEmpty) {
                    mBinding.refreshLayout.finishRefreshWithNoMoreData();
                } else {
                    mListAdapter.submitList(new ArrayList<>(loadData.allList));
                    if (loadData.state == ZIMKitConversationVM.LoadData.DATA_STATE_CHANGE) {
                        mBinding.rvList.post(() -> mBinding.rvList.smoothScrollToPosition(0));
                    } else {
                        mBinding.refreshLayout.finishLoadMore(true);
                    }
                }
            }

            @Override
            public void onFail(ZIMError error) {
                mBinding.refreshLayout.finishLoadMore(false);
                ToastUtils.showToast(getActivity(), error.message);
            }
        });
        mViewModel.loadConversation(); // the first
    }
}
