package nhannt.musicplayer.ui.itemlist;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.ViewPagerAdapter;
import nhannt.musicplayer.ui.base.BaseFragment;


public class FragmentMain extends BaseFragment {

    public static final String TAG = FragmentMain.class.getName();

    @BindView(R.id.main_tab_layout)
    protected TabLayout tabLayout;
    @BindView(R.id.main_view_pager)
    protected ViewPager viewPager;

    public static FragmentMain newInstance() {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        settingViews();
    }


    private void settingViews() {
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }


    @Override
    public void doBack() {
        getActivity().onBackPressed();
    }
}
