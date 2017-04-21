package nhannt.musicplayer.ui.playlistdetail;

import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BasePresenter;
import nhannt.musicplayer.ui.playlist.IPlaylistListView;

/**
 * Created by NhanNT on 04/21/2017.
 */

public interface IPlaylistDetailPresenter extends BasePresenter<IPlaylistDetailView>{
    void setPlaylistDetail(PlayList playList);
}
