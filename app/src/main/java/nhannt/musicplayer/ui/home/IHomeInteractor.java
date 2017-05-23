package nhannt.musicplayer.ui.home;


import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 05/12/2017.
 */

interface IHomeInteractor {

    void searchAll(String query);

    void searchAlbum(String query, int searchId);

    void searchArtist(String query, int searchId);

    void searchPlayingQueue(String query,ArrayList<Song> lstPlayingSong);

}
