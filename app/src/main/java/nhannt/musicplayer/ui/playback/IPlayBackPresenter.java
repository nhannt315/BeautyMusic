package nhannt.musicplayer.ui.playback;

import android.content.BroadcastReceiver;
import android.view.View;

import java.util.ArrayList;

import nhannt.musicplayer.ui.base.BasePresenter;
import nhannt.musicplayer.ui.custom.CircularSeekBar;

/**
 * Created by nhannt on 04/04/2017.
 */

public interface IPlayBackPresenter extends BasePresenter<IPlayBackView>,CircularSeekBar.OnCircularSeekBarChangeListener
        ,View.OnClickListener {
    BroadcastReceiver getReceiver();
    void updateTimePlay();
    boolean isSeeking();
    void onItemClicked(View view,int position);
    void search(String query);
    void searchComplete(ArrayList lstResult);
}
