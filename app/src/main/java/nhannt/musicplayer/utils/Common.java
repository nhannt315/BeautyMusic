package nhannt.musicplayer.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;

import java.util.Collections;
import java.util.List;

/**
 * Created by nhannt on 09/03/2017.
 */

public class Common {

    public static final String FIRST_LAUNCH = "first_launch";
    public static final String ALBUM_VIEW_MODE = "album_view_mode";
    public static final String ARTIST_VIEW_MODE = "artist_view_mode";
    public static final String SONG_SORT_MODE = "song_sort_mode";
    public static final String ALBUM_SORT_MODE = "album_sort_mode";
    public static final String ARTIST_SORT_MODE = "artist_sort_mode";

    public static final String LIST_SONG = "list_songs";
    public static final String CURRENT_PLAY = "current_play";

    /**
     * @return true when the caller API version is at least marshmallow 23
     */
    public static boolean isMarshMallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * @return true when the caller API version is at least nougat 24
     */
    public static boolean isNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * @return true when the caller API version is at least lollipop 21
     */
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    public static String miliSecondToString(long pTime) {
        int second = (int) (pTime/1000);
        return String.format("%02d:%02d", second/60, second % 60);
    }
    public static <T> List<T> safeSubList(List<T> list, int fromIndex, int toIndex) {
        int size = list.size();
        if (fromIndex >= size || toIndex <= 0 || fromIndex >= toIndex) {
            return Collections.emptyList();
        }

        fromIndex = Math.max(0, fromIndex);
        toIndex = Math.min(size, toIndex);

        return list.subList(fromIndex, toIndex);
    }

    /**
     *
     * @param bmp input bitmap
     * @param contrast 0..10 ,1 is default
     * @param brightness -255..255 ,0 is default
     * @return new bitmap
     */
    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }
}
