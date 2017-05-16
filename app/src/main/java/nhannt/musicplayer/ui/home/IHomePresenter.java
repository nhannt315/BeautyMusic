package nhannt.musicplayer.ui.home;

import android.content.BroadcastReceiver;

import java.util.ArrayList;

import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by nhannt on 17/03/2017.
 */

public interface IHomePresenter extends BasePresenter<IHomeView> {

    BroadcastReceiver getReceiver();
    void searchArtistDetail(String query,int id);
    void searchAlbumDetail(String query,int id);
    void searchAll(String query);
    void searchComplete(ArrayList lstResult);
}
