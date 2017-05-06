package nhannt.musicplayer.ui.itemlist.songlist;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.itemlist.ItemListInteractor;

/**
 * Created by nhannt on 07/03/2017.
 */

public class SongListInteractor implements ItemListInteractor {

    private MediaProvider mediaProvider = MediaProvider.getInstance();


    @Override
    public void loadItems(final LoaderListener loaderListener) {
        new AsyncTask<Void, Void, ArrayList<Song>>() {
            @Override
            protected ArrayList<Song> doInBackground(Void... params) {
                return mediaProvider.getListSong();
            }

            @Override
            protected void onPostExecute(ArrayList<Song> songs) {
                super.onPostExecute(songs);
                loaderListener.onFinished(songs);
            }
        }.execute();
    }
}
