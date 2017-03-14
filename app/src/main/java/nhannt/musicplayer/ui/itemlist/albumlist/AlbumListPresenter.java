package nhannt.musicplayer.ui.itemlist.albumlist;

import java.util.ArrayList;

import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.model.Album;
import nhannt.musicplayer.ui.base.BasePresenter;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;

/**
 * Created by nhannt on 14/03/2017.
 */

public class AlbumListPresenter implements BasePresenter<ItemListMvpView<Album>>,LoaderListener<Album> {

    private ItemListMvpView<Album> albumMvpView;
    private AlbumListInteractor albumInteractor;

    @Override
    public void onFinished(ArrayList<Album> itemList) {
        albumMvpView.hideProgress();
        albumMvpView.setItems(itemList);
    }

    @Override
    public void attachedView(ItemListMvpView<Album> view) {
        if(view == null){
            throw new IllegalArgumentException("You can't set a null view");
        }
        this.albumMvpView = view;
        albumInteractor = new AlbumListInteractor();
    }

    @Override
    public void detachView() {
        albumMvpView = null;
    }

    @Override
    public void onResume() {
        albumMvpView.showProgress();
        albumInteractor.loadItems(this);
    }

    @Override
    public void onItemSelected(int position) {

    }
}
