package im.zego.zimkitcommon.adapter;

import android.view.View;

import androidx.databinding.BindingAdapter;

import im.zego.zimkitcommon.components.widget.UnreadCountView;

public class ZIMKitBindingAdapter {
    @BindingAdapter("unReadCount")
    public static void setCount(UnreadCountView view, int count) {
        if (count <= 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            String strCount = count + "";
            if (count > 99) {
                strCount = "99+";
            }
            view.setText(strCount);
        }
    }
}
