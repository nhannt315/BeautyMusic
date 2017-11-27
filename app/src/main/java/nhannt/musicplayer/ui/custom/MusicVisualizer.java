package nhannt.musicplayer.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

public class MusicVisualizer extends View {
    private Runnable animateView;
    Paint paint;
    Random random;

    class AnimateView implements Runnable {
        AnimateView() {
        }

        public void run() {
            MusicVisualizer.this.postDelayed(this, 150);
            MusicVisualizer.this.invalidate();
        }
    }

    public MusicVisualizer(Context context) {
        super(context);
        this.random = new Random();
        this.paint = new Paint();
        this.animateView = new AnimateView();
        MusicVisualizer musicVisualizer = new MusicVisualizer(context, null);
    }

    public MusicVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.random = new Random();
        this.paint = new Paint();
        this.animateView = new AnimateView();
        removeCallbacks(this.animateView);
        post(this.animateView);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setStyle(Style.FILL);
        canvas.drawRect((float) getDimensionInPixel(0), (float) (getHeight() - (this.random.nextInt(((int) (((float) getHeight()) / 1.5f)) - 19) + 20)), (float) getDimensionInPixel(7), (float) getHeight(), this.paint);
        canvas.drawRect((float) getDimensionInPixel(10), (float) (getHeight() - (this.random.nextInt(((int) (((float) getHeight()) / 1.5f)) - 19) + 20)), (float) getDimensionInPixel(17), (float) getHeight(), this.paint);
        canvas.drawRect((float) getDimensionInPixel(20), (float) (getHeight() - (this.random.nextInt(((int) (((float) getHeight()) / 1.5f)) - 19) + 20)), (float) getDimensionInPixel(27), (float) getHeight(), this.paint);
    }

    public void setColor(int color) {
        this.paint.setColor(color);
        invalidate();
    }

    private int getDimensionInPixel(int dp) {
        return (int) TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics());
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            removeCallbacks(this.animateView);
            post(this.animateView);
        } else if (visibility == GONE) {
            removeCallbacks(this.animateView);
        }
    }
}
