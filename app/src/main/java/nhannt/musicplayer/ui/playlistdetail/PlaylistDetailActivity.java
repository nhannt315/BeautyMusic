package nhannt.musicplayer.ui.playlistdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SongAdapter;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.base.BaseActivity;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.DividerDecoration;

public class PlaylistDetailActivity extends BaseActivity implements IMusicServiceConnection, IPlaylistDetailView {

    public static final String KEY_COVER_TRANSITION = "key_cover_transition";
    public static final String KEY_TITLE_TRANSITION = "key_title_transition";
    public static final String KEY_PLAYLIST_ID = "key_playlist_id";
    public static final String KEY_COVER_PATH = "key_playlist_cover";


    @BindView(R.id.rv_song_playlist_detail)
    protected RecyclerView mRvSongList;
    @BindView(R.id.tv_playlist_title_detail)
    protected TextView tvTitle;
    @BindView(R.id.iv_background_playlist_detail)
    protected ImageView ivBackGround;

    private MusicService mService;
    private SongAdapter mSongAdapter;
    private PlayList mPlayList = new PlayList();
    private IPlaylistDetailPresenter mPresenter;
    private String coverPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        ButterKnife.bind(this);
        addOnMusicServiceListener(this);
        setupRecyclerView();
        getData();
        mPresenter = new PlaylistDetailPresenter();
        mPresenter.attachedView(this);
        mPresenter.onResume();
    }

    private void getData() {
        Intent intent = getIntent();
        mPlayList.setId(intent.getIntExtra(KEY_PLAYLIST_ID,-1));
        String transitionCover = intent.getStringExtra(KEY_COVER_TRANSITION);
        String transitionTitle = intent.getStringExtra(KEY_TITLE_TRANSITION);
        if(Common.isLollipop()){
            tvTitle.setTransitionName(transitionTitle);
            ivBackGround.setTransitionName(transitionCover);
        }
        coverPath = intent.getStringExtra(KEY_COVER_PATH);
        Glide.with(this).load(coverPath).into(ivBackGround);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvSongList.setLayoutManager(layoutManager);
        mRvSongList.addItemDecoration(new DividerDecoration(this));
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_playlist_detail;
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void onConnected(MusicService service) {
        mService = service;
    }

    @Override
    public int getPlaylistId() {
        return this.mPlayList.getId();
    }

    @Override
    public void setPlaylistDetail(PlayList playList) {
        mSongAdapter = new SongAdapter(this, playList.getLstSong(),R.layout.item_song_playlist_detail);
        mSongAdapter.setTextColor(ContextCompat.getColor(this, R.color.white));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mSongAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        mRvSongList.setAdapter(scaleInAnimationAdapter);
        mSongAdapter.notifyDataSetChanged();
        tvTitle.setText(playList.getTitle());

        this.mPlayList = playList;
    }

    @Override
    protected void search(String query) {
    }

    @Override
    public void onBackPressed() {
        mPresenter.cancelFetchingData();
        super.onBackPressed();
    }
}
