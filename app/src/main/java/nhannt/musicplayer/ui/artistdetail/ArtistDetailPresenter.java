package nhannt.musicplayer.ui.artistdetail;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 05/03/2017.
 */

public class ArtistDetailPresenter implements IArtistDetailPresenter {

    private IArtistDetailView mView;
    private IArtistDetailInteractor mInteractor;

    public ArtistDetailPresenter(){
        this.mInteractor = new ArtistDetailInteractor(this);
    }

    @Override
    public void attachedView(IArtistDetailView view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        mInteractor = null;
    }

    @Override
    public void cancelFetchingData() {

    }

    @Override
    public void getListData(int artistId) {
        mInteractor.loadListAlbumOfArtist(mView.getArtistId());
        mInteractor.loadListSongOfArtist(mView.getArtistId());
    }

    @Override
    public void finishGetListSong(ArrayList<Song> lstSongs) {
        mView.setListSong(lstSongs);
    }

    @Override
    public void finishGetListAlbums(ArrayList<Album> lstAlbums) {
        mView.setListAlbum(lstAlbums);
    }


}
