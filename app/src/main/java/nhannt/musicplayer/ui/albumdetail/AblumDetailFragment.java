package nhannt.musicplayer.ui.albumdetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nhannt.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AblumDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AblumDetailFragment extends Fragment {

    public AblumDetailFragment() {
        // Required empty public constructor
    }

    public static AblumDetailFragment newInstance() {
        AblumDetailFragment fragment = new AblumDetailFragment();
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
        return inflater.inflate(R.layout.fragment_ablum_detail, container, false);
    }

}
