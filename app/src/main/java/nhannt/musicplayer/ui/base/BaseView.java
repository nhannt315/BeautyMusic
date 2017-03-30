package nhannt.musicplayer.ui.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by nhannt on 17/03/2017.
 */

public interface BaseView {
    Context getViewContext();

    AppCompatActivity getViewActivity();
}
