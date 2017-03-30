package nhannt.musicplayer.ui.itemlist.albumlist;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.AlbumAdapter;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.model.Album;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.custom.DividerDecoration;
import nhannt.musicplayer.ui.custom.ItemOffsetDecoration;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;
import nhannt.musicplayer.ui.itemlist.ItemListPresenter;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.Setting;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAlbumList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlbumList extends BaseFragment implements ItemListMvpView<Album>, RecyclerItemClickListener {

    public static final String TAG = FragmentAlbumList.class.getName();
    public static final String TITLE = "Albums";

    @BindView(R.id.progress_bar_ablum)
    protected ProgressBar mProgressBar;
    @BindView(R.id.rv_album_list_main)
    protected RecyclerView mRvAlbumList;

    private AlbumListPresenter albumPresenter;
    private ArrayList<Album> mData;
    private AlbumAdapter mAlbumAdapter;

    public FragmentAlbumList() {
        // Required empty public constructor
    }


    public static FragmentAlbumList newInstance() {
        FragmentAlbumList fragment = new FragmentAlbumList();
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
        albumPresenter = new AlbumListPresenter();
        albumPresenter.attachedView(this);
        setUpRecyclerView();
        albumPresenter.onResume();
    }


    private void setUpRecyclerView() {
        mRvAlbumList.addItemDecoration(new DividerDecoration(getActivity()));
        mRvAlbumList.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.item_decoration));
        mRvAlbumList.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSortBy = menu.findItem(R.id.bt_sort_by);
        Menu menuSortBy = itemSortBy.getSubMenu();
        menuSortBy.removeItem(R.id.bt_sort_album);
        menuSortBy.removeItem(R.id.bt_sort_album_no);
        menuSortBy.removeItem(R.id.bt_sort_duration);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_view_as_list:
                mAlbumAdapter.setLayoutType(AlbumAdapter.LAYOUT_ITEM_LIST);
                Setting.getInstance().put(Common.ALBUM_VIEW_MODE, AlbumAdapter.LAYOUT_ITEM_LIST);
                refreshRecyclerView();
                notifyDataSetChanged();
                break;
            case R.id.bt_view_as_grid:
                mAlbumAdapter.setLayoutType(AlbumAdapter.LAYOUT_ITEM_GRID);
                Setting.getInstance().put(Common.ALBUM_VIEW_MODE, AlbumAdapter.LAYOUT_ITEM_GRID);
                refreshRecyclerView();
                notifyDataSetChanged();
                break;
            case R.id.bt_sort_a_z:
                albumPresenter.sortAs(ItemListPresenter.SORT_AS_A_Z);
                break;
            case R.id.bt_sort_z_a:
                albumPresenter.sortAs(ItemListPresenter.SORT_AS_Z_A);
                break;
            case R.id.bt_sort_artist:
                albumPresenter.sortAs(ItemListPresenter.SORT_AS_ARTIST);
                break;
            case R.id.bt_sort_year:
                albumPresenter.sortAs(ItemListPresenter.SORT_AS_YEAR);
                break;
            case R.id.bt_sort_song_no:
                albumPresenter.sortAs(ItemListPresenter.SORT_AS_NUMBER_OF_SONG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_album_list;
    }

    @Override
    public void setItems(ArrayList<Album> itemList) {
        mData = itemList;
        mAlbumAdapter = new AlbumAdapter(getActivity(), itemList);
        mAlbumAdapter.setLayoutType(Setting.getInstance().get(Common.ALBUM_VIEW_MODE, AlbumAdapter.LAYOUT_ITEM_LIST));
        mAlbumAdapter.setRecyclerItemClickListener(this);
        refreshRecyclerView();
        albumPresenter.sortAs(Setting.getInstance().get(Common.ALBUM_SORT_MODE, ItemListPresenter.SORT_AS_A_Z));
    }

    private void refreshRecyclerView() {
        RecyclerView.LayoutManager layoutManager;
        if (mAlbumAdapter.getLayoutType() == mAlbumAdapter.LAYOUT_ITEM_LIST) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        }
        mRvAlbumList.setAdapter(mAlbumAdapter);
        mRvAlbumList.setLayoutManager(layoutManager);
    }


    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRvAlbumList.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mRvAlbumList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        albumPresenter.detachView();
        super.onDestroy();

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void notifyDataSetChanged() {
        mAlbumAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Album> getListItem() {
        return mData;
    }

    @Override
    public void onItemClickListener(View view, int position) {
        albumPresenter.onItemSelected(view, position);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public AppCompatActivity getViewActivity() {
        return (AppCompatActivity) getActivity();
    }

}
