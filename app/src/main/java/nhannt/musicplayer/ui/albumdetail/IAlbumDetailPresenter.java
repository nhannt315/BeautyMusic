package nhannt.musicplayer.ui.albumdetail;

import android.view.View;

import java.util.ArrayList;

import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by nhannt on 21/03/2017.
 */

public interface IAlbumDetailPresenter extends BasePresenter<IAlbumDetailView>,LoaderListener<Song> {
    void onItemClick(View view, int position);
}
