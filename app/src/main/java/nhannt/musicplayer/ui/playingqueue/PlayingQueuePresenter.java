package nhannt.musicplayer.ui.playingqueue;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicServiceConnection;

/**
 * Created by nhannt on 07/04/2017.
 */

public class PlayingQueuePresenter implements IPlayingQueuePresenter {

    private IPlayingQueueView mView;
    private MusicServiceConnection mConnection;
    private final IPlayingQueueInteractor mInteractor;

    public PlayingQueuePresenter() {
        mInteractor = new PlayingQueueInteractor(this);
    }

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
        mInteractor.loadListSongPlaying();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void cancelFetchingData() {

    }

    @Override
    public void loadingFinished(ArrayList<Song> lstSong) {
        mView.setItems(lstSong);
    }
}
