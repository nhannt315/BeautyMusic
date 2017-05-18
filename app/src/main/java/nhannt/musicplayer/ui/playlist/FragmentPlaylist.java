package nhannt.musicplayer.ui.playlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.PlaylistAdapter;
import nhannt.musicplayer.interfaces.DrawerLayoutContainer;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.dialog.CreatePlaylistDialog;
import nhannt.musicplayer.utils.ItemOffsetDecoration;
import nhannt.musicplayer.utils.Navigator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlaylist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlaylist extends BaseFragment implements IPlaylistListView {

    public static final String TAG = FragmentPlaylist.class.getName();
    public static final int RECENT_PLAYED_ID = -1;
    public static final int RECENT_ADDED_ID = -2;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @BindView(R.id.rv_playlist)
    protected RecyclerView rvPlaylistList;
    @BindView(R.id.loading_indicator)
    protected AVLoadingIndicatorView mLoadingIndicator;

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
        setHasOptionsMenu(true);
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        DrawerLayoutContainer container = (DrawerLayoutContainer) getActivity();
        container.setDrawerLayoutActionBarToggle(mToolbar);
        enableDoBack();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_play_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_new_playList:
                new CreatePlaylistDialog(getContext()).show();
                break;
            case R.id.btn_shuffle_all_menu:
                break;
            case R.id.btn_equalizer:
                Navigator.navigateToEqualizer(getContext(), 1234);
                break;
        }
        return true;
    }

    @Override
    public void onStop() {
        mPresenter.cancelFetchingData();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void doBack() {
        mPresenter.cancelFetchingData();
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        rvPlaylistList.setLayoutManager(layoutManager);
        rvPlaylistList.setItemAnimator(new DefaultItemAnimator());
        rvPlaylistList.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_playlist_spacing));
    }

    @Override
    public void showProgress() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_playlist;
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
