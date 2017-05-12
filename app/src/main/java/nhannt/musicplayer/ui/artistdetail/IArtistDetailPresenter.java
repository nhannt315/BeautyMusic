package nhannt.musicplayer.ui.artistdetail;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by NhanNT on 05/03/2017.
 */

public interface IArtistDetailPresenter extends BasePresenter<IArtistDetailView> {
    void getListData(int artistId);
    void getArtistPhoto(String artistName);
    void finishGetListSong(ArrayList<Song> lstSongs);
    void finishGetListAlbums(ArrayList<Album> lstAlbums);
}
