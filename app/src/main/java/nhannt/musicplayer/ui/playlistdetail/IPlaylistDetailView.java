package nhannt.musicplayer.ui.playlistdetail;

import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by NhanNT on 04/21/2017.
 */

interface IPlaylistDetailView extends BaseView {
    PlayList getPlaylist();
    void setPlaylistDetail(PlayList playList);
}
