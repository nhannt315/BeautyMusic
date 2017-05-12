package nhannt.musicplayer.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SearchAdapter;
import nhannt.musicplayer.interfaces.DrawerLayoutContainer;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.albumdetail.FragmentAlbumDetail;
import nhannt.musicplayer.ui.albumdetail.IAlbumDetailView;
import nhannt.musicplayer.ui.artistdetail.FragmentArtistDetail;
import nhannt.musicplayer.ui.base.BaseActivity;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.custom.MaterialSearchView;
import nhannt.musicplayer.ui.itemlist.FragmentMain;
import nhannt.musicplayer.ui.playback.PlayBackActivity;
import nhannt.musicplayer.ui.playingqueue.FragmentPlayingQueue;
import nhannt.musicplayer.ui.playlist.FragmentPlaylist;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.Navigator;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends BaseActivity implements IMusicServiceConnection, IHomeView, DrawerLayoutContainer, RecyclerItemClickListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    public static final String ACTION_OPEN_ALBUM_DETAIL = "open_album_detail";
    public static final String ACTION_OPEN_ARTIST_DETAIL = "open_artist_detail";

    public static final String KEY_ALBUM_DETAIL = "album_detail";
    public static final String KEY_ARTIST_DETAIL = "artist_detail";

    @BindView(R.id.drawer_main)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.current_play_bar)
    protected CardView currentPlayBar;
    @BindView(R.id.btn_toggle_play_current_bar)
    protected ImageView btnTogglePlay;
    @BindView(R.id.iv_cover_current_playing_bar)
    protected CircleImageView ivAlbumCurrentBar;
    @BindView(R.id.tv_artist_current_bar)
    protected TextView tvArtistCurrentBar;
    @BindView(R.id.tv_song_title_current_bar)
    protected TextView tvSongTitleCurrentBar;
    @BindView(R.id.seek_bar_current_bar)
    protected ProgressBar seekBar;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    private MusicService mService;
    private HomePresenter mPresenter;
    private View header;
    private TextView tvSongTitleHeader;
    private TextView tvArtistHeader;
    private ImageView albumCoverHeader;
    public static int navItemIndex = 0;

    private static final String TAG_LIBRARY = FragmentMain.TAG;
    private static final String TAG_PLAYLISTS = FragmentPlaylist.TAG;
    private static final String TAG_PLAYING_QUEUE = FragmentPlayingQueue.TAG;
