package nhannt.musicplayer.utils;

import android.app.Application;
import android.content.Context;

import nhannt.musicplayer.adapter.AlbumAdapter;
import nhannt.musicplayer.adapter.ArtistAdapter;
import nhannt.musicplayer.ui.itemlist.ItemListPresenter;

/**
 * Created by nhannt on 07/03/2017.
 */

public class AppController extends Application {
    private static AppController mInstance;


    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    public static AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Setting.getInstance().init(this);
        initSetting();
    }

    public static void initSetting(){
        boolean isFirstLaunch = Setting.getInstance().get(Common.FIRST_LAUNCH, true);
        if(isFirstLaunch){
            Setting.getInstance().put(Common.ALBUM_VIEW_MODE, AlbumAdapter.LAYOUT_ITEM_LIST);
            Setting.getInstance().put(Common.ARTIST_VIEW_MODE, ArtistAdapter.LAYOUT_ITEM_LIST);
            Setting.getInstance().put(Common.SONG_SORT_MODE, ItemListPresenter.SORT_AS_A_Z);
            Setting.getInstance().put(Common.ALBUM_SORT_MODE, ItemListPresenter.SORT_AS_A_Z);
            Setting.getInstance().put(Common.ARTIST_SORT_MODE, ItemListPresenter.SORT_AS_A_Z);
            Setting.getInstance().put(Common.FIRST_LAUNCH, false);
        }
    }
}
