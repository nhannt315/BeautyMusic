package nhannt.musicplayer.ui.itemlist.songlist;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nhannt.musicplayer.R;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.service.MusicServiceConnection;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;
import nhannt.musicplayer.ui.itemlist.ItemListPresenter;
import nhannt.musicplayer.utils.App;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.Setting;

/**
 * Created by nhannt on 07/03/2017.
 */

public class SongListPresenter implements ItemListPresenter<ItemListMvpView<Song>>, LoaderListener<Song> {

    private ItemListMvpView<Song> songMvpView;
    private SongListInteractor songListInteractor;
    private MusicServiceConnection mMusicServiceConnection;

    @Override
    public void onFinished(ArrayList<Song> itemList) {
        if (songMvpView == null) return;
        songMvpView.setItems(itemList);
        songMvpView.hideProgress();
    }

    @Override
    public void cancelFetchingData() {
        songListInteractor.cancel();
    }

    @Override
    public void attachedView(ItemListMvpView<Song> view) {
        if (view == null) {
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
    public void onItemSelected(View view, final int position) {
        switch (view.getId()) {
            case R.id.item_song:
                mMusicServiceConnection = new MusicServiceConnection(App.getContext());
                Intent iSelectSongPlay = new Intent(App.getContext(), MusicService.class);
                iSelectSongPlay.setAction(MusicService.ACTION_PLAY);
                mMusicServiceConnection.connect(iSelectSongPlay, new IMusicServiceConnection() {
                    @Override
                    public void onConnected(MusicService service) {
                        service.setSongPos(position);
                        service.setLstSong(songMvpView.getListItem());
                        service.playSong();
                    }
                });
                break;
            case R.id.btn_menu_song_item:
                break;
        }
    }

    @Override
    public void viewAs(int viewType) {

    }

    @Override
    public void sortAs(int sortType) {
        ArrayList<Song> mData = songMvpView.getListItem();
        switch (sortType) {
            case ItemListPresenter.SORT_AS_A_Z:
                Collections.sort(mData, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_Z_A:
                Collections.sort(mData, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o2.getTitle().compareToIgnoreCase(o1.getTitle());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_ARTIST:
                Collections.sort(mData, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.getArtist().compareToIgnoreCase(o2.getArtist());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_YEAR:
                Collections.sort(mData, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        if (o1.getYear() == o2.getYear()) {
                            return 0;
                        } else if (o1.getYear() > o2.getYear()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_ALBUM:
                Collections.sort(mData, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        return o1.getAlbum().compareToIgnoreCase(o2.getAlbum());
                    }
                });
                break;
            case ItemListPresenter.SORT_AS_DURATION:
                Collections.sort(mData, new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        if (o1.getDuration() == o2.getDuration()) {
                            return 0;
                        } else if (o1.getDuration() > o2.getDuration()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                break;
        }
        Setting.getInstance().put(Common.SONG_SORT_MODE, sortType);
        songMvpView.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {

    }
}
