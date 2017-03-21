package nhannt.musicplayer.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import nhannt.musicplayer.R;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.playback.PlayBackActivity;

/**
 * Created by nhannt on 17/03/2017.
 */

public class HomePresenter implements IHomePresenter, View.OnClickListener {

    private MusicService mService;
    private IHomeView mView;
    private Handler handler;

    public HomePresenter() {
        handler = new Handler();
    }

    @Override
    public void attachedView(IHomeView view) {
        this.mView = view;
        mService = view.getMusicService();

    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_toggle_play_current_bar:
                Intent intent = new Intent(mView.getViewContext(), PlayBackActivity.class);
                mView.getViewActivity().startActivity(intent);
                mView.getViewActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.no_change);
                break;
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicService.PLAY_STATE_CHANGE)) {
                updateTimePlay();
            }
        }
    };

    public void updateTimePlay() {
        handler.removeCallbacks(mUpdateTimeTask);
        handler.post(mUpdateTimeTask);
        mUpdateTimeTask.run();
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            mView.updateSeekBar(mService.getCurrentPosition(), (int) mService.getCurrentSong().getDuration());
        }
    };

    @Override
    public BroadcastReceiver getReceiver() {
        return receiver;
    }
}
