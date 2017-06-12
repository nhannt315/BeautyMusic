package nhannt.musicplayer.ui.itemlist.artistlist;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.ui.itemlist.ItemListInteractor;

/**
 * Created by nhannt on 14/03/2017.
 */

public class ArtistListInteractor implements ItemListInteractor {

    private final MediaProvider mediaProvider = MediaProvider.getInstance();
    private AsyncTask<Void, Void, ArrayList<Artist>> asyncLoadArtist;

    @Override
    public void loadItems(final LoaderListener loaderListener) {
        asyncLoadArtist = new AsyncTask<Void, Void, ArrayList<Artist>>() {
            @Override
            protected ArrayList<Artist> doInBackground(Void... params) {
                return mediaProvider.getListArtist();
            }

            @Override
            protected void onPostExecute(ArrayList<Artist> artists) {
                super.onPostExecute(artists);
                loaderListener.onFinished(artists);
            }
        };
        asyncLoadArtist.execute();
    }

    @Override
    public void cancel() {
        if(asyncLoadArtist != null)
            asyncLoadArtist.cancel(true);
    }
}
