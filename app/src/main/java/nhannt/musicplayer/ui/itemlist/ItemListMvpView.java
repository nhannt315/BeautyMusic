package nhannt.musicplayer.ui.itemlist;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by nhannt on 07/03/2017.
 */

public interface ItemListMvpView<T> {

    void setItems(ArrayList<T> itemList);

    void showProgress();

    void hideProgress();

    void showMessage(String message);

    void notifyDataSetChange();

    ArrayList<T> getListItem();
}
