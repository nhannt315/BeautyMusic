package nhannt.musicplayer.ui.itemlist.artistlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.ArtistAdapter;
import nhannt.musicplayer.model.Artist;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.custom.DividerDecoration;
import nhannt.musicplayer.ui.custom.ItemOffsetDecoration;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentArtistList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArtistList extends BaseFragment implements ItemListMvpView<Artist> {

    public static final String TAG = FragmentArtistList.class.getName();
    public static final String TITLE="Artist";

    @BindView(R.id.progress_bar_artist)
    protected ProgressBar mProgressBar;
    @BindView(R.id.rv_artist_list_main)
    protected RecyclerView mRvArtistList;

    ArtistListPresenter artistPresenter;
    ArtistAdapter mAdapter;
    ArrayList<Artist> mData;

    public FragmentArtistList() {
        // Required empty public constructor
    }

    public static FragmentArtistList newInstance() {
        FragmentArtistList fragment = new FragmentArtistList();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        artistPresenter = new ArtistListPresenter();
        artistPresenter.attachedView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        artistPresenter.onResume();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_artist_list;
    }

    @Override
    public void setItems(ArrayList<Artist> itemList) {
        mAdapter = new ArtistAdapter(getActivity(), itemList, ArtistAdapter.LAYOUT_ITEM_LIST);
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {
        RecyclerView.LayoutManager layoutManager;
        if (mAdapter.getLayoutType() == mAdapter.LAYOUT_ITEM_LIST) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRvArtistList.addItemDecoration(new DividerDecoration(getActivity()));
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
            mRvArtistList.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.item_decoration));
        }
        mRvArtistList.setAdapter(mAdapter);
        mRvArtistList.setLayoutManager(layoutManager);
        mRvArtistList.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRvArtistList.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mRvArtistList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void notifyDataSetChange() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Artist> getListItem() {
        return mData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        artistPresenter.detachView();
    }
}
