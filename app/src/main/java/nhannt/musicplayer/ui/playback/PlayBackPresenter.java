package nhannt.musicplayer.ui.playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.custom.CircularSeekBar;
import nhannt.musicplayer.R;

/**
 * Created by nhannt on 04/04/2017.
 */

public class PlayBackPresenter implements IPlayBackPresenter {
    private IPlayBackView mView;
    private MusicService mService;
    private Handler mHandler;
    private boolean isSeeking;

    @Override
    public void attachedView(IPlayBackView view) {
        this.mView = view;
        mService = mView.getMusicService();
        isSeeking = false;
        mHandler = new Handler();
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MusicService.PLAY_STATE_CHANGE)){
                updateTimePlay();
                mView.updateSongInfo();
                mView.updateButtonState();

            }
        }
    };

    @Override
    public BroadcastReceiver getReceiver() {
        return this.receiver;
    }

    @Override
    public void updateTimePlay() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.post(mUpdateTimeTask);
        mUpdateTimeTask.run();
    }

    @Override
    public boolean isSeeking() {
        return this.isSeeking;
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if(mView == null) return;
            mService = mView.getMusicService();
            if(mService != null){
                if(mService.getState() == MusicService.MusicState.Playing){
                    mView.updateSeekBar(mService.getCurrentPosition(),mService.getDuration());
                    mView.updateTimeView(mService.getCurrentPosition());
                    mHandler.postDelayed(mUpdateTimeTask, 200);
                }
            }
        }
    };

    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {
        isSeeking = true;
    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {
        if(mService != null) {
            mService.seekTo(seekBar.getProgress());
            if(mService.getState() != MusicService.MusicState.Playing){
                mService.resumeSong();
                mView.updateButtonState();
            }
        }
        isSeeking = false;
    }


    @Override
    public void onClick(View v) {
        mService = mView.getMusicService();
        if(mService == null) return;
        switch (v.getId()){
            case R.id.bt_skip_next_playback:
                mService.skipToNextSong();
                break;
            case R.id.bt_skip_prev_playback:
                mService.skipToPrevSong();
                break;
            case R.id.bt_shuffle_playback:
                mService.setShuffle(!mService.isShuffle());
                break;
            case R.id.bt_repeat_playback:
                mService.setRepeat(!mService.isRepeat());
                break;
        }
    }
}
