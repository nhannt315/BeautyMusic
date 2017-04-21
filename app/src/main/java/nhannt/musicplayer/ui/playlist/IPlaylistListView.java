package nhannt.musicplayer.ui.playlist;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by NhanNT on 04/21/2017.
 */

public interface IPlaylistListView extends BaseView {
    void setPlaylist(ArrayList<PlayList> itemList);
}
