package nhannt.musicplayer.ui.albumdetail;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 04/18/2017.
 */

public class AlbumDetailInteractor implements IAlbumDetailInteractor{

    @Override
    public void loadListSongOfAlbum(int albumId, final LoaderListener loaderListener) {
        new AsyncTask<Integer, Void, ArrayList<Song>>() {

            @Override
            protected ArrayList<Song> doInBackground(Integer... params) {
                return MediaProvider.getInstance().getListSongOfAlbum(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<Song> songs) {
                super.onPostExecute(songs);
                loaderListener.onFinished(songs);
            }
        }.execute(albumId);
    }
}
