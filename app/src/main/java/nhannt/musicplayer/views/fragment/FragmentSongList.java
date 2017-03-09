package nhannt.musicplayer.views.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SongAdapter;
import nhannt.musicplayer.core.BaseFragment;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.presenter.RecyclerItemClickListener;
import nhannt.musicplayer.presenter.SongPresenter;
import nhannt.musicplayer.views.ItemListMvpView;
import nhannt.musicplayer.widget.ItemOffsetDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSongList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSongList extends BaseFragment implements ItemListMvpView<Song>, RecyclerItemClickListener {

    public static final String TITLE = "Songs";
    private ArrayList<Song> mData;
    @BindView(R.id.rv_song_list_main)
    RecyclerView rvSongList;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private SongPresenter songPresenter;
    private SongAdapter songAdapter;
    private Context mContext;


    public FragmentSongList() {
        // Required empty public constructor
    }

    public static FragmentSongList newInstance() {
        FragmentSongList fragment = new FragmentSongList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        songPresenter.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        songPresenter = new SongPresenter();
        songPresenter.attachedView(this);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rvSongList.setLayoutManager(layoutManager);
        rvSongList.addItemDecoration(new ItemOffsetDecoration(rvSongList.getContext(), R.dimen.item_decoration));
        rvSongList.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_song_list;
    }

    @Override
    public void onItemClickListener(int position) {
        songPresenter.onItemSelected(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void setItems(ArrayList<Song> itemList) {
        songAdapter = new SongAdapter(getActivity(), itemList);
        songAdapter.setRecyclerItemClickListener(this);
        rvSongList.setAdapter(songAdapter);

    }

    @Override
    public Context getMContext() {
        return mContext;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        rvSongList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        rvSongList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onDestroy() {
        songPresenter.detachView();
        super.onDestroy();
    }
}
