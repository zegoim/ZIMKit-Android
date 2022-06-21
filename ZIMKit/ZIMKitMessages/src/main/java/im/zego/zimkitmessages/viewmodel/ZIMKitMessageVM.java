package im.zego.zimkitmessages.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

import im.zego.zim.callback.ZIMMessageQueriedCallback;
import im.zego.zim.callback.ZIMMessageSentCallback;
import im.zego.zim.entity.ZIMError;
import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.entity.ZIMMessageQueryConfig;
import im.zego.zim.enums.ZIMConversationType;
import im.zego.zim.enums.ZIMErrorCode;
import im.zego.zimkitcommon.ZIMKitManager;
import im.zego.zimkitcommon.utils.ToastUtils;
import im.zego.zimkitmessages.R;
import im.zego.zimkitmessages.model.ZIMKitMessageItemModel;

public abstract class ZIMKitMessageVM extends AndroidViewModel {
    public ObservableField<String> inputMessage = new ObservableField<>();
    protected String mtoId = "";// toGroupId、toUserId
    public ArrayList<ZIMKitMessageItemModel> mMessageList = new ArrayList<>();
    public final static int QUERY_HISTORY_MESSAGE_COUNT = 30; //default  100
    private OnReceiveMessageListener mReceiveMessageListener;

    public ZIMKitMessageVM(@NonNull Application application) {
        super(application);
    }

    protected void postList(List<ZIMKitMessageItemModel> newList, int state) {
        mReceiveMessageListener.onSuccess(new LoadData(state, newList));
    }

    public void setId(String id) {
        this.mtoId = id;
    }

    abstract public void queryHistoryMessage();

    protected void queryHistoryMessageInner(@Nullable ZIMMessage message, ZIMConversationType type) {
        ZIMMessageQueryConfig queryConfig = new ZIMMessageQueryConfig();
        queryConfig.count = QUERY_HISTORY_MESSAGE_COUNT;
        queryConfig.nextMessage = message;
        queryConfig.reverse = true;
        ZIMKitManager.share().zim().queryHistoryMessage(mtoId, type, queryConfig, new ZIMMessageQueriedCallback() {
            @Override
            public void onMessageQueried(String conversationID, ZIMConversationType conversationType, ArrayList<ZIMMessage> messageList, ZIMError errorInfo) {
                if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                    handlerHistoryMessageList(messageList, message == null ? LoadData.DATA_STATE_HISTORY_FIRST : LoadData.DATA_STATE_HISTORY_NEXT);
                } else {
                    mReceiveMessageListener.onFail(errorInfo);
                }
            }
        });
    }

    protected void clearUnreadCount(ZIMConversationType type) {
        ZIMKitManager.share().zim().clearConversationUnreadMessageCount(mtoId, type, null);
    }

    abstract protected void handlerHistoryMessageList(ArrayList<ZIMMessage> messageList, int state);

    public void loadNextPage() {
        loadNextPage(mMessageList == null || mMessageList.size() <= 0 ? null : mMessageList.get(0).getMessage());
    }

    abstract protected void loadNextPage(ZIMMessage message);

    public void send() {
        String inputMsg = inputMessage.get();
        if (inputMsg == null || inputMsg.trim().isEmpty()) {
            ToastUtils.showToast(getApplication(), getApplication().getString(R.string.message_cant_send_empty_msg));
            return;
        }
        send(inputMsg);
        inputMessage.set("");
    }

    abstract protected void send(String message);

    protected final ZIMMessageSentCallback sentCallback = (message, errorInfo) -> {
        ArrayList<ZIMKitMessageItemModel> models = new ArrayList<>();
        ZIMKitMessageItemModel itemModel;
        itemModel = new ZIMKitMessageItemModel(message);
        setNickNameAndAvatar(itemModel, ZIMKitManager.share().getUserInfo().userName);
        mMessageList.add(itemModel);
        models.add(itemModel);
        if (errorInfo.code == ZIMErrorCode.TARGET_DOES_NOT_EXIST) {
            ZIMKitMessageItemModel errorItemMode;
            errorItemMode = new ZIMKitMessageItemModel(null);
            errorItemMode.setContent(getApplication().getString(R.string.message_user_not_exit_please_again, mtoId));
            mMessageList.add(errorItemMode);
            models.add(errorItemMode);
        }
        postList(models, LoadData.DATA_STATE_NEW);
        if (errorInfo.code != ZIMErrorCode.SUCCESS && errorInfo.code != ZIMErrorCode.TARGET_DOES_NOT_EXIST) {
            mReceiveMessageListener.onFail(errorInfo);
        }
    };

    public void setReceiveMessageListener(OnReceiveMessageListener listener) {
        mReceiveMessageListener = listener;
    }

    abstract protected void setNickNameAndAvatar(ZIMKitMessageItemModel model, String nickName);

    public interface OnReceiveMessageListener {
        void onSuccess(LoadData data);

        void onFail(ZIMError error);
    }

    public static class LoadData {
        public final static int DATA_STATE_HISTORY_NEXT = 0;
        public final static int DATA_STATE_HISTORY_FIRST = 1;
        public final static int DATA_STATE_NEW = 2;

        public int state;
        public List<ZIMKitMessageItemModel> data;

        public LoadData(int state, List<ZIMKitMessageItemModel> data) {
            this.state = state;
            this.data = data;
        }
    }

}
