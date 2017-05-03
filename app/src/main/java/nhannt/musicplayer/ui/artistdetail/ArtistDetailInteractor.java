package nhannt.musicplayer.ui.artistdetail;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 05/03/2017.
 */

public class ArtistDetailInteractor implements IArtistDetailInteractor {

    private IArtistDetailPresenter mPresenter;

    public ArtistDetailInteractor(IArtistDetailPresenter presenter){
        this.mPresenter = presenter;
    }

    @Override
    public void loadListAlbumOfArtist(int artistId) {
        new AsyncTask<Integer,Void,ArrayList<Album>>(){

            @Override
            protected ArrayList<Album> doInBackground(Integer... params) {
                ArrayList<Album> lstAlbum = MediaProvider.getInstance().getListAlbumOfArtist(params[0]);
                return lstAlbum;
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
                ArrayList<Song> lstSong = MediaProvider.getInstance().getListSongOfArtist(params[0]);
                return lstSong;
            }

            @Override
            protected void onPostExecute(ArrayList<Song> songs) {
                super.onPostExecute(songs);
                mPresenter.finishGetListSong(songs);
            }
        }.execute(artistId);
    }
}
