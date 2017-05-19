package nhannt.musicplayer.ui.playingqueue;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SongQueueAdapter;
import nhannt.musicplayer.interfaces.DrawerLayoutContainer;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.recyclerhelper.OnStartDragListener;
import nhannt.musicplayer.recyclerhelper.SimpleItemTouchHelperCallback;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.utils.ItemOffsetDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayingQueue#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayingQueue extends BaseFragment implements IPlayingQueueView,OnStartDragListener {

    public static final String TAG = FragmentPlayingQueue.class.getName();
    private static final String KEY_LST_SONG_QUEUE = "key_lst_song";

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @BindView(R.id.rv_list_song_queue)
    protected RecyclerView mRvLstSong;

    private SongQueueAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<Song> lstSong;
    private IPlayingQueuePresenter mPresenter;

    public FragmentPlayingQueue() {
        // Required empty public constructor
    }

    public static FragmentPlayingQueue newInstance() {
        FragmentPlayingQueue fragment = new FragmentPlayingQueue();
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
        return inflater.inflate(R.layout.fragment_playing_queue, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupDragRecyclerView();
        mPresenter = new PlayingQueuePresenter();
        mPresenter.attachedView(this);
        mPresenter.onResume();
        DrawerLayoutContainer container = (DrawerLayoutContainer) getActivity();
        container.setDrawerLayoutActionBarToggle(mToolbar);
        enableDoBack();
    }

    private void setupDragRecyclerView() {
        mAdapter = new SongQueueAdapter(getContext(), lstSong, this);
        mRvLstSong.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        mRvLstSong.setLayoutManager(layoutManager);
//        mRvLstSong.addItemDecoration(new DividerDecoration(getContext()));
        mRvLstSong.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_album_artist_detail_spacing));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void doBack() {
        mPresenter.cancelFetchingData();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_playing_queue;
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void setItems(ArrayList<Song> lstSong) {
        mAdapter = new SongQueueAdapter(getContext(),lstSong,this);
        mRvLstSong.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRvLstSong);
    }
}
