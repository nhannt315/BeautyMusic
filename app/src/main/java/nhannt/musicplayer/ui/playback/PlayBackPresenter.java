package nhannt.musicplayer.ui.playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import nhannt.musicplayer.R;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.custom.CircularSeekBar;

/**
 * Created by nhannt on 04/04/2017.
 */

public class PlayBackPresenter implements IPlayBackPresenter {
    private IPlayBackView mView;
    private MusicService mService;
    private Handler mHandler;
    private boolean isSeeking;
    private IPlayBackInteractor mInteractor;

    @Override
    public void cancelFetchingData() {

    }

    @Override
    public void attachedView(IPlayBackView view) {
        this.mView = view;
        mService = mView.getMusicService();
        isSeeking = false;
        mHandler = new Handler();
        mInteractor = new PlayBackInteractor(this);
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void onResume() {
        mService = mView.getMusicService();
        Log.d("playback presenter","onResume");
        if (mService == null) return;
        mView.setItems(mService.getLstSong());
    }

    @Override
    public void onDestroy() {

    }

    final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicService.PLAY_STATE_CHANGE)) {
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

    @Override
    public void onItemClicked(View view, int position) {
        mService = mView.getMusicService();
        if (mService == null) return;
        switch (view.getId()){
            case R.id.item_song:
                mService.setSongPos(position);
                mService.playSong();
                break;
        }
    }

    @Override
    public void search(String query) {
        mService = mView.getMusicService();
        if (mService == null){
            mView.updateSearchView(new ArrayList());
            return;
        }
        mInteractor.search(mService.getLstSong(), query);
    }

    @Override
    public void searchComplete(ArrayList lstResult) {
        mView.updateSearchView(lstResult);
    }

    private final Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if(mView == null) return;
            mService = mView.getMusicService();
            if (mService == null || mService.getState() != MusicService.MusicState.Playing) return;
            if (mService != null) {
                if (mService.getState() == MusicService.MusicState.Playing) {
                    mView.updateSeekBar(mService.getCurrentPosition(), mService.getDuration());
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
        if (mService != null) {
            mService.seekTo(seekBar.getProgress());
            if (mService.getState() != MusicService.MusicState.Playing) {
                mService.resumeSong();
                mView.updateButtonState();
            }
        }
        isSeeking = false;
    }


    @Override
    public void onClick(View v) {
        mService = mView.getMusicService();
        if (mService == null || !mService.isSongSetted()) return;
        switch (v.getId()) {
            case R.id.bt_skip_next_playback:
                mService.skipToNextSong();
                break;
            case R.id.bt_skip_prev_playback:
                mService.skipToPrevSong();
                break;
            case R.id.bt_shuffle_playback:
                mService.setShuffle(!mService.isShuffle());
                mView.updateButtonState();
                break;
            case R.id.bt_repeat_playback:
                mService.setRepeat(!mService.isRepeat());
                mView.updateButtonState();
                break;
            case R.id.fab_play_pause_playback:
                if (mService.getState() == MusicService.MusicState.Playing)
                    mService.pauseSong();
                else if (mService.getState() == MusicService.MusicState.Pause)
                    mService.resumeSong();
                mView.updateButtonState();
                break;
        }
    }
}
