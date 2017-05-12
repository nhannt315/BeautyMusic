package nhannt.musicplayer.ui.home;


import java.util.ArrayList;

/**
 * Created by NhanNT on 05/12/2017.
 */

public interface IHomeInteractor {

    ArrayList searchAll(String query);

    ArrayList searchAlbum(String query, int searchId);

    ArrayList searchArtist(String query, int searchId);
}
