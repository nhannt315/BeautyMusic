package nhannt.musicplayer.ui.playback;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SearchAdapter;
import nhannt.musicplayer.adapter.SongAdapter;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.base.BaseActivity;
import nhannt.musicplayer.ui.custom.CenterLayoutManager;
import nhannt.musicplayer.ui.custom.CircularSeekBar;
import nhannt.musicplayer.ui.custom.MaterialSearchView;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.DividerDecoration;
import nhannt.musicplayer.utils.Navigator;

public class PlayBackActivity extends BaseActivity implements IPlayBackView, IMusicServiceConnection, RecyclerItemClickListener {

    public static final String TAG = PlayBackActivity.class.getName();

    @BindView(R.id.tv_song_title_playback)
    protected TextView tvSongTitle;
    @BindView(R.id.tv_album_playback)
    protected TextView tvAlbumTitle;
    @BindView(R.id.tv_time_play)
    protected TextView tvTimePlay;
    @BindView(R.id.bt_skip_prev_playback)
    protected ImageView btSkipPrev;
    @BindView(R.id.bt_skip_next_playback)
    protected ImageView btSkipNext;
    @BindView(R.id.bt_shuffle_playback)
    protected ImageView btShuffle;
    @BindView(R.id.bt_repeat_playback)
    protected ImageView btRepeat;
    @BindView(R.id.iv_cover_background_blur)
    protected ImageView ivBackGround;
    @BindView(R.id.iv_album_cover_playback)
    protected CircleImageView ivAlbumCover;
    @BindView(R.id.seek_bar_playback)
    protected CircularSeekBar seekBar;
    @BindView(R.id.toolbar_playback)
    protected Toolbar mToolbar;
    @BindView(R.id.rv_song_list_playback)
    protected RecyclerView mRvSongList;
    @BindView(R.id.fab_play_pause_playback)
    protected FloatingActionButton fabPlayPause;

    private IPlayBackPresenter mPresenter;
    private MusicService mService;
    private SongAdapter songAdapter;
    private CenterLayoutManager layoutManager;


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
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        setupToolbar();
        mPresenter = new PlayBackPresenter();
        mPresenter.attachedView(this);
        addOnMusicServiceListener(this);
        setupRecyclerView();
        updateAll();
        initEvents();
        setUpSearchView();
    }

    private void setUpSearchView() {
        searchAdapter = new SearchAdapter(this);
        lstSearchResult = new ArrayList();
        searchAdapter.setItemClickListener(this);
        searchView.setOnQueryTextListener(this);
    }

    private void setupRecyclerView() {
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PlayBackActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager = new CenterLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerDecoration dividerDecoration = new DividerDecoration(PlayBackActivity.this);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        mRvSongList.setLayoutManager(layoutManager);
        mRvSongList.addItemDecoration(dividerDecoration);
        mRvSongList.setItemAnimator(itemAnimator);
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
        fabPlayPause.setOnClickListener(mPresenter);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more_vert_white_24dp));
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
        mPresenter.detachView();
        super.onDestroy();

    }

    @Override
    protected ArrayList getSearchResultList(String query) {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen())
            searchView.closeSearch();
        super.onBackPressed();
    }

    @Override
    public void onConnected(MusicService service) {
        this.mService = service;
        updateSongInfo();
        mPresenter.updateTimePlay();
        updateButtonState();
        mPresenter.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.btn_equalizer_playback:
                Navigator.navigateToEqualizer(this, 126);
                break;
        }
        return true;
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
        if (mService == null || !mService.isSongSetted()) return;
        if (mService.isRepeat()) {
            btRepeat.setImageResource(R.drawable.ic_repeat_pressed);
        } else {
            btRepeat.setImageResource(R.drawable.ic_repeat_white_24dp);
        }
        if (mService.isShuffle()) {
            btShuffle.setImageResource(R.drawable.ic_shuffle_pressed);
        } else {
            btShuffle.setImageResource(R.drawable.ic_shuffle_white_24dp);
        }
        if (mService.getState() == MusicService.MusicState.Playing) {
            fabPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            fabPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    @Override
    public void updateTimeView(long currentTime) {
        tvTimePlay.setText(Common.miliSecondToString(currentTime));
    }

    @Override
    public void setItems(ArrayList<Song> lstItem) {
        songAdapter = new SongAdapter(PlayBackActivity.this, lstItem,R.layout.item_song);
        Log.d("playback", "set item");
        songAdapter.setRecyclerItemClickListener(this);
        mRvSongList.setAdapter(songAdapter);
        if (mService != null && mService.isSongSetted())
            songAdapter.updatePlayPosition(mService.getCurrentSong().getId());
        if (mService != null && mService.isSongSetted())
            layoutManager.smoothScrollToPosition(mRvSongList, null, mService.getSongPos());
    }

    @Override
    public void notifyDataSetChanged() {
        this.songAdapter.notifyDataSetChanged();
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
        if (song.getCoverPath() != null) {
            new LoadAlbumCover().execute(song.getCoverPath());
        } else {
            Blurry.with(PlayBackActivity.this)
                    .radius(25)
                    .sampling(1)
                    .color(Color.argb(57, 54, 54, 0))
                    .async()
                    .from(BitmapFactory.decodeResource(getResources(), R.drawable.music_background_new))
                    .into(ivBackGround);
        }
        Glide.with(this).load(song.getCoverPath())
                .placeholder(R.drawable.google_play_music_logo)
                .crossFade()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivAlbumCover);
        mPresenter.updateTimePlay();
        if (songAdapter != null)
            songAdapter.updatePlayPosition(mService.getCurrentSong().getId());
        if (mService != null && mService.isSongSetted() && songAdapter != null)
            layoutManager.smoothScrollToPosition(mRvSongList, null, mService.getSongPos());
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if (searchView.isSearchOpen())
            searchView.closeSearch();
        mPresenter.onItemClicked(view, position);
    }

    private class LoadAlbumCover extends AsyncTask<String, Void, Void> {
        Bitmap bitmap;


        @Override
        protected Void doInBackground(String... params) {
            bitmap = BitmapFactory.decodeFile(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Blurry.with(PlayBackActivity.this)
                    .radius(25)
                    .sampling(1)
                    .color(Color.argb(57, 54, 54, 0))
                    .async()
                    .from(bitmap)
                    .into(ivBackGround);
            super.onPostExecute(aVoid);
        }
    }
}
