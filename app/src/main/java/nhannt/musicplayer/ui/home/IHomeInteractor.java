package nhannt.musicplayer.ui.home;


/**
 * Created by NhanNT on 05/12/2017.
 */

interface IHomeInteractor {

    void searchAll(String query);

    void searchAlbum(String query, int searchId);

    void searchArtist(String query, int searchId);

}
