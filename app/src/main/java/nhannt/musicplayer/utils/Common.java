package nhannt.musicplayer.utils;

import android.os.Build;

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
     * @return true when the caller API version is at least lollipop 21
     */
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static int percentSeekbar(int msCurrent, int msSongEnd) {
        return (int) ((msCurrent * 100.0f) / msSongEnd);
    }

    public static int msCurrentSeekbar(int percent, int msSongEnd) {
        return (msSongEnd * percent) / 100;
    }
}
