package nhannt.musicplayer.ui.playingqueue;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by nhannt on 07/04/2017.
 */

interface IPlayingQueuePresenter extends BasePresenter<IPlayingQueueView> {
    void loadingFinished(ArrayList<Song> lstSong);
}
