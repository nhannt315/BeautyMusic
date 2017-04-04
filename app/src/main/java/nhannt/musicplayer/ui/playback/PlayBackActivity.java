package nhannt.musicplayer.ui.playback;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import nhannt.musicplayer.R;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.base.BaseActivity;
import nhannt.musicplayer.ui.custom.CircularSeekBar;
import nhannt.musicplayer.utils.Common;

public class PlayBackActivity extends BaseActivity implements IPlayBackView, IMusicServiceConnection {

    @BindView(R.id.tv_song_title_playback)
    TextView tvSongTitle;
    @BindView(R.id.tv_album_playback)
    TextView tvAlbumTitle;
    @BindView(R.id.tv_time_play)
    TextView tvTimePlay;
    @BindView(R.id.bt_skip_prev_playback)
    ImageView btSkipPrev;
    @BindView(R.id.bt_skip_next_playback)
    ImageView btSkipNext;
    @BindView(R.id.bt_shuffle_playback)
    ImageView btShuffle;
    @BindView(R.id.bt_repeat_playback)
    ImageView btRepeat;
    @BindView(R.id.iv_cover_background_blur)
    ImageView ivBackGround;
    @BindView(R.id.iv_album_cover_playback)
    CircleImageView ivAlbumCover;
    @BindView(R.id.seek_bar_playback)
    CircularSeekBar seekBar;
    @BindView(R.id.toolbar_playback)
    Toolbar mToolbar;

    private IPlayBackPresenter mPresenter;
    private MusicService mService;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playback_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back);
        ButterKnife.bind(this);
        setupToolbar();
        mPresenter = new PlayBackPresenter();
        mPresenter.attachedView(this);
        addOnMusicServiceListener(this);
        updateAll();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.PLAY_STATE_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPresenter.getReceiver(), filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPresenter.getReceiver());
    }

    private void initEvents() {
        seekBar.setOnSeekBarChangeListener(mPresenter);
        btRepeat.setOnClickListener(mPresenter);
        btShuffle.setOnClickListener(mPresenter);
        btSkipNext.setOnClickListener(mPresenter);
        btSkipPrev.setOnClickListener(mPresenter);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_play_back;
    }


    @Override
    public Context getViewContext() {
        return this;
    }

    private void updateAll() {
        updateSongInfo();
        mPresenter.updateTimePlay();
        updateButtonState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onConnected(MusicService service) {
        this.mService = service;
        updateSongInfo();
        mPresenter.updateTimePlay();
        updateButtonState();
    }

    @Override
    public void updateSeekBar(int currentTime, int totalTime) {
        if (!mPresenter.isSeeking()) {
            seekBar.setMax(totalTime);
            seekBar.setProgress(currentTime);
        }
    }

    @Override
    public void updateButtonState() {
        if (mService == null) return;
        if (mService.isRepeat()) {
            btRepeat.setImageResource(R.drawable.ic_repeat_pressed);
        } else {
            btRepeat.setImageResource(R.drawable.ic_repeat_white_24dp);
        }
        if (mService.isShuffle()) {
            btShuffle.setImageResource(R.drawable.ic_shuffle_white_24dp);
        } else {
            btShuffle.setImageResource(R.drawable.ic_shuffle_pressed);
        }
    }

    @Override
    public void updateTimeView(long currentTime) {
        tvTimePlay.setText(Common.miliSecondToString(currentTime));
    }

    @Override
    public MusicService getMusicService() {
        return this.mService;
    }

    @Override
    public void updateSongInfo() {
        if (mService == null) return;
        Song song = mService.getCurrentSong();
        if (song == null) return;
        tvSongTitle.setText(song.getTitle());
        tvAlbumTitle.setText(song.getAlbum());
        Glide.with(this).load(song.getCoverPath()).into(ivAlbumCover);
//        Glide.with(this).load(song.getCoverPath()).into(ivBackGround);
        if (song.getCoverPath() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(song.getCoverPath());
            Blurry.with(PlayBackActivity.this)
                    .radius(25)
                    .sampling(1)
                    .color(Color.argb(57, 54, 54, 0))
                    .async()
                    .from(bitmap)
                    .into(ivBackGround);
        } else {
            Blurry.with(PlayBackActivity.this)
                    .radius(25)
                    .sampling(1)
                    .color(Color.argb(57, 54, 54, 0))
                    .async()
                    .from(BitmapFactory.decodeResource(getResources(), R.drawable.music_background))
                    .into(ivBackGround);
        }
        mPresenter.updateTimePlay();
    }
}
