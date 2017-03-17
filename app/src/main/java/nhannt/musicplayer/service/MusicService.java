package nhannt.musicplayer.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.ArrayList;

import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.receiver.RemoteReceiver;
import nhannt.musicplayer.utils.Common;

/**
 * Created by nhannt on 01/03/2017.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {

    private IBinder mBinder = new LocalBinder();
    private MediaPlayer mediaPlayer;

    public enum MusicState {
        Preparing,
        Stop,
        Playing,
        Pause
    }


    private MusicState musicState = MusicState.Stop;
    private ArrayList<Song> lstSong;
    private int songPos;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private static final int NOTIFY_ID = 1;
    private MediaSessionCompat mediaSession;
    private AudioManager audioManager;
    private int audioManagerResult;

    public static final String ACTION_PLAY = "nhannt.app.musicplayer.ACTION_PLAY";
    public static final String ACTION_TOGGLE_PLAY_PAUSE = "nhannt.app.musicplayer.ACTION_TOGGLE_PLAY_PAUSE";
    public static final String ACTION_PAUSE = "nhannt.app.musicplayer.ACTION_PAUSE";
    public static final String ACTION_NEXT = "nhannt.app.musicplayer.ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "nhannt.app.musicplayer.ACTION_PREVIOUS";
    public static final String ACTION_EXIT = "nhannt.app.musicplayer.ACTION_EXIT";

    public static final String META_CHANGE = "nhannt.app.musicplayer.META_CHANGE";
    public static final String PLAY_STATE_CHANGE = "nhannt.app.musicplayer.STATE_CHANGE";
    public static final String EXIT = "nhannt.app.musicplayer.EXIT";

    @Override
    public void onCreate() {
        super.onCreate();
        initMusicPlayer();
        audioManagerResult = audioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    private void initMusicPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(MusicService.this);
        mediaPlayer.setOnSeekCompleteListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (!action.isEmpty()) {
                if (action.equals(ACTION_TOGGLE_PLAY_PAUSE)) {
                    if (getState() == MusicState.Playing)
                        pauseSong();
                    else
                        playPauseSong();
                } else if (action.equals(ACTION_PAUSE)) {
                    pauseSong();
                } else if (action.equals(ACTION_NEXT)) {
                    nextSong();
                } else if (action.equals(ACTION_PREVIOUS)) {
                    previousSong();
                } else if (action.equals(ACTION_EXIT)) {
                    stopSelf();
                } else if (action.equals(ACTION_PLAY)) {
                    ArrayList list = (ArrayList) intent.getSerializableExtra(Common.LIST_SONG);
                    int curPos = intent.getIntExtra(Common.CURRENT_PLAY, 0);
                    if (list != null) {
                        setLstSong(list);
                        setSongPos(curPos);
                        playSong();
                    }
                }

            }
        }
        return START_NOT_STICKY;
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    pauseSong();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    playPauseSong();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    pauseSong();
                    break;
            }
        }
    };

    public void showLockScreen() {
        Song currentSong = lstSong.get(songPos);
        ComponentName receiver = new ComponentName(getPackageName(), RemoteReceiver.class.getName());
        mediaSession = new MediaSessionCompat(this, "MusicService", receiver, null);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build());
        Bitmap cover = BitmapFactory.decodeFile(lstSong.get(songPos).getCoverPath());
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentSong.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, currentSong.getAlbum() + " - " + currentSong.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentSong.getTitle())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, cover)
                .build());
        mediaSession.setActive(true);
    }

    public void playSong() {
        if (mediaPlayer == null) return;
        if (lstSong == null || lstSong.size() == 0) return;
        Song song = lstSong.get(songPos);
        try {
            mediaPlayer.reset();
            String path = song.getSongPath();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            notifyClients(PLAY_STATE_CHANGE);
            setState(MusicState.Preparing);
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
    }

    public void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            setState(MusicState.Stop);
            notifyClients(PLAY_STATE_CHANGE);
        }
    }

    public void pauseSong() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying() && getState() == MusicState.Playing) {
                mediaPlayer.pause();
                setState(MusicState.Pause);
                notifyClients(PLAY_STATE_CHANGE);
//                updateNotification();
            }
        }
    }

    public void playPauseSong() {
        if (mediaPlayer != null) {
            if (getState() == MusicState.Pause) {
                mediaPlayer.start();
//                startForeground(NOTIFY_ID, createNotification(getCurrentSong()));
                setState(MusicState.Playing);
                notifyClients(PLAY_STATE_CHANGE);
//                updateNotification();
            }
        }
    }

    public void previousSong() {
        if ((getState() != MusicState.Pause) && (getState() != MusicState.Playing)) return;
        stopSong();
//        songPos = getPrePosition();
        playSong();
    }

    public void nextSong() {
        if ((getState() != MusicState.Pause) && (getState() != MusicState.Playing)) return;
        stopSong();
//        songPos = getNextPosition();
        playSong();
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.release();
            musicState = MusicState.Stop;
            mediaPlayer = null;
        }
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        setState(MusicState.Stop);
        notifyClients(EXIT);
        releaseMediaPlayer();
        audioManager.abandonAudioFocus(afChangeListener);
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (getCurrentPosition() == 0) return;
        if (mediaPlayer != null) {
            nextSong();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        if (getState() != MusicState.Preparing) return;
        mediaPlayer.start();
        setState(MusicState.Playing);
        notifyClients(PLAY_STATE_CHANGE);
        notifyClients(META_CHANGE);
        //TODO intert into recent play database
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        setState(MusicState.Playing);
        mediaPlayer.start();
        notifyClients(PLAY_STATE_CHANGE);
    }

    private void notifyClients(String what, Bundle bundle) {
        Intent intent = new Intent(what);
        if (bundle != null)
            intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void notifyClients(String what) {
        notifyClients(what, null);
    }

    public void setState(MusicState state) {
        this.musicState = state;
    }

    public MusicState getState() {
        return this.musicState;
    }

    public ArrayList<Song> getLstSong() {
        return lstSong;
    }

    public void setLstSong(ArrayList<Song> lstSong) {
        this.lstSong = lstSong;
    }

    public int getSongPos() {
        return songPos;
    }

    public void setSongPos(int songPos) {
        this.songPos = songPos;
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
