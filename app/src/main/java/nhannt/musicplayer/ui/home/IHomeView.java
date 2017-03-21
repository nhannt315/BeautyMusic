package nhannt.musicplayer.ui.home;

import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by nhannt on 17/03/2017.
 */

public interface IHomeView extends BaseView {

    void updateSeekBar(int currentTime, int totalTime);

    void updatePlayPauseState();

    MusicService getMusicService();

    void updateSongInfo();

}
