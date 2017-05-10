package nhannt.musicplayer.ui.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SearchAdapter;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.interfaces.OnBackPressedListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.custom.MaterialSearchView;

/**
 * Created by nhannt on 01/03/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements MaterialSearchView.OnQueryTextListener {

    private IMusicServiceConnection iConnection = null;
    private MusicService mService;
    protected Toolbar toolbar;
    protected OnBackPressedListener backPressedListener;

    protected MaterialSearchView searchView;
    protected ArrayList lstSearchResult;
    protected SearchAdapter searchAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(getLayout());
        settingToolbar();
        ButterKnife.bind(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onQueryTextChange(query);
        return false;
    }

    public void setBackPressedListener(OnBackPressedListener backPressedListener) {
        this.backPressedListener = backPressedListener;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        if(lstSearchResult == null) return false;
        if (newText == null || newText.trim().isEmpty()) {
            lstSearchResult.clear();
            searchAdapter.updateSearchResult(lstSearchResult);
            searchView.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
            return false;
        }
        Log.d("searchKey", newText);
        lstSearchResult.clear();
        MediaProvider mediaProvider = MediaProvider.getInstance();
        ArrayList<Song> lstSong = mediaProvider.searchSongs(newText);
        ArrayList<Album> lstAlbum = mediaProvider.searchAlbums(newText);
        ArrayList<Artist> lstArtist = mediaProvider.searchArtists(newText);

        if (!lstSong.isEmpty()) {
            lstSearchResult.add(getString(R.string.song));
            Log.d("searchSongSize",lstSong.size()+"");
            if (lstSong.size() > 5)
                lstSong = new ArrayList<>(lstSong.subList(0, 4));
            lstSearchResult.addAll(lstSong);
        }
        if (!lstAlbum.isEmpty()) {
            lstSearchResult.add(getString(R.string.album));
            Log.d("searchAlbumSize",lstAlbum.size()+"");
            if (lstAlbum.size() > 5)
                lstAlbum = new ArrayList<>(lstAlbum.subList(0, 4));
            lstSearchResult.addAll(lstAlbum);
        }
        if (!lstArtist.isEmpty()) {
            lstSearchResult.add(getString(R.string.artist));
            Log.d("searchArtistSize",lstArtist.size()+"");
            if (lstArtist.size() > 5)
                lstArtist = new ArrayList<>(lstArtist.subList(0, 4));
            lstSearchResult.addAll(lstArtist);
        }
        searchAdapter.updateSearchResult(lstSearchResult);
        searchView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public void onBackPressed() {
        if (backPressedListener != null) {
            backPressedListener.doBack();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void settingToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        if (searchView != null)
            searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }


    protected Toolbar getToolbar() {
        return this.toolbar;
    }

    protected abstract int getLayout();

    protected void addOnMusicServiceListener(IMusicServiceConnection connection) {
        this.iConnection = connection;
    }

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mService = binder.getService();
            iConnection.onConnected(mService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (iConnection != null) {
            Intent iMusicService = new Intent(this, MusicService.class);
            bindService(iMusicService, connection, 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (iConnection != null) {
            unbindService(connection);
        }
    }

    public void showFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack("");
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.commit();
    }

    public void showFragmentAddToStack(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.commit();
    }


    public void popFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
