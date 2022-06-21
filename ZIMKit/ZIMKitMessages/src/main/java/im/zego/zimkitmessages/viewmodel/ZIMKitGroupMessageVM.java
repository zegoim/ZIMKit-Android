package im.zego.zimkitmessages.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import im.zego.zim.entity.ZIMGroupMemberInfo;
import im.zego.zim.entity.ZIMGroupMemberQueryConfig;
import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.entity.ZIMMessageSendConfig;
import im.zego.zim.entity.ZIMTextMessage;
import im.zego.zim.enums.ZIMConversationType;
import im.zego.zim.enums.ZIMErrorCode;
import im.zego.zim.enums.ZIMGroupMemberEvent;
import im.zego.zim.enums.ZIMGroupMemberState;
import im.zego.zimkitcommon.ZIMKitConstant;
import im.zego.zimkitcommon.ZIMKitManager;
import im.zego.zimkitcommon.event.IZIMKitEventCallBack;
import im.zego.zimkitcommon.event.ZIMKitEventHandler;
import im.zego.zimkitcommon.utils.ToastUtils;
import im.zego.zimkitmessages.R;
import im.zego.zimkitmessages.model.ZIMKitMessageItemModel;

public class ZIMKitGroupMessageVM extends ZIMKitMessageVM {
    private final Map<String, String> mGroupUserInfoMap = new HashMap<>();


    private final IZIMKitEventCallBack eventCallBack = (key, event) -> {
        if (key.equals(ZIMKitConstant.EventConstant.KEY_RECEIVE_GROUP_MESSAGE)) {
            String fromGroupId = (String) event.get(ZIMKitConstant.EventConstant.PARAM_FROM_GROUP_ID);
            if (!mtoId.equals(fromGroupId)) {
                return;
            }
            ArrayList<ZIMMessage> messageList = (ArrayList<ZIMMessage>) event.get(ZIMKitConstant.EventConstant.PARAM_MESSAGE_LIST);
            if (messageList != null && !messageList.isEmpty()) {
                handlerNewMessageList(messageList);
                clearUnreadCount(ZIMConversationType.GROUP);
            }
        } else if (key.equals(ZIMKitConstant.EventConstant.KEY_GROUP_MEMBER_STATE_CHANGED)) {
            String groupId = (String) event.get(ZIMKitConstant.EventConstant.PARAM_GROUP_ID);
            ArrayList<ZIMGroupMemberInfo> userList = (ArrayList<ZIMGroupMemberInfo>) event.get(ZIMKitConstant.EventConstant.PARAM_USER_LIST);
            if (!mtoId.equals(groupId) || userList == null) {
                return;
            }
            ZIMGroupMemberState memberState = (ZIMGroupMemberState) event.get(ZIMKitConstant.EventConstant.PARAM_STATE);
            if (memberState == ZIMGroupMemberState.ENTER) {
                for (ZIMGroupMemberInfo info : userList) {
                    mGroupUserInfoMap.put(info.userID, info.userName);
                }
            } else if (memberState == ZIMGroupMemberState.QUIT) {
                for (ZIMGroupMemberInfo info : userList) {
                    mGroupUserInfoMap.remove(info.userID);
                }
            }
        }
    };

    public ZIMKitGroupMessageVM(@NonNull Application application) {
        super(application);
        ZIMKitEventHandler.share().addEventListener(ZIMKitConstant.EventConstant.KEY_RECEIVE_GROUP_MESSAGE, this, eventCallBack);
        ZIMKitEventHandler.share().addEventListener(ZIMKitConstant.EventConstant.KEY_GROUP_MEMBER_STATE_CHANGED, this, eventCallBack);
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        queryGroupMemberList(0);
    }

    @Override
    public void queryHistoryMessage() {
        queryHistoryMessageInner(null, ZIMConversationType.GROUP);
    }

    private void queryGroupMemberList(int nextFlag) {
        ZIMGroupMemberQueryConfig queryConfig = new ZIMGroupMemberQueryConfig();
        queryConfig.count = 100;
        queryConfig.nextFlag = nextFlag;
        ZIMKitManager.share().zim().queryGroupMemberList(mtoId, queryConfig, (groupID, userList, nextFlag1, errorInfo) -> {
            if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                for (ZIMGroupMemberInfo info : userList) {
                    mGroupUserInfoMap.put(info.userID, info.userName);
                }
                if (userList.size() == 100) {
                    queryGroupMemberList(nextFlag1);
                } else {
                    checkCanPostGroupHistoryList();
                }
            }
        });
    }

    private void checkCanPostGroupHistoryList() {
        if (!mMessageList.isEmpty()) {
            for (ZIMKitMessageItemModel itemModel : mMessageList) {
                if (itemModel.getMessage() != null) {
                    String nickName = mGroupUserInfoMap.get(itemModel.getMessage().getSenderUserID());
                    if (nickName != null) {
                        setNickNameAndAvatar(itemModel, nickName);
                    } else {
                        setNickNameAndAvatar(itemModel, itemModel.getMessage().getSenderUserID());
                    }
                }
            }
            postList(mMessageList, LoadData.DATA_STATE_HISTORY_FIRST);
        }
    }

    @Override
    protected void handlerHistoryMessageList(ArrayList<ZIMMessage> messageList, int state) {
        ArrayList<ZIMKitMessageItemModel> models = new ArrayList<>();
        for (ZIMMessage zimMessage : messageList) {
            ZIMKitMessageItemModel itemModel = new ZIMKitMessageItemModel(zimMessage);
            String nickName = mGroupUserInfoMap.get(zimMessage.getSenderUserID());
            if (nickName != null) {
                setNickNameAndAvatar(itemModel, nickName);
            } else {
                setNickNameAndAvatar(itemModel, zimMessage.getSenderUserID());
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
        queryHistoryMessageInner(message, ZIMConversationType.GROUP);
    }

    @Override
    protected void setNickNameAndAvatar(ZIMKitMessageItemModel model, String nickName) {
        model.setNickName(nickName);
        model.setAvatar(nickName);//目前avatar 使用nickname
    }

    private void handlerNewMessageList(ArrayList<ZIMMessage> messageList) {
        ArrayList<ZIMKitMessageItemModel> models = new ArrayList<>();
        for (ZIMMessage message : messageList) {
            ZIMKitMessageItemModel itemModel = new ZIMKitMessageItemModel(message);
            String nickName = mGroupUserInfoMap.get(message.getSenderUserID());
            if (nickName != null) {
                setNickNameAndAvatar(itemModel, nickName);
            } else {
                setNickNameAndAvatar(itemModel, message.getSenderUserID());
            }
            models.add(itemModel);
        }
        postList(models, LoadData.DATA_STATE_NEW);
    }

    @Override
    public void send(String inputMsg) {
        ZIMMessage message = new ZIMTextMessage(inputMsg);
        ZIMKitManager.share().zim().sendGroupMessage(message, mtoId, new ZIMMessageSendConfig(), sentCallback);
    }

    @Override
    protected void onCleared() {
        ZIMKitEventHandler.share().removeEventListener(ZIMKitConstant.EventConstant.KEY_RECEIVE_GROUP_MESSAGE, this);
        ZIMKitEventHandler.share().removeEventListener(ZIMKitConstant.EventConstant.KEY_GROUP_MEMBER_STATE_CHANGED, this);
    }
}
