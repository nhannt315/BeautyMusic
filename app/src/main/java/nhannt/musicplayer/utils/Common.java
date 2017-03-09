package nhannt.musicplayer.utils;

import android.os.Build;

/**
 * Created by nhannt on 09/03/2017.
 */

public class Common {
    /**
     * @return true when the caller API version is at least lollipop 21
     */
    public static boolean isMarshMallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
