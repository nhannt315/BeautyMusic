package nhannt.musicplayer.ui.playlistdetail;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.App;
import nhannt.musicplayer.R;
import nhannt.musicplayer.data.database.DBQuery;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.playlist.FragmentPlaylist;

/**
 * Created by NhanNT on 04/21/2017.
 */

public class PlaylistDetailInteractor implements IPlaylistDetailInteractor {

    private final IPlaylistDetailPresenter mPresenter;

    public PlaylistDetailInteractor(IPlaylistDetailPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void loadPlaylistDetail(int playlistId) {

        new AsyncTask<Integer, Void, Void>() {
            ArrayList<Song> lstSong;
            PlayList playList;
            @Override
            protected Void doInBackground(Integer... params) {
                int pId = params[0];
                switch (pId){
                    case FragmentPlaylist.RECENT_ADDED_ID:
                        lstSong = MediaProvider.getInstance().getListSong();
                        playList = new PlayList(FragmentPlaylist.RECENT_ADDED_ID, App.getContext().getString(R.string.last_added),
                                lstSong.size(),
                                lstSong, true);
                        break;
                    case FragmentPlaylist.RECENT_PLAYED_ID:
                        lstSong = DBQuery.getInstance().getListSongRecentPlay();
                        playList = new PlayList(FragmentPlaylist.RECENT_PLAYED_ID,
                                App.getContext().getString(R.string.recent_played), lstSong.size(),
                                lstSong, true);
                        break;
                    default:
                        lstSong = DBQuery.getInstance().getListSongByPlaylist(params[0]);
                        playList = DBQuery.getInstance().getPlayListById(params[0]);
                        break;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                playList.setLstSong(lstSong);
                mPresenter.setPlaylistDetail(playList);
                super.onPostExecute(aVoid);
            }
        }.execute(playlistId);
    }

}
