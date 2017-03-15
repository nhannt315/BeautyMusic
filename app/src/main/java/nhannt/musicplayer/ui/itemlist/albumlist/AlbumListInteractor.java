package nhannt.musicplayer.ui.itemlist.albumlist;

import android.os.Handler;

import nhannt.musicplayer.data.MediaProvider;
import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.ui.itemlist.ItemListInteractor;

/**
 * Created by nhannt on 14/03/2017.
 */

public class AlbumListInteractor implements ItemListInteractor {

    private MediaProvider mediaProvider = MediaProvider.getInstance();

    @Override
    public void loadItems(final LoaderListener loaderListener) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loaderListener.onFinished(mediaProvider.getListAlbum());
            }
        });
    }
}
