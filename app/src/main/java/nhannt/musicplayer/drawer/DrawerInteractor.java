package nhannt.musicplayer.drawer;

import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * Created by nhannt on 07/03/2017.
 */

public interface DrawerInteractor {
    void navigateTo(MenuItem item, DrawerLayout drawerLayout, DrawerListener drawerListener);
}
