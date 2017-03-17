package nhannt.musicplayer.ui.drawer;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * Created by nhannt on 07/03/2017.
 */

public class DrawerPresenterImpl implements DrawerPresenter, DrawerListener {

    DrawerInteractorImpl drawerInteractor;
    DrawerView drawerView;

    public DrawerPresenterImpl(DrawerView drawerView) {
        this.drawerView = drawerView;
        drawerInteractor = new DrawerInteractorImpl();
    }

    @Override
    public void fragmentReplace(Fragment fragment) {
        drawerView.navigateUsingTo(fragment);
    }

    @Override
    public void navigationItemSelected(MenuItem item, DrawerLayout drawerLayout) {
        drawerInteractor.navigateTo(item, drawerLayout, this);
    }

    public interface DrawerView {
        void navigateUsingTo(Fragment fragment);
    }
}
