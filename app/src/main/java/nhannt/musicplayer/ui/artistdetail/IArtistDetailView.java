package nhannt.musicplayer.ui.artistdetail;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by NhanNT on 05/03/2017.
 */

public interface IArtistDetailView extends BaseView {
    int getArtistId();
    void setListAlbum(ArrayList<Album> lstAlbum);
    void setListSong(ArrayList<Song> lstAlbum);
}
