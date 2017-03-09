package nhannt.musicplayer.model;

import android.content.Context;
import android.os.Handler;
import nhannt.musicplayer.provider.MediaProvider;

/**
 * Created by nhannt on 07/03/2017.
 */

public class SongInteractor implements Interactor {

    private Context mContext;
    private MediaProvider mediaProvider = MediaProvider.getInstance();

    public SongInteractor(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void loadItems(final LoaderListener loaderListener) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loaderListener.onFinished(mediaProvider.getAllSong());
            }
        });
    }
}
