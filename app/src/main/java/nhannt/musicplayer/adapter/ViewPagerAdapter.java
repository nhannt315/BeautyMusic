package nhannt.musicplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nhannt.musicplayer.views.fragment.FragmentAlbumList;
import nhannt.musicplayer.views.fragment.FragmentArtistList;
import nhannt.musicplayer.views.fragment.FragmentSongList;

/**
 * Created by nhannt on 03/03/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FragmentSongList.newInstance();
            case 1:
                return FragmentAlbumList.newInstance();
            case 2:
                return FragmentArtistList.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return FragmentSongList.TITLE;
            case 1:
                return FragmentAlbumList.TITLE;
            case 2:
                return FragmentArtistList.TITLE;
            default:
                return "";
        }
    }
}
