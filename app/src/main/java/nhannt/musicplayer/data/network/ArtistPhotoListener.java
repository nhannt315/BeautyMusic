package nhannt.musicplayer.data.network;

/**
 * Created by NhanNT on 05/12/2017.
 */

public interface ArtistPhotoListener {
    void onSuccess(String url);
    void onError(String message);
}
