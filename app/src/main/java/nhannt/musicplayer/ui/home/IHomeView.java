package nhannt.musicplayer.ui.home;

import java.util.ArrayList;

import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by nhannt on 17/03/2017.
 */

interface IHomeView extends BaseView {

    void updateSeekBar(int currentTime, int totalTime);

    void updatePlayPauseState();

    MusicService getMusicService();

    void updateSongInfo();

    void updateSearch(ArrayList lstResult);

}
