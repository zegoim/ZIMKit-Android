package im.zego.zimkitmessages.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.entity.ZIMMessageSendConfig;
import im.zego.zim.entity.ZIMTextMessage;
import im.zego.zim.enums.ZIMConversationType;
import im.zego.zim.enums.ZIMMessageDirection;
import im.zego.zimkitcommon.ZIMKitConstant;
import im.zego.zimkitcommon.ZIMKitManager;
import im.zego.zimkitcommon.event.IZIMKitEventCallBack;
import im.zego.zimkitcommon.event.ZIMKitEventHandler;
import im.zego.zimkitcommon.utils.ToastUtils;
import im.zego.zimkitmessages.R;
import im.zego.zimkitmessages.model.ZIMKitMessageItemModel;

public class ZIMKitSingleMessageVM extends ZIMKitMessageVM {


    private String mSingleOtherSideUserName;

    private final IZIMKitEventCallBack eventCallBack = (key, event) -> {
        if (key.equals(ZIMKitConstant.EventConstant.KEY_RECEIVE_PEER_MESSAGE)) {
            ArrayList<ZIMMessage> messageList = (ArrayList<ZIMMessage>) event.get(ZIMKitConstant.EventConstant.PARAM_MESSAGE_LIST);
            String fromUserId = (String) event.get(ZIMKitConstant.EventConstant.PARAM_FROM_USER_ID);
            if (fromUserId != null && fromUserId.equals(mtoId)) {
                if (messageList != null && !messageList.isEmpty()) {
                    handlerNewMessageList(messageList);
                    clearUnreadCount(ZIMConversationType.PEER);
                }
            }
        }
    };

    public ZIMKitSingleMessageVM(@NonNull Application application) {
        super(application);
        ZIMKitEventHandler.share().addEventListener(ZIMKitConstant.EventConstant.KEY_RECEIVE_PEER_MESSAGE, this, eventCallBack);
    }

    public void setSingleOtherSideUserName(String userName) {
        this.mSingleOtherSideUserName = userName;
    }

    @Override
    public void queryHistoryMessage() {
        queryHistoryMessageInner(null, ZIMConversationType.PEER);
    }

    @Override
    protected void handlerHistoryMessageList(ArrayList<ZIMMessage> messageList, int state) {
        ArrayList<ZIMKitMessageItemModel> models = new ArrayList<>();
        for (ZIMMessage zimMessage : messageList) {
            ZIMKitMessageItemModel itemModel = new ZIMKitMessageItemModel(zimMessage);
            if (zimMessage.getDirection() == ZIMMessageDirection.RECEIVE) {
                setNickNameAndAvatar(itemModel, mSingleOtherSideUserName);
            } else {
                setNickNameAndAvatar(itemModel, ZIMKitManager.share().getUserInfo().userName);
            }
            models.add(itemModel);
        }
        if (state == LoadData.DATA_STATE_HISTORY_NEXT) {
            mMessageList.addAll(0, models);
        } else {
            mMessageList.addAll(models);
        }
        postList(models, state);
    }

    @Override
    public void loadNextPage(ZIMMessage message) {
        queryHistoryMessageInner(message, ZIMConversationType.PEER);
    }

    @Override
    protected void setNickNameAndAvatar(ZIMKitMessageItemModel model, String nickName) {
        model.setAvatar(nickName);
    }

    private void handlerNewMessageList(ArrayList<ZIMMessage> messageList) {
        ArrayList<ZIMKitMessageItemModel> models = new ArrayList<>();
        for (ZIMMessage zimMessage : messageList) {
            ZIMKitMessageItemModel itemModel = new ZIMKitMessageItemModel(zimMessage);
            if (zimMessage.getDirection() == ZIMMessageDirection.RECEIVE) {
                setNickNameAndAvatar(itemModel, mSingleOtherSideUserName);
            }
            models.add(itemModel);
        }
        postList(models, LoadData.DATA_STATE_NEW);
    }

    @Override
    public void send(String inputMsg) {
        ZIMMessage message = new ZIMTextMessage(inputMsg);
        ZIMKitManager.share().zim().sendPeerMessage(message, mtoId, new ZIMMessageSendConfig(), sentCallback);
    }

    @Override
    protected void onCleared() {
        ZIMKitEventHandler.share().removeEventListener(ZIMKitConstant.EventConstant.KEY_RECEIVE_PEER_MESSAGE, this);
        super.onCleared();
    }
}
