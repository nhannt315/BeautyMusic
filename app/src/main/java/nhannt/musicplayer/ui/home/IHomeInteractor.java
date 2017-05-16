package nhannt.musicplayer.ui.home;


import java.util.ArrayList;

/**
 * Created by NhanNT on 05/12/2017.
 */

public interface IHomeInteractor {

    void searchAll(String query);

    void searchAlbum(String query, int searchId);

    void searchArtist(String query, int searchId);

}
