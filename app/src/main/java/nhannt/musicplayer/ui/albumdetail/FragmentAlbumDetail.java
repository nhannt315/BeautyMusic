package nhannt.musicplayer.ui.albumdetail;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SongAdapter;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.custom.SquareImageView;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.DividerDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAlbumDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlbumDetail extends BaseFragment implements IAlbumDetailView, RecyclerItemClickListener {
    public static final String TAG = FragmentAlbumDetail.class.getName();

    private static final String KEY_ALBUM = "key_album";
    private static final String KEY_IS_TRANSITION = "key_is_transition";
    private static final String KEY_TRANSITION_NAME = "key_transition_name";

    private Album album;
    private boolean isTransition;
    private String transitionName = "";
    private SongAdapter mSongAdapter;
    private ArrayList<Song> lstSong;
    private IAlbumDetailPresenter mPresenter;


    @BindView(R.id.iv_album_cover_album_detail)
    protected SquareImageView albumCover;
    @BindView(R.id.rv_song_list_album_detail)
    protected RecyclerView rvSongList;
    @BindView(R.id.toolbar_album_detail)
    protected Toolbar mToolbar;
    @BindView(R.id.app_bar_album_detail)
    protected AppBarLayout appBarLayout;
    @BindView(R.id.collasping_toolbar_album_detail)
    protected CollapsingToolbarLayout collapsingToolbarLayout;
    private int mainColor;

    private static Handler handler;

    private class GetMainColorForToolbar implements Runnable {
        final Bitmap bitmap;

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
            int statusbarColor = mainColor - 987670;
            collapsingToolbarLayout.setContentScrimColor(mainColor);
            collapsingToolbarLayout.setStatusBarScrimColor(statusbarColor);
        }
    }

    public FragmentAlbumDetail() {
        // Required empty public constructor
    }

    public static FragmentAlbumDetail newInstance(Album album, boolean isTransition, String transitionName) {
        FragmentAlbumDetail fragment = new FragmentAlbumDetail();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ALBUM, album);
        args.putBoolean(KEY_IS_TRANSITION, isTransition);
        args.putString(KEY_TRANSITION_NAME, transitionName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        if (Common.isLollipop())
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        super.onDestroyView();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.album = (Album) args.getSerializable(KEY_ALBUM);
            this.isTransition = args.getBoolean(KEY_IS_TRANSITION);
            this.transitionName = args.getString(KEY_TRANSITION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ablum_detail, container, false);
        ButterKnife.bind(this, view);
        setupCollaspingToolbar();
        setHasOptionsMenu(true);
        handler = new Handler();
        return view;
    }

    private void setupCollaspingToolbar() {
        collapsingToolbarLayout.setTitle(album.getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        enableDoBack();
        mainColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
    }

    private void init() {
        if (Common.isMarshMallow() && isTransition) {
            albumCover.setTransitionName(transitionName);
        }
        if (album.getCoverPath() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(album.getCoverPath());
            bitmap = Common.changeBitmapContrastBrightness(bitmap, 1, -50);
            albumCover.setImageBitmap(bitmap);
            handler.post(new GetMainColorForToolbar(bitmap));

        }
        setupToolbar();
        setupRecyclerView();
        mPresenter = new AlbumDetailPresenter();
        mPresenter.attachedView(this);
        mPresenter.onResume();
    }

    @Override
    public void doBack() {
        mPresenter.cancelFetchingData();
    }


    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSongList.setLayoutManager(layoutManager);
        rvSongList.addItemDecoration(new DividerDecoration(getActivity()));
        rvSongList.setItemAnimator(new DefaultItemAnimator());

    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(album.getTitle());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        if (mSongAdapter != null) {
            mSongAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
    @Override
    public void onDetach() {
        if(Common.isLollipop())
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        super.onDetach();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_ablum_detail;
    }


    @Override
    public ArrayList<Song> getListSong() {
        return lstSong;
    }

    @Override
    public Album getAlbum() {
        return this.album;
    }

    @Override
    public void setListSong(ArrayList<Song> lstSong) {
        this.lstSong = lstSong;
        mSongAdapter = new SongAdapter(getActivity(), lstSong, R.layout.item_song);
        mSongAdapter.setRecyclerItemClickListener(this);
        rvSongList.setAdapter(mSongAdapter);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        mPresenter.onItemClick(view, position);
    }

}
