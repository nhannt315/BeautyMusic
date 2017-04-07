package nhannt.musicplayer.ui.playingqueue;

import android.content.Intent;

import java.util.ArrayList;

import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.service.MusicServiceConnection;
import nhannt.musicplayer.ui.playback.IPlayBackPresenter;
import nhannt.musicplayer.ui.playback.IPlayBackView;
import nhannt.musicplayer.utils.AppController;

/**
 * Created by nhannt on 07/04/2017.
 */

public class PlayingQueuePresenter implements IPlayingQueuePresenter {

    private IPlayingQueueView mView;
    private MusicServiceConnection mConnection;

    @Override
    public void attachedView(IPlayingQueueView view) {
        this.mView = view;
    }


    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void onResume() {
        setListSong();
    }

    @Override
    public void onDestroy() {

    }

    private void setListSong(){
        mConnection = new MusicServiceConnection(AppController.getContext());
        Intent intent = new Intent(AppController.getContext(), MusicService.class);
        mConnection.connect(intent, new IMusicServiceConnection() {
            @Override
            public void onConnected(MusicService service) {
                ArrayList<Song> lstSong = service.getLstSong();
                if(lstSong != null){
                    mView.setItems(lstSong);
                }
            }
        });
    }
}
