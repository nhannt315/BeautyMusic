package nhannt.musicplayer.ui.albumdetail;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import nhannt.musicplayer.R;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.utils.App;

/**
 * Created by nhannt on 30/03/2017.
 */

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private IAlbumDetailView mView;
    private final AlbumDetailInteractor mInteractor;

    public AlbumDetailPresenter() {
        mInteractor = new AlbumDetailInteractor();
    }

    @Override
    public void attachedView(IAlbumDetailView view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void onResume() {
        mInteractor.loadListSongOfAlbum(mView.getAlbum().getId(), this);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void cancelFetchingData() {

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.item_song:
                Toast.makeText(mView.getViewContext(), "Song clicked", Toast.LENGTH_SHORT).show();
                App.getInstance().setListSong(mView.getListSong());
                App.getInstance().setSongPos(position);
                break;
            case R.id.btn_menu_song_item:
                Toast.makeText(mView.getViewContext(), "Menu Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onFinished(ArrayList<Song> itemList) {
        if (mView != null)
            mView.setListSong(itemList);
    }
}
