package nhannt.musicplayer.ui.playlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.PlaylistAdapter;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlaylist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlaylist extends BaseFragment implements IPlaylistListView {

    public static final String TAG = FragmentPlaylist.class.getName();
    public static final int RECENT_PLAYED_ID = -1;
    public static final int RECENT_ADDED_ID = -2;

    @BindView(R.id.rv_playlist)
    protected RecyclerView rvPlaylistList;
    private PlaylistAdapter mPlaylistAdapter;
    private IPlaylistListPresenter mPresenter;

    public FragmentPlaylist() {
        // Required empty public constructor
    }

    public static FragmentPlaylist newInstance() {
        FragmentPlaylist fragment = new FragmentPlaylist();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setUpRecyclerView();
        mPresenter = new PlaylistListPresenter();
        mPresenter.attachedView(this);
        mPresenter.onResume();
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        rvPlaylistList.setLayoutManager(layoutManager);
        rvPlaylistList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_playlist;
    }

    @Override
    public void doBack() {

    }

    @Override
    public void setPlaylist(ArrayList<PlayList> itemList) {
        mPlaylistAdapter = new PlaylistAdapter(getContext(), itemList);
        rvPlaylistList.setAdapter(mPlaylistAdapter);
        mPlaylistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
