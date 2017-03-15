package nhannt.musicplayer.ui.itemlist.albumlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.model.Album;
import nhannt.musicplayer.ui.base.BasePresenter;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;
import nhannt.musicplayer.ui.itemlist.ItemListPresenter;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.Setting;

/**
 * Created by nhannt on 14/03/2017.
 */

public class AlbumListPresenter implements ItemListPresenter<ItemListMvpView<Album>>,LoaderListener<Album> {

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

    @Override
    public void viewAs(int viewType) {

    }

    @Override
    public void sortAs(int sortType) {
        ArrayList<Album> mData = albumMvpView.getListItem();
        switch (sortType){
            case ItemListPresenter.SORT_AS_A_Z:
                Collections.sort(mData, new Comparator<Album>() {
                    @Override
                    public int compare(Album o1, Album o2) {
                        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_Z_A:
                Collections.sort(mData, new Comparator<Album>() {
                    @Override
                    public int compare(Album o1, Album o2) {
                        return o2.getTitle().compareToIgnoreCase(o1.getTitle());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_YEAR:
                Collections.sort(mData, new Comparator<Album>() {
                    @Override
                    public int compare(Album o1, Album o2) {
                        if(o1.getYear() == o2.getYear()){
                            return 0;
                        }else if(o1.getYear()>o2.getYear()){
                            return 1;
                        }else{
                            return -1;
                        }
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_ARTIST:
                Collections.sort(mData, new Comparator<Album>() {
                    @Override
                    public int compare(Album o1, Album o2) {
                        return o1.getArtist().compareToIgnoreCase(o2.getArtist());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_NUMBER_OF_SONG:
                Collections.sort(mData, new Comparator<Album>() {
                    @Override
                    public int compare(Album o1, Album o2) {
                        if(o1.getSongCount() == o2.getSongCount()){
                            return 0;
                        }else if(o1.getSongCount()>o2.getSongCount()){
                            return 1;
                        }else{
                            return -1;
                        }
                    }
                });
                break;
        }
        Setting.getInstance().put(Common.ALBUM_SORT_MODE, sortType);
        albumMvpView.notifyDataSetChanged();
    }
}
