package nhannt.musicplayer.ui.albumdetail;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SongAdapter;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.model.Album;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.custom.DividerDecoration;
import nhannt.musicplayer.ui.custom.SquareImageView;
import nhannt.musicplayer.utils.BlurBuilder;
import nhannt.musicplayer.utils.Common;

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
    private ActionBar activityActionBar = null;
    private SongAdapter mSongAdapter;
    private ArrayList<Song> lstSong;
    private IAlbumDetailPresenter mPresenter;

    @BindView(R.id.iv_album_cover_album_detail)
    protected SquareImageView albumCover;

    @BindView(R.id.rv_song_list_album_detail)
    protected RecyclerView rvSongList;

    @BindView(R.id.toolbar_album_detail)
    protected Toolbar mToolbar;

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

        return view;
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
        if(album.getCoverPath() != null) {
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
    }

    private void setupToolbar() {
        if(mToolbar == null) Log.d("fragment album detail","tool bar null");
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
}
