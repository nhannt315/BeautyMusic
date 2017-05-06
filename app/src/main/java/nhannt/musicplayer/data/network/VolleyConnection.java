package nhannt.musicplayer.data.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import nhannt.musicplayer.utils.App;

/**
 * Created by hienl_000 on 5/9/2016.
 */
public class VolleyConnection {
    private static VolleyConnection mInstance;
    private Context context = App.getInstance().getContext();
    private RequestQueue mRequestQueue;


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
