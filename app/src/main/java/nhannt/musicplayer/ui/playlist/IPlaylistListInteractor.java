package nhannt.musicplayer.ui.playlist;

import nhannt.musicplayer.interfaces.LoaderListener;

/**
 * Created by NhanNT on 04/21/2017.
 */

interface IPlaylistListInteractor {
    void loadPlaylistList(LoaderListener listener);
    void cancel();
}
