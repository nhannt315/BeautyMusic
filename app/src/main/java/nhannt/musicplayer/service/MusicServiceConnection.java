package nhannt.musicplayer.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import nhannt.musicplayer.interfaces.IMusicServiceConnection;

/**
 * Created by nhannt on 01/03/2017.
 */

public class MusicServiceConnection {
    final Context context;
    MusicService mService;
    IMusicServiceConnection connection;
    Intent intent;

    public MusicServiceConnection(Context context) {
        this.context = context;
    }

    public void connect(Intent intent, IMusicServiceConnection connection) {
        this.connection = connection;
        context.startService(intent);
        context.bindService(intent, mConnection, 0);
        this.intent = intent;
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MusicService.LocalBinder) service).getService();
            connection.onConnected(mService);
            context.unbindService(mConnection);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };
}
