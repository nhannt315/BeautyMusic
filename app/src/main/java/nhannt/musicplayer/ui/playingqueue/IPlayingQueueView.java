package nhannt.musicplayer.ui.playingqueue;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by nhannt on 07/04/2017.
 */

public interface IPlayingQueueView extends BaseView {
    void setItems(ArrayList<Song> lstSong);
}
