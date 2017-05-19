package nhannt.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import nhannt.musicplayer.R;
import nhannt.musicplayer.data.database.DBQuery;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.playback.PlayBackActivity;
import nhannt.musicplayer.utils.Common;

/**
 * Created by nhannt on 01/03/2017.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {

    private final IBinder mBinder = new LocalBinder();
    private MediaPlayer mediaPlayer;

    public enum MusicState {
        Preparing,
        Stop,
        Playing,
        Pause,
        Buffering
    }


    private MusicState musicState = MusicState.Stop;
    private ArrayList<Song> lstSong;
    private int songPos = -1;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private static final int NOTIFY_ID = 1;
    private MediaSessionCompat mediaSession;
    private AudioManager audioManager;
    private int audioManagerResult;

    private Random rand;
    private Vector<Integer> histories;

    public static final String ACTION_PLAY = "nhannt.musicplayer.ACTION_PLAY";
    public static final String ACTION_TOGGLE_PLAY_PAUSE = "nhannt.musicplayer.ACTION_TOGGLE_PLAY_PAUSE";
    public static final String ACTION_PAUSE = "nhannt.musicplayer.ACTION_PAUSE";
    public static final String ACTION_NEXT = "nhannt.musicplayer.ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "nhannt.musicplayer.ACTION_PREVIOUS";
    public static final String ACTION_EXIT = "nhannt.musicplayer.ACTION_EXIT";

    public static final String META_CHANGE = "nhannt.musicplayer.META_CHANGE";
    public static final String PLAY_STATE_CHANGE = "nhannt.musicplayer.STATE_CHANGE";
    public static final String EXIT = "nhannt.musicplayer.EXIT";

    @Override
    public void onCreate() {
        super.onCreate();
        initMusicPlayer();
        initOther();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManagerResult = audioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

    }

    private void initOther() {
        rand = new Random();
        histories = new Vector<>();
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
            if (action != null && !action.isEmpty()) {
                if (action.equals(ACTION_TOGGLE_PLAY_PAUSE)) {
                    if (getState() == MusicState.Playing)
                        pauseSong();
                    else
                        resumeSong();
                } else if (action.equals(ACTION_PAUSE)) {
                    pauseSong();
                } else if (action.equals(ACTION_NEXT)) {
                    skipToNextSong();
                    Log.d("next", "song");
                } else if (action.equals(ACTION_PREVIOUS)) {
                    skipToPrevSong();
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

    private final MediaSessionCompat.Callback mMediaSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            final String intentAction = mediaButtonEvent.getAction();
            if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
                final KeyEvent event = mediaButtonEvent.getParcelableExtra(
                        Intent.EXTRA_KEY_EVENT);
                if (event == null) {
                    return super.onMediaButtonEvent(mediaButtonEvent);
                }
                final int keycode = event.getKeyCode();
                final int action = event.getAction();
                if (event.getRepeatCount() == 0 && action == KeyEvent.ACTION_DOWN) {

                    switch (keycode) {
                        // Do what you want in here
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                            if (getState() == MusicState.Playing)
                                pauseSong();
                            else
                                resumeSong();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            skipToNextSong();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            skipToPrevSong();
                            break;
                    }
                }
            }
            return super.onMediaButtonEvent(mediaButtonEvent);
        }
    };

    private final AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    pauseSong();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    resumeSong();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    pauseSong();
                    break;
            }
        }
    };

    private void updateNotification() {
        showLockScreen();
        setStatePlayPauseLockScreen();
        Notification notify = createNotification(getCurrentSong());
        if (getState() == MusicState.Playing) {
            startForeground(NOTIFY_ID, notify);
            Log.d("notification", "update startForeground");
        } else if (getState() == MusicState.Pause || getState() == MusicState.Stop) {
            stopForeground(true);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFY_ID, notify);
            Log.d("notification", "update clearable notification");
        }

    }

    private Notification createNotification(Song song) {
        Intent notificationIntent = new Intent(this, PlayBackActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(contentIntent);

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        RemoteViews bigNotificationView = new RemoteViews(getPackageName(), R.layout.notification_layout_expanded);

        if (getState() == MusicState.Playing) {
            notificationView.setImageViewResource(R.id.bt_play_pause_notification, R.drawable.ic_pause_black_24dp);
            bigNotificationView.setImageViewResource(R.id.bt_play_pause_notification, R.drawable.ic_pause_black_24dp);
        } else {
            notificationView.setImageViewResource(R.id.bt_play_pause_notification, R.drawable.ic_play_arrow_black_24dp);
            bigNotificationView.setImageViewResource(R.id.bt_play_pause_notification, R.drawable.ic_play_arrow_black_24dp);
        }

        if (song.getCoverPath() != null && !song.getCoverPath().isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(song.getCoverPath());
            notificationView.setImageViewBitmap(R.id.img_album_art_notification, bitmap);
            bigNotificationView.setImageViewBitmap(R.id.img_album_art_notification, bitmap);
        } else {
            notificationView.setImageViewResource(R.id.img_album_art_notification, R.mipmap.ic_launcher);
            bigNotificationView.setImageViewResource(R.id.img_album_art_notification, R.mipmap.ic_launcher);
        }

        notificationView.setTextViewText(R.id.tv_song_name_notification, getCurrentSong().getTitle());
        notificationView.setTextViewText(R.id.tv_song_info_notification, getCurrentSong().getArtist() + " - " + getCurrentSong().getAlbum());

        bigNotificationView.setTextViewText(R.id.tv_song_name_notification, getCurrentSong().getTitle());
        bigNotificationView.setTextViewText(R.id.tv_song_info_notification, getCurrentSong().getArtist() + " - " + getCurrentSong().getAlbum());

        PendingIntent piPlayPause = PendingIntent.getService(getApplicationContext(), 0, new Intent(ACTION_TOGGLE_PLAY_PAUSE),
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piSkipNext = PendingIntent.getService(getApplicationContext(), 0, new Intent(ACTION_NEXT),
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piSkipPrev = PendingIntent.getService(getApplicationContext(), 0, new Intent(ACTION_PREVIOUS),
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationView.setOnClickPendingIntent(R.id.bt_play_pause_notification, piPlayPause);
        notificationView.setOnClickPendingIntent(R.id.bt_next_notification, piSkipNext);
        notificationView.setOnClickPendingIntent(R.id.bt_prev_notification, piSkipPrev);

        bigNotificationView.setOnClickPendingIntent(R.id.bt_play_pause_notification, piPlayPause);
        bigNotificationView.setOnClickPendingIntent(R.id.bt_next_notification, piSkipNext);
        bigNotificationView.setOnClickPendingIntent(R.id.bt_prev_notification, piSkipPrev);

        builder.setContent(notificationView);
        builder.setCustomBigContentView(bigNotificationView);

        return builder.build();
    }

    private void showLockScreen() {
        if(!isSongSetted()) return;
        Song currentSong = lstSong.get(songPos);
        mediaSession = new MediaSessionCompat(this, MusicService.class.getName());
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(mMediaSessionCallback);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
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

    private void setStatePlayPauseLockScreen() {
        if (mediaSession.getController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
            mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                    .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE
                            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                    .build());
        } else {
            mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                    .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                    .build());
        }
    }

    public void playNext(Song song) {
        if (lstSong == null) {
            lstSong = new ArrayList<>();
            lstSong.add(song);
            songPos = 0;
            playSong();
        } else {
            lstSong.add(songPos + 1, song);
        }
    }

    public boolean isSongSetted(){
//        if(lstSong == null || songPos == -1)
//            return false;
//        else
//            return true;

        return  !(lstSong == null || songPos == -1);
    }

    public void addToQueue(Song... song) {
        if (lstSong == null) {
            lstSong = new ArrayList<>();
            lstSong.add(song[0]);
            songPos = 0;
            playSong();
        } else {
            lstSong.add(song[0]);
        }
        Toast.makeText(this, "1 " + getString(R.string.success_add_to_queue), Toast.LENGTH_SHORT).show();
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
            if (audioManagerResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaPlayer.start();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }

    }

    private void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            setState(MusicState.Stop);
            notifyClients(PLAY_STATE_CHANGE);
        }
    }

    public Song getCurrentSong() {
        if (lstSong == null) return null;
        if (songPos < 0) songPos = 0;
        if (songPos >= lstSong.size()) return null;
        return lstSong.get(songPos);
    }

    public void pauseSong() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying() && getState() == MusicState.Playing) {
                mediaPlayer.pause();
                setState(MusicState.Pause);
                notifyClients(PLAY_STATE_CHANGE);
                updateNotification();
            }
        }
    }

    public void resumeSong() {
        if (mediaPlayer != null) {
            if (getState() == MusicState.Pause) {
                mediaPlayer.start();
                setState(MusicState.Playing);
                updateNotification();
                notifyClients(PLAY_STATE_CHANGE);
            }
        }
        updateNotification();
    }

    public void skipToPrevSong() {
        if ((getState() != MusicState.Pause) && (getState() != MusicState.Playing)) return;
        stopSong();
        songPos = getPrePosition();
        playSong();
    }

    private int getPrePosition() {
        if (isRepeat) return songPos;
        int newSongPosition;
        if (isShuffle) {
            if (histories.size() <= 0) {
                newSongPosition = songPos;
                while (newSongPosition == songPos)
                    newSongPosition = rand.nextInt(lstSong.size());
                return newSongPosition;
            }
            newSongPosition = histories.get(histories.size() - 1);
            histories.remove(histories.size() - 1);
        } else {
            if (songPos == 0) newSongPosition = lstSong.size() - 1;
            else {
                newSongPosition = --songPos;
            }
        }
        return newSongPosition;
    }

    public void skipToNextSong() {
        if ((getState() != MusicState.Pause) && (getState() != MusicState.Playing)) return;
        stopSong();
        songPos = getNextPosition();
        playSong();
    }

    private int getNextPosition() {
        if (songPos == (lstSong.size() - 1)) return 0;
        if (histories.size() > lstSong.size() - 1)
            histories.remove(0);
        if (isRepeat) return songPos;
        if (songPos < 0) return 0;
        if (isShuffle) {
            int newSongPos = songPos;
            if (histories.size() <= 0) {
                while (newSongPos == songPos || histories.contains(newSongPos)) {
                    newSongPos = rand.nextInt(lstSong.size());
                }
            } else {
                while (newSongPos == songPos)
                    newSongPos = rand.nextInt(lstSong.size());
            }
            return newSongPos;
        } else {
            return ++songPos;
        }
    }

    private void releaseMediaPlayer() {
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
            skipToNextSong();

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
        updateNotification();
        DBQuery.getInstance().insertRecentPlay(lstSong.get(songPos));
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

    public void seekTo(int mSec) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(mSec);
            setState(MusicState.Buffering);
            notifyClients(PLAY_STATE_CHANGE);
        }
    }

    private void notifyClients(String what) {
        notifyClients(what, null);
    }

    private void setState(MusicState state) {
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

    public boolean isShuffle() {
        return this.isShuffle;
    }

    public boolean isRepeat() {
        return this.isRepeat;
    }

    public void setShuffle(boolean isShuffle) {
        this.isShuffle = isShuffle;
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }
}
