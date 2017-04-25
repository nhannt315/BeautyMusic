package nhannt.musicplayer.ui.albumdetail;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SongAdapter;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.custom.DividerDecoration;
import nhannt.musicplayer.ui.custom.SquareImageView;
import nhannt.musicplayer.utils.Common;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAlbumDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlbumDetail extends BaseFragment implements IAlbumDetailView, RecyclerItemClickListener, AppBarLayout.OnOffsetChangedListener {
    public static final String TAG = FragmentAlbumDetail.class.getName();

    private static final String KEY_ALBUM = "key_album";
    private static final String KEY_IS_TRANSITION = "key_is_transition";
    private static final String KEY_TRANSITION_NAME = "key_transition_name";

    private Album album;
    private boolean isTransition;
    private String transitionName = "";
    private ActionBar activityActionBar = null;
    private SongAdapter mSongAdapter;
    private ArrayList<Song> lstSong;
    private IAlbumDetailPresenter mPresenter;
    private int scrollRange = -1;

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
        appBarLayout.addOnOffsetChangedListener(this);
        setupCollaspingToolbar();
        return view;
    }

    private void setupCollaspingToolbar() {
        collapsingToolbarLayout.setTitle(album.getTitle());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

    }

    private void init() {
        if (Common.isMarshMallow() && isTransition) {
            albumCover.setTransitionName(transitionName);
        }
        if (album.getCoverPath() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(album.getCoverPath());
            bitmap = Common.changeBitmapContrastBrightness(bitmap, 1, -50);
            albumCover.setImageBitmap(bitmap);
        }
        setupToolbar();
        setupRecyclerView();
        mPresenter = new AlbumDetailPresenter();
        mPresenter.attachedView(this);
        mPresenter.onResume();
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSongList.setLayoutManager(layoutManager);
        rvSongList.addItemDecoration(new DividerDecoration(getActivity()));
        rvSongList.setItemAnimator(new DefaultItemAnimator());
        rvSongList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if ((scrollDy == 0) && (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)) {
                    appBarLayout.setExpanded(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollDy += dy;
            }
        });
    }

    private void setupToolbar() {
        if (mToolbar == null) Log.d("fragment album detail", "tool bar null");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(album.getTitle());
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
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
    protected int getLayout() {
        return R.layout.fragment_ablum_detail;
    }


    @Override
    public void doBack() {

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
        mSongAdapter = new SongAdapter(getActivity(), lstSong);
        mSongAdapter.setRecyclerItemClickListener(this);
        rvSongList.setAdapter(mSongAdapter);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        mPresenter.onItemClick(view, position);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //Initialize the size of the scroll
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Check if the view is collapsed
        if (scrollRange + verticalOffset == 0) {
            mToolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            if (Common.isLollipop())
                window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        } else {
            mToolbar.setBackgroundColor(0);
            if (Common.isLollipop())
                window.setStatusBarColor(0);
        }
    }
}
