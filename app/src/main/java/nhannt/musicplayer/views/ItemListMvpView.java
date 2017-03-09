package nhannt.musicplayer.views;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by nhannt on 07/03/2017.
 */

public interface ItemListMvpView<T> {

    void setItems(ArrayList<T> itemList);

    Context getMContext();

    void showProgress();

    void hideProgress();

    void showMessage(String message);
}
