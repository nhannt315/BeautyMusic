package nhannt.musicplayer.ui.artistdetail;

import nhannt.musicplayer.data.network.lastfmapi.ArtistPhotoListener;

/**
 * Created by NhanNT on 05/03/2017.
 */
 interface IArtistDetailInteractor {
    void loadListAlbumOfArtist(int artistId);

    void loadListSongOfArtist(int artistId);

    void loadArtistPhoto(String artistName, ArtistPhotoListener listener);
}
