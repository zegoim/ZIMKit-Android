package im.zego.zimkitcommon.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import im.zego.zimkitcommon.BR;

public class TitleBarModel extends BaseObservable {
    private String mTitle = "ZegoIM";

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
        notifyPropertyChanged(BR.title);
    }
}
