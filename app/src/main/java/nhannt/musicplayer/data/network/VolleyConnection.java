package nhannt.musicplayer.data.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by hienl_000 on 5/9/2016.
 */
public class VolleyConnection {
    private static VolleyConnection mInstance;
    private Context context;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


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
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     * Get imageloader
     *
     * @return
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

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
