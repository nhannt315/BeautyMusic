package nhannt.musicplayer.ui.itemlist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

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

    Fragment getFragment();

    ArrayList<T> getListItem();

    void navigateToDetailFragment(Object object, @Nullable View transitionView);
}
