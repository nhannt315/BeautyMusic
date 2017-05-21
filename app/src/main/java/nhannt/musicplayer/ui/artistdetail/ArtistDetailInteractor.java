package nhannt.musicplayer.ui.artistdetail;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.App;
import nhannt.musicplayer.data.network.ArtistPhotoListener;
import nhannt.musicplayer.data.network.retrofit.LastFmApi;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 05/03/2017.
 */

public class ArtistDetailInteractor implements IArtistDetailInteractor {

    private final IArtistDetailPresenter mPresenter;

    public ArtistDetailInteractor(IArtistDetailPresenter presenter){
        this.mPresenter = presenter;
    }

    @Override
    public void loadListAlbumOfArtist(int artistId) {
        new AsyncTask<Integer,Void,ArrayList<Album>>(){

            @Override
            protected ArrayList<Album> doInBackground(Integer... params) {
                return MediaProvider.getInstance().getListAlbumOfArtist(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<Album> alba) {
                super.onPostExecute(alba);
                mPresenter.finishGetListAlbums(alba);
            }
        }.execute(artistId);
    }

    @Override
    public void loadListSongOfArtist(int artistId) {
        new AsyncTask<Integer,Void,ArrayList<Song>>(){

            @Override
            protected ArrayList<Song> doInBackground(Integer... params) {
                return MediaProvider.getInstance().getListSongOfArtist(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<Song> songs) {
                super.onPostExecute(songs);
                mPresenter.finishGetListSong(songs);
            }
        }.execute(artistId);
    }

    @Override
    public void loadArtistPhoto(String artistName, ArtistPhotoListener listener) {
//        new ArtistPhotoLastFmApi(App.getInstance().getApplicationContext(), artistName, listener).execute();
        new LastFmApi(App.getInstance().getApplicationContext()).getArtistPhoto(artistName, listener);
    }
}
