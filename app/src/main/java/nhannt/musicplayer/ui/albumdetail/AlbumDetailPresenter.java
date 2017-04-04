package nhannt.musicplayer.ui.albumdetail;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import nhannt.musicplayer.R;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.model.Song;

/**
 * Created by nhannt on 30/03/2017.
 */

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private IAlbumDetailView mView;

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
        new LoadSongOfAlbum(mView.getAlbum().getId()).execute();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.item_song:
                Toast.makeText(mView.getViewContext(), "Song clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_menu_song_item:
                Toast.makeText(mView.getViewContext(), "Menu Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private class LoadSongOfAlbum extends AsyncTask<Void, Void, Void> {

        private int albumID;
        private ArrayList<Song> lstSong;

        public LoadSongOfAlbum(int albumID) {
            this.albumID = albumID;
        }

        @Override
        protected Void doInBackground(Void... params) {
            lstSong = MediaProvider.getInstance().getListSongOfAlbum(albumID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mView != null)
                mView.setListSong(lstSong);
        }
    }
}
