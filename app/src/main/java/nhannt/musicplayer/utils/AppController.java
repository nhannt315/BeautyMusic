package nhannt.musicplayer.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by nhannt on 07/03/2017.
 */

public class AppController extends Application {
    private static AppController mInstance;


    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    public static AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
