package nhannt.musicplayer.data.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import nhannt.musicplayer.utils.AppController;

/**
 * Created by hienl_000 on 5/9/2016.
 */
public class VolleyConnection {
    private static VolleyConnection mInstance;
    private Context context = AppController.getInstance().getContext();
    private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;
//    public static final String LAST_FM_API_KEY="761226e2f2b94da7de6a61d73f50e33c";
//    public static final String URL = "http://ws.audioscrobbler.com/2.0/?method=artist.search&artist=cher&api_key="
//            +LAST_FM_API_KEY+"&format=json";


    public static synchronized VolleyConnection getInstance(Context context) {
        if (mInstance == null) {
            synchronized (VolleyConnection.class) {
                if (mInstance == null) {
                    mInstance = new VolleyConnection(context);
                }
            }
        }
        return mInstance;
    }

    private VolleyConnection(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();
//        mImageLoader = new ImageLoader(mRequestQueue,
//                new ImageLoader.ImageCache() {
//                    private final LruCache<String, Bitmap>
//                            cache = new LruCache<String, Bitmap>(20);
//
//                    @Override
//                    public Bitmap getBitmap(String url) {
//                        return cache.get(url);
//                    }
//
//                    @Override
//                    public void putBitmap(String url, Bitmap bitmap) {
//                        cache.put(url, bitmap);
//                    }
//                });
    }

    /**
     * Get imageloader
     *
     * @return
     */
//    public ImageLoader getImageLoader() {
//        return mImageLoader;
//    }

    /**
     * Get request queue
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Add request to queue
     *
     * @param request
     */
    public <T> void addRequestToQueue(Request<T> request) {
        this.getRequestQueue().add(request);
    }
}
