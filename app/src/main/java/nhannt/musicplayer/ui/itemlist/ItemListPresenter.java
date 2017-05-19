package nhannt.musicplayer.ui.itemlist;

import android.view.View;

import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by nhannt on 14/03/2017.
 */

public interface ItemListPresenter<V> extends BasePresenter<V> {

    int SORT_AS_A_Z = 0;
    int SORT_AS_Z_A = 1;
    int SORT_AS_YEAR = 2;
    int SORT_AS_ARTIST = 3;
    int SORT_AS_ALBUM = 4;
    int SORT_AS_DURATION = 5;
    int SORT_AS_NUMBER_OF_SONG = 6;
    int SORT_AS_NUMBER_OF_ALBUM = 7;

    int VIEW_AS_LIST = 8;
    int VIEW_AS_GRID = 9;


    void onItemSelected(View view, int position);

    void viewAs(int viewType);

    void sortAs(int sortType);


}
