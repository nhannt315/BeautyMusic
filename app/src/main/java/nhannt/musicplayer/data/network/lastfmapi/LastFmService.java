package nhannt.musicplayer.data.network.lastfmapi;



import nhannt.musicplayer.data.network.lastfmapi.model.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by NhanNT on 05/20/2017.
 */

public interface LastFmService {
    String API_KEY = "761226e2f2b94da7de6a61d73f50e33c";
    String ARTIST_URL = "2.0/?method=artist.search&api_key="+API_KEY+"&format=json";

    @GET(ARTIST_URL)
    Call<Result> getArtistPhoto(@Query("artist") String artistName);
}
