package nhannt.musicplayer.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;


/**
 * Created by NhanNT on 04/25/2017.
 */

public class SystemUtils {
    private SystemUtils() {

    }
    public static int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
            return result;
        }
        return 0;
    }
    public static int getActionBarHeight(Context context) {
        final TypedArray ta = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        return (int) ta.getDimension(0, 0);
    }


    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
