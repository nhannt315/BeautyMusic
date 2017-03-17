package nhannt.musicplayer.ui.itemlist.artistlist;

import android.os.Handler;

import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.ui.itemlist.LoaderListener;
import nhannt.musicplayer.ui.itemlist.ItemListInteractor;

/**
 * Created by nhannt on 14/03/2017.
 */

public class ArtistListInteractor implements ItemListInteractor {

    private MediaProvider mediaProvider = MediaProvider.getInstance();

    @Override
    public void loadItems(final LoaderListener loaderListener) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loaderListener.onFinished(mediaProvider.getListArtist());
            }
        });
    }
}
