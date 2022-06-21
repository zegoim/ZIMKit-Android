package im.zego.zimkitmessages.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import im.zego.zim.entity.ZIMMessage;
import im.zego.zim.entity.ZIMTextMessage;
import im.zego.zim.enums.ZIMMessageDirection;

public class ZIMKitMessageItemModel extends BaseObservable {
    private String mAvatar;
    private String mNickName;
    private String mContent;
    private ZIMMessage mMessage;

    public ZIMKitMessageItemModel(ZIMMessage message) {
        if (message instanceof ZIMTextMessage) {
            this.mContent = ((ZIMTextMessage) message).message;
        }
        this.mMessage = message;
    }

    public ZIMMessage getMessage() {
        return mMessage;
    }

    public ZIMMessageDirection getDirection() {
        if (mMessage == null) {
            return null;
        } else {
            return mMessage.getDirection();
        }
    }

    public int getType() {
        if (mMessage == null) {
            return 99;
        } else {
            return mMessage.getType().value();
        }
    }

    @Bindable
    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String mAvatar) {
        this.mAvatar = mAvatar.toLowerCase().charAt(0) + "";
    }

    @Bindable
    public String getNickName() {
        return mNickName == null ? "" : mNickName;
    }

    public void setNickName(String nickName) {
        this.mNickName = nickName;
    }

    @Bindable
    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }
}
