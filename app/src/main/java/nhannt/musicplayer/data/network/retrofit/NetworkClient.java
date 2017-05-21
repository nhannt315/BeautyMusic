package nhannt.musicplayer.data.network.retrofit;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import nhannt.musicplayer.App;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by NhanNT on 05/20/2017.
 */

public class NetworkClient {
    private static Retrofit retrofit;
    public static final String BASE_URL = "http://ws.audioscrobbler.com/";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final long CACHE_SIZE = 10 * 1024 * 1024; //10MB

    public static Retrofit provideRetrofit(String baseUrl) {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient())
                    .build();
        }

        return retrofit;
    }

    public static Retrofit provideRetrofit(){
        return provideRetrofit(BASE_URL);
    }


    public static OkHttpClient provideOkHttpClient() {

        return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build();
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(App.getCacheDirectory(), "http-cache"), CACHE_SIZE);
        } catch (Exception ex) {
            Timber.e(ex, "Could not create Cache!");
        }
        return cache;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Timber.d(message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return httpLoggingInterceptor;
    }

    public static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    public static Interceptor provideOfflineCacheInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if(!App.getInstance().isNetworkOnline()){
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    public static LastFmService provideLastFmService(){
        return provideRetrofit(BASE_URL).create(LastFmService.class);
    }

}
