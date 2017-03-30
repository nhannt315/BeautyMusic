package nhannt.musicplayer.ui.albumdetail;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.model.Album;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.custom.SquareImageView;
import nhannt.musicplayer.utils.Common;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAlbumDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlbumDetail extends BaseFragment {
    public static final String TAG = FragmentAlbumDetail.class.getName();

    private static final String KEY_ALBUM = "key_album";
    private static final String KEY_IS_TRANSITION = "key_is_transition";
    private static final String KEY_TRANSITION_NAME = "key_transition_name";

    private Album album;
    private boolean isTransition;
    private String transitionName = "";
    private ActionBar activityActionBar = null;

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
        if(Common.isMarshMallow() && isTransition){
            albumCover.setTransitionName(transitionName);
        }

        Glide.with(getContext()).load(album.getCoverPath()).into(albumCover);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    private void setupView() {
        setupToolbar();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) getView().findViewById(R.id.toolbar_album_detail);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(album.getTitle());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_ablum_detail;
    }

    @Override
    public AppCompatActivity getViewActivity() {
        return (AppCompatActivity) getActivity();
    }
}
