package nhannt.musicplayer.ui.itemlist.artistlist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.ArtistAdapter;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;
import nhannt.musicplayer.ui.itemlist.ItemListPresenter;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.DividerDecoration;
import nhannt.musicplayer.utils.ItemOffsetDecoration;
import nhannt.musicplayer.utils.Navigator;
import nhannt.musicplayer.utils.Setting;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentArtistList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArtistList extends BaseFragment implements ItemListMvpView<Artist>, RecyclerItemClickListener {

    public static final String TAG = FragmentArtistList.class.getName();
    public static final String TITLE = "Artist";

    @BindView(R.id.loading_indicator)
    protected AVLoadingIndicatorView mLoadingIndicator;
    @BindView(R.id.rv_artist_list_main)
    protected RecyclerView mRvArtistList;

    private ArtistListPresenter artistPresenter;
    private ArtistAdapter mAdapter;
    private ArrayList<Artist> mData;

    private DividerDecoration dividerItemDecoration;
    private ItemOffsetDecoration itemOffsetDecoration;

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
        setUpRecyclerView();
        artistPresenter.onResume();
        enableDoBack();
    }

    private void refreshRecyclerView() {
        RecyclerView.LayoutManager layoutManager;

        if (mAdapter.getLayoutType() == ArtistAdapter.LAYOUT_ITEM_LIST) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRvArtistList.removeItemDecoration(itemOffsetDecoration);
            mRvArtistList.addItemDecoration(dividerItemDecoration);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
            mRvArtistList.removeItemDecoration(dividerItemDecoration);
            mRvArtistList.addItemDecoration(itemOffsetDecoration);
        }

        mRvArtistList.setAdapter(mAdapter);
        mRvArtistList.setLayoutManager(layoutManager);
    }

    private void setUpRecyclerView() {
        dividerItemDecoration = new DividerDecoration(getActivity());
        itemOffsetDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_decoration);
        mRvArtistList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void doBack() {
        artistPresenter.cancelFetchingData();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_artist_list;
    }

    @Override
    public void setItems(ArrayList<Artist> itemList) {
        mData = itemList;
        mAdapter = new ArtistAdapter(getActivity(), itemList);
        mAdapter.setLayoutType(Setting.getInstance().get(Common.ARTIST_VIEW_MODE, ArtistAdapter.LAYOUT_ITEM_LIST));

        if (mAdapter.getLayoutType() == ArtistAdapter.LAYOUT_ITEM_LIST)
            mRvArtistList.addItemDecoration(dividerItemDecoration);
        else if (mAdapter.getLayoutType() == ArtistAdapter.LAYOUT_ITEM_GRID)
            mRvArtistList.addItemDecoration(itemOffsetDecoration);

        mAdapter.setRecyclerItemClickListener(this);

        RecyclerView.LayoutManager layoutManager;
        if (mAdapter.getLayoutType() == ArtistAdapter.LAYOUT_ITEM_LIST) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        }
        mRvArtistList.setAdapter(mAdapter);
        mRvArtistList.setLayoutManager(layoutManager);

        artistPresenter.sortAs(Setting.getInstance().get(Common.ARTIST_SORT_MODE, ItemListPresenter.SORT_AS_A_Z));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_view_as_list:
                mAdapter.setLayoutType(ArtistAdapter.LAYOUT_ITEM_LIST);
                Setting.getInstance().put(Common.ARTIST_VIEW_MODE, ArtistAdapter.LAYOUT_ITEM_LIST);
                refreshRecyclerView();
                notifyDataSetChanged();
                break;
            case R.id.bt_view_as_grid:
                mAdapter.setLayoutType(ArtistAdapter.LAYOUT_ITEM_GRID);
                Setting.getInstance().put(Common.ARTIST_VIEW_MODE, ArtistAdapter.LAYOUT_ITEM_GRID);
                refreshRecyclerView();
                notifyDataSetChanged();
                break;
            case R.id.bt_sort_a_z:
                artistPresenter.sortAs(ItemListPresenter.SORT_AS_A_Z);
                break;
            case R.id.bt_sort_z_a:
                artistPresenter.sortAs(ItemListPresenter.SORT_AS_Z_A);
                break;
            case R.id.bt_sort_song_no:
                artistPresenter.sortAs(ItemListPresenter.SORT_AS_NUMBER_OF_SONG);
                break;
            case R.id.bt_sort_album_no:
                artistPresenter.sortAs(ItemListPresenter.SORT_AS_NUMBER_OF_ALBUM);
                break;
            case R.id.bt_equalizer:
                Navigator.navigateToEqualizer(getContext(), 124);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSortBy = menu.findItem(R.id.bt_sort_by);
        Menu menuSortBy = itemSortBy.getSubMenu();
        menuSortBy.removeItem(R.id.bt_sort_artist);
        menuSortBy.removeItem(R.id.bt_sort_album);
        menuSortBy.removeItem(R.id.bt_sort_year);
        menuSortBy.removeItem(R.id.bt_sort_duration);
    }


    @Override
    public void showProgress() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRvArtistList.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        mLoadingIndicator.setVisibility(View.GONE);
        mRvArtistList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Artist> getListItem() {
        return mData;
    }

    @Override
    public void onDestroy() {
        artistPresenter.detachView();
        super.onDestroy();

    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onItemClickListener(View view, int position) {
        artistPresenter.onItemSelected(view, position);
    }

    @Override
    public void navigateToDetailFragment(Object object, @Nullable View transitionView) {

    }

}
