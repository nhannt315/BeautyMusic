package nhannt.musicplayer.ui.drawer;

import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import nhannt.musicplayer.R;
import nhannt.musicplayer.ui.itemlist.FragmentMain;

/**
 * Created by nhannt on 07/03/2017.
 */

public class DrawerInteractorImpl implements DrawerInteractor {
    @Override
    public void navigateTo(MenuItem item, DrawerLayout drawerLayout, DrawerListener drawerListener) {
        switch (item.getItemId()){
            case R.id.btn_lib_nav:
                drawerListener.fragmentReplace(FragmentMain.newInstance());
                break;
            case R.id.btn_playlist_nav:
                break;
            case R.id.btn_play_queue_nav:
                break;
            case R.id.btn_now_playing_nav:
                break;
            case R.id.btn_about_nav:
                break;
            case R.id.btn_license_nav:
                break;
            case R.id.btn_feedback_nav:
                break;
        }
    }
}
