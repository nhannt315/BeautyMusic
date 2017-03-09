package nhannt.musicplayer.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import nhannt.musicplayer.R;

/**
 * Created by nhannt on 01/03/2017.
 */

public abstract class BaseFragment extends Fragment {

    protected Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        settingToolbar(view);
    }

    private void settingToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                if (getHomeAsUpIndicator() != 0) {
                    actionBar.setHomeAsUpIndicator(getHomeAsUpIndicator());
                }
                actionBar.setTitle(getActionbarName());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected abstract int getLayout();

    protected int getHomeAsUpIndicator() {
        return 0;
    }

    protected String getActionbarName() {
        return getActivity().getResources().getString(R.string.app_name);
    }

    protected void setActionBarName(String name) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
        }
    }

    protected ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    protected void popCurrentFragment() {
        ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack();
    }

}
