package nhannt.musicplayer.ui.playingqueue;

import android.content.Intent;

import java.util.ArrayList;

import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.service.MusicServiceConnection;
import nhannt.musicplayer.utils.App;

/**
 * Created by NhanNT on 04/18/2017.
 */

public class PlayingQueueInteractor implements IPlayingQueueInteractor {
    private final IPlayingQueuePresenter mPresenter;

    public PlayingQueueInteractor(IPlayingQueuePresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void loadListSongPlaying() {
        MusicServiceConnection mConnection;
        mConnection = new MusicServiceConnection(App.getContext());
        Intent intent = new Intent(App.getContext(), MusicService.class);
        mConnection.connect(intent, new IMusicServiceConnection() {
            @Override
            public void onConnected(MusicService service) {
                ArrayList<Song> lstSong = service.getLstSong();
                if(lstSong != null){
                    mPresenter.loadingFinished(lstSong);
                }
            }
        });
    }
}
