package nhannt.musicplayer.ui.itemlist.songlist;

import java.util.ArrayList;

import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.ui.base.BasePresenter;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;

/**
 * Created by nhannt on 07/03/2017.
 */

public class SongListPresenter implements BasePresenter<ItemListMvpView<Song>>,LoaderListener<Song> {

    private ItemListMvpView<Song> songMvpView;
    private SongListInteractor songListInteractor;


    @Override
    public void onFinished(ArrayList<Song> itemList) {
        songMvpView.setItems(itemList);
        songMvpView.hideProgress();
    }

    @Override
    public void attachedView(ItemListMvpView<Song> view) {
        if(view == null){
            throw new IllegalArgumentException("You can't set a null view");
        }
        this.songMvpView = view;
        songListInteractor = new SongListInteractor();
    }


    @Override
    public void detachView() {
        songMvpView = null;
    }

    @Override
    public void onResume() {
        songMvpView.showProgress();
        songListInteractor.loadItems(this);
    }

    @Override
    public void onItemSelected(int position) {

    }
}
