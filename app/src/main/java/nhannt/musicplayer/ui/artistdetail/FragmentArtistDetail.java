package nhannt.musicplayer.ui.artistdetail;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.AlbumAdapter;
import nhannt.musicplayer.adapter.SongAdapter;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.service.MusicServiceConnection;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.utils.App;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.DividerDecoration;
import nhannt.musicplayer.utils.ItemOffsetDecoration;
import nhannt.musicplayer.utils.Navigator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentArtistDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArtistDetail extends BaseFragment implements IArtistDetailView, RecyclerItemClickListener {
    public static final String TAG = FragmentArtistDetail.class.getName();

    private static final String KEY_ARTIST = "key_artist";
    private static final String KEY_IS_TRANSITION = "key_is_transition_artist";
    private static final String KEY_TRANSITION_NAME = "key_transition_name_artist";

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @BindView(R.id.rv_album_list_artist_detail)
    protected RecyclerView rvAlbumList;
    @BindView(R.id.app_bar_artist_detail)
    protected AppBarLayout mAppBar;
    @BindView(R.id.collasping_toolbar_artist_detail)
    protected CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.iv_artist_cover_artist_detail)
    protected ImageView imageArtistCover;
    @BindView(R.id.img_trans_artist)
    protected ImageView imgTransArtist;
    @BindView(R.id.rv_song_list_artist_detail)
    protected RecyclerView rvSongList;

    private Artist mArtist;
    private boolean isTransition = false;
    private String transitionName;
    private AlbumAdapter albumAdapter;
    private ArrayList<Album> lstAlbum;

    private SongAdapter songAdapter;
    private ArrayList<Song> lstSongs;
    private IArtistDetailPresenter mPresenter;
    private int mainColor;
    private int statusBarColor;
    private int diffColor = 987670;
    private static Handler handler;


    private class GetMainColorForToolbar implements Runnable {
        Bitmap bitmap;

        public GetMainColorForToolbar(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            Palette palette = Palette.from(bitmap).generate();
            Palette.Swatch swatch = palette.getLightVibrantSwatch();
            if (swatch != null)
                mainColor = swatch.getRgb();
            else {
                swatch = palette.getLightMutedSwatch();
                if (swatch != null) {
                    mainColor = swatch.getRgb();
                }
            }
            statusBarColor = mainColor - diffColor;
            collapsingToolbarLayout.setContentScrimColor(mainColor);
            if (Common.isLollipop())
                getActivity().getWindow().setStatusBarColor(statusBarColor);
        }
    }

    public FragmentArtistDetail() {
        // Required empty public constructor
    }

    public static FragmentArtistDetail newInstance(Artist artist, boolean isTransition, String transitionName) {
        FragmentArtistDetail fragment = new FragmentArtistDetail();
        Bundle args = new Bundle();

        args.putSerializable(KEY_ARTIST, artist);
        args.putBoolean(KEY_IS_TRANSITION, isTransition);
        args.putString(KEY_TRANSITION_NAME, transitionName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mArtist = (Artist) args.getSerializable(KEY_ARTIST);
            this.isTransition = args.getBoolean(KEY_IS_TRANSITION);
            if (isTransition)
                this.transitionName = args.getString(KEY_TRANSITION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPresenter = new ArtistDetailPresenter();
        mPresenter.attachedView(this);
        mPresenter.getListData(getArtistId());
        initControls();
        enableDoBack();
        mainColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        handler = new Handler();
    }

    private void initControls() {
        setHasOptionsMenu(true);
        setupCollaspingToolbar();
        setupToolbar();
        setupRecyclerView();

        if (Common.isLollipop() && isTransition) {
            imageArtistCover.setTransitionName(transitionName);
        }
        mPresenter.getArtistPhoto(mArtist.getName());

    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rvAlbumList.setLayoutManager(gridLayoutManager);
        rvAlbumList.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_album_artist_detail_spacing));

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvSongList.setLayoutManager(linearLayoutManager);
        rvSongList.addItemDecoration(new DividerDecoration(getContext()));
    }

    private void setupCollaspingToolbar() {
        collapsingToolbarLayout.setTitle(mArtist.getName());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void onDetach() {
        if(Common.isLollipop())
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        super.onDetach();
    }

    @Override
    public void doBack() {
        mPresenter.cancelFetchingData();

    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_artist_detail;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getArtistId() {
        return this.mArtist.getId();
    }

    @Override
    public void setListAlbum(ArrayList<Album> lstAlbum) {
        this.lstAlbum = lstAlbum;
        albumAdapter = new AlbumAdapter(getContext(), lstAlbum);
        albumAdapter.setLayoutId(R.layout.item_album_artist_detail);
        albumAdapter.setAnimate(true);
        albumAdapter.setRecyclerItemClickListener(this);
        rvAlbumList.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListSong(ArrayList<Song> lstSongs) {
        this.lstSongs = lstSongs;
        songAdapter = new SongAdapter(getContext(), this.lstSongs, R.layout.item_song);
        rvSongList.setAdapter(songAdapter);
        songAdapter.setRecyclerItemClickListener(this);
        songAdapter.notifyDataSetChanged();
    }

    @Override
    public void artistPhotoSuccess(String url) {
        Glide.with(getContext()).load(url).asBitmap().placeholder(R.drawable.google_play_music_logo)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        handler.post(new GetMainColorForToolbar(resource));
                        return false;
                    }
                })
                .into(imageArtistCover);
    }

    @Override
    public void artistPhotoError(String message) {

    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mArtist.getName());
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popCurrentFragment();
            }
        });
    }


    @Override
    public void onItemClickListener(View view, final int position) {
        switch (view.getId()) {
            case R.id.item_album_artist:
                ImageView albumCover = (ImageView) view.findViewById(R.id.iv_cover_item_album);
                Navigator.navigateToAlbumDetail(getContext(), lstAlbum.get(position), albumCover);
                break;
            case R.id.item_song:
                MusicServiceConnection mMusicServiceConnection = new MusicServiceConnection(App.getContext());
                Intent iSelectSongPlay = new Intent(App.getContext(), MusicService.class);
                iSelectSongPlay.setAction(MusicService.ACTION_PLAY);
                mMusicServiceConnection.connect(iSelectSongPlay, new IMusicServiceConnection() {
                    @Override
                    public void onConnected(MusicService service) {
                        service.setSongPos(position);
                        service.setLstSong(lstSongs);
                        service.playSong();
                    }
                });
                Navigator.navigateToPlayBackActivity(getContext());
                break;
        }

    }

}
