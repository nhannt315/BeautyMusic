package nhannt.musicplayer.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by nhannt on 07/03/2017.
 */

public class SquareRelativeLayoutVert extends RelativeLayout {
    public SquareRelativeLayoutVert(Context context) {
        super(context);
    }

    public SquareRelativeLayoutVert(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeLayoutVert(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
