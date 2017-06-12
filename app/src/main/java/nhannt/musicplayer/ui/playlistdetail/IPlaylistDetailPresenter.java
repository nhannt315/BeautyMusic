package nhannt.musicplayer.ui.playlistdetail;

import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by NhanNT on 04/21/2017.
 */

interface IPlaylistDetailPresenter extends BasePresenter<IPlaylistDetailView>,RecyclerItemClickListener{
    void setPlaylistDetail(PlayList playList);
}
