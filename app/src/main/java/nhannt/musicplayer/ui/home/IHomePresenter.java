package nhannt.musicplayer.ui.home;

import android.content.BroadcastReceiver;

import nhannt.musicplayer.ui.base.BasePresenter;

/**
 * Created by nhannt on 17/03/2017.
 */

public interface IHomePresenter extends BasePresenter<IHomeView> {

    BroadcastReceiver getReceiver();
}
