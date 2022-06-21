package im.zego.zimkitmessages.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import im.zego.zimkitmessages.R;

public class ZIMKitMessageTimeLineDecoration extends RecyclerView.ItemDecoration {
    private final DecorationCallback mCallBack;
    private TextPaint mTextPaint;
    private float topGap;
    private int paddingTop;

    public ZIMKitMessageTimeLineDecoration(Context context, DecorationCallback decorationCallback) {
        mCallBack = decorationCallback;
        Resources res = context.getResources();
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(res.getColor(im.zego.zimkitcommon.R.color.color_b8b8b8));
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        Rect rect = new Rect();
        mTextPaint.getTextBounds(res.getString(R.string.message_test_time), 0, res.getString(R.string.message_test_time).length(), rect);
        int h = rect.height();

        paddingTop = res.getDimensionPixelSize(R.dimen.message_time_decoration_top_padding);
        topGap = h + res.getDimensionPixelSize(R.dimen.message_time_decoration_bottom_padding) + paddingTop;// measure height
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            boolean needAddTimeLine = mCallBack.needAddTimeLine(position);
            if (needAddTimeLine) {
                String time = mCallBack.getTimeLine(position);
                if (TextUtils.isEmpty(time)) return;
                float top = view.getTop() - topGap;
                float bottom = view.getTop();
                float yPos = (((bottom - top) / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2)) + top;
                c.drawText(time, (left + right) >> 1, yPos, mTextPaint);//绘制文本
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        boolean needAddTimeLine = mCallBack.needAddTimeLine(pos);
        if (needAddTimeLine) {
            outRect.top = (int) topGap;
        } else {
            outRect.top = 0;
        }
    }

    public interface DecorationCallback {
        boolean needAddTimeLine(int position);

        String getTimeLine(int position);
    }
}
