package nhannt.musicplayer.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import nhannt.musicplayer.R;

/**
 * Created by nhannt on 01/03/2017.
 */

public abstract class BaseFragment extends Fragment implements BaseView {

    protected Toolbar toolbar;
    private Context mContext;

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

    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
    }

    private void settingToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            Log.d("BaseFragment","have toolbar");
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
    public Context getViewContext() {
        return mContext;
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
