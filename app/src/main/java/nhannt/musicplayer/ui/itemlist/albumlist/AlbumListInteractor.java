package nhannt.musicplayer.ui.itemlist.albumlist;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.ui.itemlist.ItemListInteractor;

/**
 * Created by nhannt on 14/03/2017.
 */

public class AlbumListInteractor implements ItemListInteractor {

    private MediaProvider mediaProvider = MediaProvider.getInstance();

    @Override
    public void loadItems(final LoaderListener loaderListener) {
        new AsyncTask<Void, Void, ArrayList<Album>>() {
            @Override
            protected ArrayList<Album> doInBackground(Void... params) {
                return mediaProvider.getListAlbum();
            }

            @Override
            protected void onPostExecute(ArrayList<Album> alba) {
                super.onPostExecute(alba);
                loaderListener.onFinished(alba);
            }
        }.execute();
    }
}
