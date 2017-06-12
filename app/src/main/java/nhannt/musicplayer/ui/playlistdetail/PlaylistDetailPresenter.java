package nhannt.musicplayer.ui.playlistdetail;

import android.view.View;

import nhannt.musicplayer.App;
import nhannt.musicplayer.objectmodel.PlayList;

/**
 * Created by NhanNT on 04/21/2017.
 */

public class PlaylistDetailPresenter implements IPlaylistDetailPresenter {

    private IPlaylistDetailView mView;
    private IPlaylistDetailInteractor mInteractor;

    public PlaylistDetailPresenter() {
        mInteractor = new PlaylistDetailInteractor(this);
    }

    @Override
    public void attachedView(IPlaylistDetailView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        this.mInteractor = null;
    }

    @Override
    public void onResume() {
        mInteractor.loadPlaylistDetail(mView.getPlaylist().getId());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void cancelFetchingData() {

    }

    @Override
    public void setPlaylistDetail(PlayList playList) {
        mView.setPlaylistDetail(playList);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if(mView == null || mView.getPlaylist() == null
                || mView.getPlaylist().getLstSong() == null
                || mView.getPlaylist().getLstSong().size() == 0) return;
        App.getInstance().playSong(mView.getPlaylist().getLstSong(), position);
    }
}
