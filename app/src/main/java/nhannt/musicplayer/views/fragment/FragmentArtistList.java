package nhannt.musicplayer.views.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import nhannt.musicplayer.R;
import nhannt.musicplayer.core.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentArtistList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentArtistList extends BaseFragment {

    public static final String TITLE="Artist";

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
    protected int getLayout() {
        return R.layout.fragment_artist_list;
    }

}
