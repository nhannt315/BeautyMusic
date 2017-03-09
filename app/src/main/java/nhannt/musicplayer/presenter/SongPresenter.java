package nhannt.musicplayer.presenter;

import java.util.ArrayList;

import nhannt.musicplayer.model.LoaderListener;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.model.SongInteractor;
import nhannt.musicplayer.views.ItemListMvpView;

/**
 * Created by nhannt on 07/03/2017.
 */

public class SongPresenter implements Presenter<ItemListMvpView<Song>>,LoaderListener<Song> {

    private ItemListMvpView<Song> songMvpview;
    private SongInteractor songInteractor;


    @Override
    public void onFinished(ArrayList<Song> itemList) {
        songMvpview.setItems(itemList);
        songMvpview.hideProgress();
    }

    @Override
    public void attachedView(ItemListMvpView<Song> view) {
        if(view == null){
            throw new IllegalArgumentException("You can't set a null view");
        }
        this.songMvpview = view;
        songInteractor = new SongInteractor(view.getMContext());
    }

    @Override
    public void detachView() {
        songMvpview = null;
    }

    @Override
    public void onResume() {
        songMvpview.showProgress();
        songInteractor.loadItems(this);
    }

    @Override
    public void onItemSelected(int position) {

    }
}
