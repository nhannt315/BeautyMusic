package nhannt.musicplayer.views.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import nhannt.musicplayer.R;
import nhannt.musicplayer.core.BaseFragment;
import nhannt.musicplayer.model.Album;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAlbumList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlbumList extends BaseFragment {

    public static final String TITLE = "Albums";

    private ArrayList<Album> mData;

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
    protected int getLayout() {
        return R.layout.fragment_album_list;
    }

}
