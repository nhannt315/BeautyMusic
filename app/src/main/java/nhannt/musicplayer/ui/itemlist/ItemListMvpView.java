package nhannt.musicplayer.ui.itemlist;

import android.content.Context;

import java.util.ArrayList;

import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by nhannt on 07/03/2017.
 */

public interface ItemListMvpView<T> extends BaseView {

    void setItems(ArrayList<T> itemList);

    void showProgress();

    void hideProgress();

    void showMessage(String message);

    void notifyDataSetChanged();

    ArrayList<T> getListItem();
}
