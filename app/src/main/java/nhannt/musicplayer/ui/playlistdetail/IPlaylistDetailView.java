package nhannt.musicplayer.ui.playlistdetail;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by NhanNT on 04/21/2017.
 */

public interface IPlaylistDetailView extends BaseView {
    int getPlaylistId();
    void setPlaylistDetail(PlayList playList);
}
