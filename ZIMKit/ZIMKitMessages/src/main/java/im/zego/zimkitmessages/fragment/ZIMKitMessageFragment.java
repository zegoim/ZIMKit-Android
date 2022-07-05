package im.zego.zimkitmessages.fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import im.zego.zim.entity.ZIMError;
import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.enums.ZIMErrorCode;
import im.zego.zimkitcommon.ZIMKitConstant;
import im.zego.zimkitcommon.base.BaseFragment;
import im.zego.zimkitcommon.utils.ToastUtils;
import im.zego.zimkitcommon.utils.ZIMKitDateUtils;
import im.zego.zimkitmessages.BR;
import im.zego.zimkitmessages.R;
import im.zego.zimkitmessages.adapter.ZIMKitMessageAdapter;
import im.zego.zimkitmessages.databinding.FragmentMessageBinding;
import im.zego.zimkitmessages.viewmodel.ZIMKitGroupMessageVM;
import im.zego.zimkitmessages.viewmodel.ZIMKitMessageVM;
import im.zego.zimkitmessages.viewmodel.ZIMKitSingleMessageVM;
import im.zego.zimkitmessages.widget.ZIMKitMessageTimeLineDecoration;

public class ZIMKitMessageFragment extends BaseFragment<FragmentMessageBinding, ZIMKitMessageVM> {
    private ZIMKitMessageAdapter mAdapter;
    private final long mTimeLineInterval = 1000 * 60 * 5;//时间戳间隔,5min

    @Override
    protected void initView() {
        initRv();
    }

    private void initRv() {
        mAdapter = new ZIMKitMessageAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.rvMessage.setLayoutManager(linearLayoutManager);
        mBinding.rvMessage.setAdapter(mAdapter);
        mBinding.refreshLayout.setEnableLoadMore(false);
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.loadNextPage();
        });
        mBinding.refreshLayout.setEnableScrollContentWhenRefreshed(true);
        if (getContext() != null) {
            mBinding.rvMessage.addItemDecoration(new ZIMKitMessageTimeLineDecoration(getContext(), new ZIMKitMessageTimeLineDecoration.DecorationCallback() {
                @Override
                public boolean needAddTimeLine(int position) {
                    if (position < 0) {
                        return false;
                    }
                    if (position == 0) {
                        return mAdapter.getData().get(position).getMessage() != null;
                    }
                    ZIMMessage nowMessage = mAdapter.getData().get(position).getMessage();
                    ZIMMessage lastMessage = mAdapter.getData().get(position - 1).getMessage();
                    if (nowMessage == null || lastMessage == null) {
                        return false;
                    } else {
                        return (nowMessage.getTimestamp() - lastMessage.getTimestamp()) > mTimeLineInterval;
                    }
                }

                @Override
                public String getTimeLine(int position) {
                    if (position < 0) {
                        return "";
                    }
                    return ZIMKitDateUtils.getMessageDate(mAdapter.getData().get(position).getMessage().getTimestamp(), true);
                }
            }));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected int getViewModelId() {
        return 0;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            String type = getArguments().getString(ZIMKitConstant.MessagePageConstant.KEY_TYPE);
            String id = getArguments().getString(ZIMKitConstant.MessagePageConstant.KEY_ID);
            if (type.equals(ZIMKitConstant.MessagePageConstant.TYPE_SINGLE_MESSAGE)) {
                String title = getArguments().getString(ZIMKitConstant.MessagePageConstant.KEY_TITLE);
                mViewModel = new ViewModelProvider(requireActivity()).get(ZIMKitSingleMessageVM.class);
                ((ZIMKitSingleMessageVM) mViewModel).setSingleOtherSideUserName(title);
            } else if (type.equals(ZIMKitConstant.MessagePageConstant.TYPE_GROUP_MESSAGE)) {
                mViewModel = new ViewModelProvider(requireActivity()).get(ZIMKitGroupMessageVM.class);
            }
            mBinding.setVariable(BR.vm, mViewModel);
            mViewModel.setId(id);
            mViewModel.queryHistoryMessage();
        }
        mViewModel.setReceiveMessageListener(new ZIMKitMessageVM.OnReceiveMessageListener() {
            @Override
            public void onSuccess(ZIMKitMessageVM.LoadData loadData) {
                if (loadData.data.isEmpty()) {
                    mBinding.refreshLayout.finishRefreshWithNoMoreData();
                    return;
                }
                if (loadData.state == ZIMKitMessageVM.LoadData.DATA_STATE_HISTORY_FIRST) {
                    mAdapter.setNewList(loadData.data);
                } else if (loadData.state == ZIMKitMessageVM.LoadData.DATA_STATE_HISTORY_NEXT) {
                    mAdapter.addListToTop(loadData.data);
                } else if (loadData.state == ZIMKitMessageVM.LoadData.DATA_STATE_NEW) {
                    mAdapter.addListToBottom(loadData.data);
                }
                if (loadData.state != ZIMKitMessageVM.LoadData.DATA_STATE_NEW) {
                    if (loadData.data.size() < ZIMKitMessageVM.QUERY_HISTORY_MESSAGE_COUNT) {
                        mBinding.refreshLayout.finishRefresh();
                    } else {
                        mBinding.refreshLayout.finishRefreshWithNoMoreData();
                    }
                }
                if (loadData.state != ZIMKitMessageVM.LoadData.DATA_STATE_HISTORY_NEXT) {
                    mBinding.rvMessage.postDelayed(() -> mBinding.rvMessage.smoothScrollToPosition(mAdapter.getData().size() - 1), 100);
                }
            }

            @Override
            public void onFail(ZIMError error) {
                mBinding.refreshLayout.finishRefresh(false);
                if (error.code != ZIMErrorCode.SUCCESS) {
                    ToastUtils.showToast(getContext(), error.message);
                }
            }
        });
    }
}
