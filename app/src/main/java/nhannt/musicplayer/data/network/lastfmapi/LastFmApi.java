package nhannt.musicplayer.data.network.lastfmapi;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import nhannt.musicplayer.R;
import nhannt.musicplayer.data.network.lastfmapi.model.Artist;
import nhannt.musicplayer.data.network.lastfmapi.model.Artistmatches;
import nhannt.musicplayer.data.network.lastfmapi.model.Result;
import nhannt.musicplayer.data.network.lastfmapi.model.ResultDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NhanNT on 05/20/2017.
 */

public class LastFmApi {

    private LastFmService mService;
    private Context mContext;
    private ImageView imgArtist;
    private ArtistPhotoListener listener;
    private boolean isAnimate;

    public LastFmApi(Context context) {
        this.mContext = context;
        mService = NetworkClient.provideLastFmService();
    }

    private class ArtistCallBack implements Callback<Result>{

        @Override
        public void onResponse(Call<Result> call, Response<Result> response) {
            if (response.isSuccessful()) {
                Result result = response.body();
                ResultDetail resultDetail = result.getResultDetail();

                Artistmatches artistmatches = resultDetail.getArtistmatches();
                Artist artist;
                String url = null;


                if (artistmatches.getArtist() != null) {
                    artist = artistmatches.getArtist().get(0);
                    if (artist != null)
                        url = artist.getImage().get(2).getText();
                }

                if(imgArtist != null) {
                    if (isAnimate)
                        Glide.with(mContext).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.google_play_music_logo)
                                .into(imgArtist);
                    else
                        Glide.with(mContext).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.google_play_music_logo)
                                .dontAnimate()
                                .into(imgArtist);
                }
                if(listener != null){
                    listener.onSuccess(url);
                }
            }
        }

        @Override
        public void onFailure(Call<Result> call, Throwable t) {

        }
    }

    public void getArtistPhoto(String artistName, final ImageView artistIv, final boolean isAnimate) {
        this.imgArtist = artistIv;
        this.isAnimate = isAnimate;
        mService.getArtistPhoto(artistName).enqueue(new ArtistCallBack());
    }

    public void getArtistPhoto(String artistName, ArtistPhotoListener listener) {
        this.listener = listener;
        mService.getArtistPhoto(artistName).enqueue(new ArtistCallBack());
    }
}
