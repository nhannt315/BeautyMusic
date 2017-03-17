package nhannt.musicplayer.ui.itemlist.songlist;

import android.os.Handler;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.ui.itemlist.ItemListInteractor;
import nhannt.musicplayer.ui.itemlist.LoaderListener;

/**
 * Created by nhannt on 07/03/2017.
 */

public class SongListInteractor implements ItemListInteractor {

    private MediaProvider mediaProvider = MediaProvider.getInstance();


    @Override
    public void loadItems(final LoaderListener loaderListener) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loaderListener.onFinished(mediaProvider.getListSong());
            }
        });
    }
}
