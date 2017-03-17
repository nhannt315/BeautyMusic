package nhannt.musicplayer.ui.itemlist.artistlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nhannt.musicplayer.ui.itemlist.LoaderListener;
import nhannt.musicplayer.model.Artist;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;
import nhannt.musicplayer.ui.itemlist.ItemListPresenter;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.Setting;

/**
 * Created by nhannt on 14/03/2017.
 */

public class ArtistListPresenter implements ItemListPresenter<ItemListMvpView<Artist>>, LoaderListener<Artist> {

    private ItemListMvpView<Artist> mArtistMvpView;
    private ArtistListInteractor mArtistInteractor;

    @Override
    public void onFinished(ArrayList<Artist> itemList) {
        mArtistMvpView.hideProgress();
        mArtistMvpView.setItems(itemList);

    }

    @Override
    public void attachedView(ItemListMvpView<Artist> view) {
        if (view == null) {
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

    @Override
    public void viewAs(int viewType) {

    }

    @Override
    public void sortAs(int sortType) {
        ArrayList<Artist> mData = mArtistMvpView.getListItem();
        switch (sortType) {
            case ItemListPresenter.SORT_AS_A_Z:
                Collections.sort(mData, new Comparator<Artist>() {
                    @Override
                    public int compare(Artist o1, Artist o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_Z_A:
                Collections.sort(mData, new Comparator<Artist>() {
                    @Override
                    public int compare(Artist o1, Artist o2) {
                        return o2.getName().compareToIgnoreCase(o1.getName());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_NUMBER_OF_ALBUM:
                Collections.sort(mData, new Comparator<Artist>() {
                    @Override
                    public int compare(Artist o1, Artist o2) {
                        if (o1.getNumberOfAlbum() == o2.getNumberOfAlbum()) {
                            return 0;
                        } else if (o1.getNumberOfAlbum() > o2.getNumberOfAlbum()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_NUMBER_OF_SONG:
                Collections.sort(mData, new Comparator<Artist>() {
                    @Override
                    public int compare(Artist o1, Artist o2) {
                        if (o1.getNumberOfSong() == o2.getNumberOfSong()) {
                            return 0;
                        } else if (o1.getNumberOfSong() > o2.getNumberOfSong()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                break;
        }
        Setting.getInstance().put(Common.ARTIST_SORT_MODE, sortType);
        mArtistMvpView.notifyDataSetChanged();
    }
}
