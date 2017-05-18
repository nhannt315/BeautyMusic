package nhannt.musicplayer.ui.playlist;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.PlayList;

/**
 * Created by NhanNT on 04/21/2017.
 */

public class PlaylistListPresenter implements IPlaylistListPresenter {

    IPlaylistListView mView;
    IPlaylistListInteractor mInteractor;

    public PlaylistListPresenter() {
        mInteractor = new PlaylistListInteractor();
    }

    @Override
    public void attachedView(IPlaylistListView view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        this.mInteractor = null;
    }

    @Override
    public void onResume() {
        mView.showProgress();
        mInteractor.loadPlaylistList(this);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void cancelFetchingData() {
        mInteractor.cancel();
    }

    @Override
    public void onFinished(ArrayList<PlayList> itemList) {
        if(mView == null) return;
        mView.hideProgress();
        mView.setPlaylist(itemList);
    }
}
