package nhannt.musicplayer.ui.albumdetail;

import nhannt.musicplayer.interfaces.LoaderListener;

/**
 * Created by NhanNT on 04/18/2017.
 */

interface IAlbumDetailInteractor {
    void loadListSongOfAlbum(int albumId,LoaderListener loaderListener);
}
