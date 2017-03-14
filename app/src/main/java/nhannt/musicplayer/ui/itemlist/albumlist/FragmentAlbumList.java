package nhannt.musicplayer.ui.itemlist.albumlist;


import android.content.Context;
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
import nhannt.musicplayer.adapter.AlbumAdapter;
import nhannt.musicplayer.model.Album;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.custom.DividerDecoration;
import nhannt.musicplayer.ui.custom.ItemOffsetDecoration;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAlbumList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlbumList extends BaseFragment implements ItemListMvpView<Album> {

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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        albumPresenter.onResume();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_album_list;
    }

    @Override
    public void setItems(ArrayList<Album> itemList) {
        mData = itemList;
        mAlbumAdapter = new AlbumAdapter(getActivity(), itemList, AlbumAdapter.LAYOUT_ITEM_LIST);
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {
        RecyclerView.LayoutManager layoutManager;
        if (mAlbumAdapter.getLayoutType() == mAlbumAdapter.LAYOUT_ITEM_LIST) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRvAlbumList.addItemDecoration(new DividerDecoration(getActivity()));
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
            mRvAlbumList.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.item_decoration));
        }
        mRvAlbumList.setAdapter(mAlbumAdapter);
        mRvAlbumList.setLayoutManager(layoutManager);
        mRvAlbumList.setItemAnimator(new DefaultItemAnimator());
        mAlbumAdapter.notifyDataSetChanged();
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
    public void notifyDataSetChange() {
        mAlbumAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Album> getListItem() {
        return mData;
    }
}
