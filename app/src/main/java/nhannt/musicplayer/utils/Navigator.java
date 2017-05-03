package nhannt.musicplayer.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.View;

import nhannt.musicplayer.R;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.ui.albumdetail.FragmentAlbumDetail;
import nhannt.musicplayer.ui.artistdetail.FragmentArtistDetail;
import nhannt.musicplayer.ui.playback.PlayBackActivity;

/**
 * Created by nhannt on 31/03/2017.
 */

public class Navigator {
    public static void navigateToAlbumDetail(Context context, Album album, @Nullable View transitionView) {
        Fragment desFragment;
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        if (transitionView != null) {
            if (Common.isLollipop()) {
                String transitionName = transitionView.getTransitionName();
                desFragment = FragmentAlbumDetail.newInstance(album, true, transitionName);
                desFragment.setSharedElementEnterTransition(TransitionInflater.from(
                        activity).inflateTransition(R.transition.image_tran));
                fragmentTransaction.addSharedElement(transitionView, transitionName);
            } else {
                desFragment = FragmentAlbumDetail.newInstance(album, false, "");
            }
        } else {
            desFragment = FragmentAlbumDetail.newInstance(album, false, "");
        }

        fragmentTransaction.hide(activity.getSupportFragmentManager().findFragmentById(R.id.container))
                .hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.container))
                .add(R.id.container, desFragment)
                .addToBackStack(FragmentAlbumDetail.TAG)
                .commit();
    }

    public static void navigateToArtistDetail(Context context, Artist artist, @Nullable View transitionView){
        Fragment desFragment;
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        if (transitionView != null) {
            if (Common.isLollipop()) {
                String transitionName = transitionView.getTransitionName();
                desFragment = FragmentArtistDetail.newInstance(artist, true, transitionName);
                desFragment.setSharedElementEnterTransition(TransitionInflater.from(
                        activity).inflateTransition(R.transition.image_tran));
                fragmentTransaction.addSharedElement(transitionView, transitionName);
            } else {
                desFragment = FragmentArtistDetail.newInstance(artist, false, "");
            }
        } else {
            desFragment = FragmentArtistDetail.newInstance(artist, false, "");
        }

        fragmentTransaction.hide(activity.getSupportFragmentManager().findFragmentById(R.id.container))
                .hide(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.container))
                .add(R.id.container, desFragment)
                .addToBackStack(FragmentAlbumDetail.TAG)
                .commit();
    }

    public static void navigateToPlayBackActivity(Context context) {
        Intent intent = new Intent(context, PlayBackActivity.class);
        context.startActivity(intent);
    }
}
