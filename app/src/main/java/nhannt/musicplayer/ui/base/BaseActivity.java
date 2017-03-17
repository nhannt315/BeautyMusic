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

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.service.MusicService;

/**
 * Created by nhannt on 01/03/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private IMusicServiceConnection iConnection = null;
    private MusicService mService;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(getLayout());
        settingToolbar();
        ButterKnife.bind(this);
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
            Intent iMusicService = new Intent(this, MusicService.class);
            unbindService(connection);
        }
    }

    public void showFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.add(R.id.container, fragment, tag);
        fragmentTransaction.commit();
    }

    public void popFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
