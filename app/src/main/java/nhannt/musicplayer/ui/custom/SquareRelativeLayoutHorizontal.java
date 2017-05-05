package nhannt.musicplayer.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by NhanNT on 05/05/2017.
 */

public class SquareRelativeLayoutHorizontal extends RelativeLayout {
    public SquareRelativeLayoutHorizontal(Context context) {
        super(context);
    }

    public SquareRelativeLayoutHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeLayoutHorizontal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public SquareRelativeLayoutHorizontal(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(heightMeasureSpec,heightMeasureSpec);
    }
}
