package nhannt.musicplayer.ui.albumdetail;

import java.util.ArrayList;

import nhannt.musicplayer.model.Album;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by nhannt on 21/03/2017.
 */

public interface IAlbumDetailView extends BaseView {

    ArrayList<Song> getListSong();
    void setListSong(ArrayList<Song> lstSong);
    void notifyDataSetChanged();
    Album getAlbum();

}
