package nhannt.musicplayer.ui.itemlist.artistlist;

import android.util.Log;

import java.util.ArrayList;

import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.model.Artist;
import nhannt.musicplayer.ui.base.BasePresenter;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;

/**
 * Created by nhannt on 14/03/2017.
 */

public class ArtistListPresenter implements BasePresenter<ItemListMvpView<Artist>>, LoaderListener<Artist> {

    private ItemListMvpView<Artist> mArtistMvpView;
    private ArtistListInteractor mArtistInteractor;

    @Override
    public void onFinished(ArrayList<Artist> itemList) {
        mArtistMvpView.hideProgress();
        mArtistMvpView.setItems(itemList);

    }

    @Override
    public void attachedView(ItemListMvpView<Artist> view) {
        if(view == null){
            throw new IllegalArgumentException("You can't set a null view");
        }
        this.mArtistMvpView = view;
        mArtistInteractor = new ArtistListInteractor();
    }

    @Override
    public void detachView() {
        mArtistMvpView = null;
    }

    @Override
    public void onResume() {
        mArtistMvpView.showProgress();
        mArtistInteractor.loadItems(this);
    }

    @Override
    public void onItemSelected(int position) {

    }
}
