package nhannt.musicplayer.ui.home;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nhannt.musicplayer.R;
import nhannt.musicplayer.ui.base.BaseActivity;
import nhannt.musicplayer.utils.AppController;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.ui.itemlist.FragmentMain;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends BaseActivity implements DrawerPresenterImpl.DrawerView, NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 200;

    @BindView(R.id.drawer_main)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.current_play_bar)
    protected LinearLayout currentPlayBar;
    @BindView(R.id.btn_toggle_play_current_bar)
    protected ImageView btnTogglePlay;
    @BindView(R.id.iv_cover_current_playing_bar)
    protected CircleImageView ivAlbumCurrentBar;
    @BindView(R.id.tv_artist_current_bar)
    protected TextView tvArtistCurrentBar;
    @BindView(R.id.tv_song_title_current_bar)
    protected TextView tvSongTitleCurrentBar;
    @BindView(R.id.seek_bar_current_bar)
    protected SeekBar seekBar;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    private DrawerPresenterImpl drawerPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        drawerPresenter = new DrawerPresenterImpl(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, getToolbar(),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        navigationView.getMenu().performIdentifierAction(R.id.btn_lib_nav, 0);
        toggle.syncState();
        if (Common.isMarshMallow()) {
            if (!checkPermission()) {
                requestPermission();
            }else{
                doMainWork();
            }
        } else {
            doMainWork();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }


    private void doMainWork() {
        showFragment(FragmentMain.newInstance(), FragmentMain.TAG);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_now_playing_nav:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void navigateUsingTo(Fragment fragment) {
        showFragment(fragment, "");
    }
}