//    private static final String TAG_NOW_PLAYING= PlayBackActivity.TAG;

    public static String CURRENT_TAG = TAG_LIBRARY;
    private static Handler mHandler;
    private ActionBarDrawerToggle toggle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        initToolbar();
        ButterKnife.bind(this);
        initSetting();

        if (Common.isMarshMallow()) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                doMainWork();
            }
        } else {
            doMainWork();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void loadHomeFragment() {
        selectNavMenu();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.container, fragment, CURRENT_TAG);
                fragmentTransaction.commit();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawerLayout.closeDrawers();

        invalidateOptionsMenu();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // library
                FragmentMain homeFragment = FragmentMain.newInstance();
                return homeFragment;
            case 1:
                // playlist
                FragmentPlaylist fragmentPlaylist = FragmentPlaylist.newInstance();
                return fragmentPlaylist;
            case 2:
                // playing queue
                FragmentPlayingQueue fragment = FragmentPlayingQueue.newInstance();
                return fragment;
            default:
                return FragmentMain.newInstance();
        }

    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void initSetting() {
        mHandler = new Handler();

        navigationView.getMenu().performIdentifierAction(R.id.btn_lib_nav, 0);

        header = navigationView.getHeaderView(0);
        tvSongTitleHeader = (TextView) header.findViewById(R.id.tv_song_name_nav_header);
        tvArtistHeader = (TextView) header.findViewById(R.id.tv_artist_name_nav_header);
        albumCoverHeader = (ImageView) header.findViewById(R.id.iv_nav_header_back_ground);

        setupNavigationView();
        mPresenter = new HomePresenter();
        mPresenter.attachedView(this);
        addOnMusicServiceListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                return true;
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_lib_nav:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_LIBRARY;
                        break;
                    case R.id.btn_playlist_nav:
                        CURRENT_TAG = TAG_PLAYLISTS;
                        navItemIndex = 1;
                        break;
                    case R.id.btn_play_queue_nav:
                        CURRENT_TAG = TAG_PLAYING_QUEUE;
                        navItemIndex = 2;
                        break;
                    case R.id.btn_now_playing_nav:
                        navItemIndex = 3;
                        startActivity(new Intent(HomeActivity.this, PlayBackActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.btn_about_nav:
                        break;
                    case R.id.btn_license_nav:
                        break;
                    case R.id.btn_feedback_nav:
                        break;
                    default:
                        navItemIndex = 0;
                }

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });


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
        navigationView.getMenu().getItem(0).setChecked(true);
        showFragment(FragmentMain.newInstance(), FragmentMain.TAG);
        btnTogglePlay.setOnClickListener(mPresenter);
        currentPlayBar.setOnClickListener(mPresenter);
        setupSearchView();
        Intent intent = getIntent();
        switch (intent.getAction()) {
            case ACTION_OPEN_ALBUM_DETAIL:
                Album album = (Album) intent.getSerializableExtra(KEY_ALBUM_DETAIL);
                Navigator.navigateToAlbumDetail(this, album, null);
                break;
            case ACTION_OPEN_ARTIST_DETAIL:
                Artist artist = (Artist) intent.getSerializableExtra(KEY_ARTIST_DETAIL);
                Navigator.navigateToArtistDetail(this, artist, null);
                break;
        }
    }

    private void setupSearchView() {
        searchAdapter = new SearchAdapter(this);
        lstSearchResult = new ArrayList();
        searchAdapter.setItemClickListener(this);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.PLAY_STATE_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPresenter.getReceiver(), filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPresenter.getReceiver());
    }

    @Override
    protected ArrayList getSearchResultList(String query) {
        FragmentMain fragmentMain = (FragmentMain) getSupportFragmentManager().findFragmentByTag(FragmentMain.TAG);
        if (fragmentMain != null && fragmentMain.isVisible()) {
            return mPresenter.searchAll(query);
        }

        FragmentAlbumDetail fragmentAlbum = (FragmentAlbumDetail) getSupportFragmentManager().findFragmentByTag(FragmentAlbumDetail.TAG);
        if (fragmentAlbum != null && fragmentAlbum.isVisible()) {
            return mPresenter.searchAlbumDetail(query, fragmentAlbum.getAlbum().getId());
        }

        FragmentArtistDetail fragmentArtist = (FragmentArtistDetail) getSupportFragmentManager().findFragmentByTag(FragmentArtistDetail.TAG);
        if (fragmentArtist != null && fragmentArtist.isVisible()) {
            return mPresenter.searchArtistDetail(query, fragmentArtist.getArtistId());
        }
        return new ArrayList();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START) || searchView.isSearchOpen()) {
            if (searchView.isSearchOpen())
                searchView.closeSearch();
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
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
    public Context getViewContext() {
        return this;
    }

    @Override
    public void updateSeekBar(int currentTime, int totalTime) {
        seekBar.setMax(totalTime);
        seekBar.setProgress(currentTime);
    }

    @Override
    public void updatePlayPauseState() {
        if (mService.getState() == MusicService.MusicState.Playing) {
            btnTogglePlay.setImageResource(R.drawable.ic_pause_red_600_24dp);
        } else {
            btnTogglePlay.setImageResource(R.drawable.ic_play_arrow_red_600_24dp);
        }
    }

    @Override
    public MusicService getMusicService() {
        return this.mService;
    }

    @Override
    public void updateSongInfo() {
        if (mService == null) return;
        Song song = mService.getCurrentSong();
        if (song == null) return;
        //Current bar
        tvArtistCurrentBar.setText(song.getArtist());
        tvSongTitleCurrentBar.setText(song.getTitle());
        Glide.with(this).load(song.getCoverPath())
                .placeholder(R.drawable.music_background)
                .centerCrop()
                .dontAnimate()
                .into(ivAlbumCurrentBar);
        //Navigation header
        tvArtistHeader.setText(song.getArtist());
        tvSongTitleHeader.setText(song.getTitle());
        Glide.with(this).load(song.getCoverPath())
                .centerCrop()
                .placeholder(R.drawable.music_background)
                .into(albumCoverHeader);
    }

    @Override
    public void onConnected(MusicService service) {
        this.mService = service;
        updateSongInfo();
        mPresenter.updateTimePlay();
        updatePlayPauseState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mPresenter = null;
    }

    @Override
    public void setDrawerLayoutActionBarToggle(Toolbar toolbar) {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if (searchView.isSearchOpen())
            searchView.closeSearch();
    }
}
