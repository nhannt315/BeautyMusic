package nhannt.musicplayer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.util.ArrayList;

import nhannt.musicplayer.adapter.AlbumAdapter;
import nhannt.musicplayer.adapter.ArtistAdapter;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.service.MusicServiceConnection;
import nhannt.musicplayer.ui.itemlist.ItemListPresenter;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.Setting;

/**
 * Created by nhannt on 07/03/2017.
 */

public class App extends Application {
    private static App mInstance;
    private MusicService musicService;


    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    public static App getInstance() {
        return mInstance;
    }

    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getActiveNetworkInfo();
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Setting.getInstance().init(this);
        initSetting();
        connect();
    }

    public static File getCacheDirectory(){
       return mInstance.getApplicationContext().getCacheDir();
    }

    public static void initSetting() {
        boolean isFirstLaunch = Setting.getInstance().get(Common.FIRST_LAUNCH, true);
        if (isFirstLaunch) {
            Setting.getInstance().put(Common.ALBUM_VIEW_MODE, AlbumAdapter.LAYOUT_ITEM_LIST);
            Setting.getInstance().put(Common.ARTIST_VIEW_MODE, ArtistAdapter.LAYOUT_ITEM_LIST);
            Setting.getInstance().put(Common.SONG_SORT_MODE, ItemListPresenter.SORT_AS_A_Z);
            Setting.getInstance().put(Common.ALBUM_SORT_MODE, ItemListPresenter.SORT_AS_A_Z);
            Setting.getInstance().put(Common.ARTIST_SORT_MODE, ItemListPresenter.SORT_AS_A_Z);
            Setting.getInstance().put(Common.FIRST_LAUNCH, false);
        }
    }

    public void shuffeAll(){
        if(musicService == null) return;
        ArrayList<Song> lstSong = MediaProvider.getInstance().getListSong();
        musicService.shuffleAll(lstSong);
    }

    public void shuffeAll(ArrayList<Song> lstSong){
        if(musicService == null) return;
        musicService.shuffleAll(lstSong);
    }

    public Song getCurrentPlayingSong() {
        if (musicService == null || !musicService.isSongSetted()) return null;
        return musicService.getCurrentSong();
    }

    public ArrayList<Song> getCurrentPlayingList(){
        if (musicService == null || !musicService.isSongSetted()) return null;
        return musicService.getLstSong();
    }

    public void setSongPos(int songPsn){
        if(musicService == null) return;
        musicService.setSongPos(songPsn);
        musicService.playSong();
    }

    public void setListSong(ArrayList<Song> lstSong){
        if(musicService == null) return;
        musicService.setLstSong(lstSong);
    }

    public void updateListSong(ArrayList<Song> lstSong) {
        if (musicService == null || !musicService.isSongSetted()) return;
        musicService.setLstSong(lstSong);
        Song song = musicService.getCurrentSong();
        for (int i = 0; i < lstSong.size(); i++) {
            if(song.getId() == lstSong.get(i).getId()){
                musicService.setSongPos(i);
            }
        }
    }

    private void connect() {
        Intent intent = new Intent(this, MusicService.class);
        MusicServiceConnection connection = new MusicServiceConnection(getApplicationContext());
        connection.connect(intent, new IMusicServiceConnection() {
            @Override
            public void onConnected(MusicService service) {
                musicService = service;
            }
        });
    }
}
