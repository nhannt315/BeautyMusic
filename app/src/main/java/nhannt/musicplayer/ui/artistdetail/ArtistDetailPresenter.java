package nhannt.musicplayer.ui.artistdetail;

import java.util.ArrayList;

import nhannt.musicplayer.data.network.ArtistPhotoListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 05/03/2017.
 */

public class ArtistDetailPresenter implements IArtistDetailPresenter,ArtistPhotoListener {

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
    public void getArtistPhoto(String artistName) {
        mInteractor.loadArtistPhoto(artistName, this);
    }

    @Override
    public void finishGetListSong(ArrayList<Song> lstSongs) {
        mView.setListSong(lstSongs);
    }

    @Override
    public void finishGetListAlbums(ArrayList<Album> lstAlbums) {
        mView.setListAlbum(lstAlbums);
    }


    @Override
    public void onSuccess(String url) {

        if(mView != null)
            mView.artistPhotoSuccess(url);
    }

    @Override
    public void onError(String message) {
        if(mView != null)
            mView.artistPhotoError(message);
    }
}
