package im.zego.zimkitconversation.model;

import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.Objects;

import im.zego.zim.entity.ZIMConversation;
import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.entity.ZIMTextMessage;
import im.zego.zim.enums.ZIMConversationType;
import im.zego.zim.enums.ZIMMessageType;
import im.zego.zimkitcommon.utils.ZIMKitDateUtils;
import im.zego.zimkitconversation.BR;

public class ZIMKitConversationModel extends BaseObservable {
    private String mName;
    private String mTime;
    private Integer mUnReadCount;
    private String mAvatar;
    private final ZIMConversation mConversation;
    private String mLastMsgContent;

    public ZIMKitConversationModel(ZIMConversation conversation) {
        this.setLastMsgContent(conversation);
        this.setAvatar(getAvatar(conversation));
        this.setName(conversation);
        this.setTime(conversation.lastMessage);
        this.setUnReadCount(conversation.unreadMessageCount);
        this.mConversation = conversation;
    }

    private String getAvatar(ZIMConversation conversation) {
        if (conversation.type == ZIMConversationType.GROUP) {
            return "G";//群聊头像就只显示 「G」
        } else if (conversation.type == ZIMConversationType.PEER) {
            return (TextUtils.isEmpty(conversation.conversationName)
                    ? conversation.conversationID :
                    conversation.conversationName
            ).toLowerCase().charAt(0) + ""; //单聊取首字母
        } else {
            return "";
        }
    }

    private void setName(ZIMConversation conversation) {
        this.mName = TextUtils.isEmpty(conversation.conversationName)
                ? conversation.conversationID
                : conversation.conversationName;
        notifyPropertyChanged(BR.name);
    }

    private void setTime(ZIMMessage message) {
        if (message == null || message.getTimestamp() == 0) {
            return;
        }
        long time = message.getTimestamp();
        this.mTime = ZIMKitDateUtils.getMessageDate(time, false);
        notifyPropertyChanged(BR.time);
    }

    private void setUnReadCount(Integer mUnReadCount) {
        this.mUnReadCount = mUnReadCount;
        notifyPropertyChanged(BR.unReadCount);
    }

    private void setAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
        notifyPropertyChanged(BR.lastMsgContent);
    }

    private void setLastMsgContent(ZIMConversation conversation) {
        ZIMMessage message = conversation.lastMessage;
        if (message == null) {
            return;
        }
        if (message.getType() == ZIMMessageType.TEXT) {
            ZIMTextMessage textMessage = (ZIMTextMessage) message;
            mLastMsgContent = textMessage.message;
        } else {
            mLastMsgContent = "other type message"; // todo 这里目前先不做其他的消息
        }
        notifyPropertyChanged(BR.lastMsgContent);
    }

    @Bindable
    public String getLastMsgContent() {
        return mLastMsgContent;
    }

    @Bindable
    public String getName() {
        return mName;
    }

    @Bindable
    public String getTime() {
        return mTime;
    }

    @Bindable
    public Integer getUnReadCount() {
        return mUnReadCount;
    }

    @Bindable
    public String getAvatar() {
        return mAvatar;
    }

    public ZIMConversation getConversation() {
        return mConversation;
    }

    public String getConversationID() {
        return mConversation.conversationID;
    }

    public ZIMConversationType getType() {
        return mConversation.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZIMKitConversationModel model = (ZIMKitConversationModel) o;
        return Objects.equals(model.getConversation().conversationID, mConversation.conversationID) && Objects.equals(model.getConversation().orderKey, mConversation.orderKey) && Objects.equals(model.getConversation().type.value(), mConversation.type.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mConversation.conversationID, mConversation.orderKey, mConversation.type.value());
    }
}
