package nhannt.musicplayer.ui.playback;

import java.util.ArrayList;

import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 05/16/2017.
 */

interface IPlayBackInteractor {
    void search(ArrayList<Song> lstSongPlaying, String keyword);
}
