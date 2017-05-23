package nhannt.musicplayer.ui.home;

import android.content.BroadcastReceiver;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by nhannt on 17/03/2017.
 */

interface IHomePresenter extends BasePresenter<IHomeView> {

    BroadcastReceiver getReceiver();
    void searchArtistDetail(String query,int id);
    void searchAlbumDetail(String query,int id);
    void searchAll(String query);
    void searchPlayingQueue(String query,ArrayList<Song> lstPlayingSong);
    void searchComplete(ArrayList lstResult);
}
