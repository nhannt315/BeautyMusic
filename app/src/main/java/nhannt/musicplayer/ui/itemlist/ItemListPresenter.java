package nhannt.musicplayer.ui.itemlist;

import android.view.View;

import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by nhannt on 14/03/2017.
 */

public interface ItemListPresenter<V> extends BasePresenter<V> {

    public static final int SORT_AS_A_Z = 0;
    public static final int SORT_AS_Z_A = 1;
    public static final int SORT_AS_YEAR = 2;
    public static final int SORT_AS_ARTIST = 3;
    public static final int SORT_AS_ALBUM = 4;
    public static final int SORT_AS_DURATION = 5;
    public static final int SORT_AS_NUMBER_OF_SONG = 6;
    public static final int SORT_AS_NUMBER_OF_ALBUM = 7;

    public static final int VIEW_AS_LIST = 8;
    public static final int VIEW_AS_GRID = 9;


    void onItemSelected(View view, int position);

    void viewAs(int viewType);

    void sortAs(int sortType);
}
