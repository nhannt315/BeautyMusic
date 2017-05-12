package nhannt.musicplayer.ui.home;


import android.util.Log;

import java.util.ArrayList;

import nhannt.musicplayer.R;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.utils.App;

/**
 * Created by NhanNT on 05/12/2017.
 */

public class HomeInteractor implements IHomeInteractor {

    private MediaProvider mediaProvider = MediaProvider.getInstance();

    @Override
    public ArrayList searchAll(String query) {
        ArrayList lstSearchResult = new ArrayList();
        ArrayList<Song> lstSong = mediaProvider.searchSongs(query);
        ArrayList<Album> lstAlbum = mediaProvider.searchAlbums(query);
        ArrayList<Artist> lstArtist = mediaProvider.searchArtists(query);

        if (!lstSong.isEmpty()) {
            lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.song));
            Log.d("searchSongSize", lstSong.size() + "");
            if (lstSong.size() > 5)
                lstSong = new ArrayList<>(lstSong.subList(0, 4));
            lstSearchResult.addAll(lstSong);
        }
        if (!lstAlbum.isEmpty()) {
            lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.album));
            Log.d("searchAlbumSize", lstAlbum.size() + "");
            if (lstAlbum.size() > 5)
                lstAlbum = new ArrayList<>(lstAlbum.subList(0, 4));
            lstSearchResult.addAll(lstAlbum);
        }
        if (!lstArtist.isEmpty()) {
            lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.artist));
            Log.d("searchArtistSize", lstArtist.size() + "");
            if (lstArtist.size() > 5)
                lstArtist = new ArrayList<>(lstArtist.subList(0, 4));
            lstSearchResult.addAll(lstArtist);
        }
        return lstSearchResult;
    }

    @Override
    public ArrayList searchAlbum(String query, int searchId) {
        ArrayList lstSearchResult = new ArrayList();
        ArrayList<Song> lstSong = mediaProvider.getListSongOfAlbum(searchId);
        ArrayList<Song> lstSongToRemove = new ArrayList<>();
        for (Song song : lstSong) {
            if (!song.getTitle().contains(query))
                lstSongToRemove.remove(song);
        }
        lstSong.removeAll(lstSongToRemove);
        if (!lstSong.isEmpty()) {
            lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.song));
            Log.d("searchSongSize", lstSong.size() + "");
            if (lstSong.size() > 5)
                lstSong = new ArrayList<>(lstSong.subList(0, 4));
            lstSearchResult.addAll(lstSong);
        }
        return lstSearchResult;
    }

    @Override
    public ArrayList searchArtist(String query, int searchId) {
        ArrayList lstSearchResult = new ArrayList();
        ArrayList<Song> lstSong = mediaProvider.getListSongOfArtist(searchId);
        ArrayList<Album> lstAlbum = mediaProvider.getListAlbumOfArtist(searchId);

        ArrayList<Song> lstSongToRemove = new ArrayList<>();
        ArrayList<Album> lstAlbumToRemove = new ArrayList<>();
        for (Song song : lstSong) {
            if (!song.getTitle().contains(query))
                lstSongToRemove.add(song);
        }
        lstSong.removeAll(lstSongToRemove);
        for (Album album : lstAlbum) {
            if (!album.getTitle().contains(query))
                lstAlbumToRemove.add(album);
        }
        lstAlbum.removeAll(lstAlbumToRemove);

        if (!lstSong.isEmpty()) {
            lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.song));
            Log.d("searchSongSize", lstSong.size() + "");
            if (lstSong.size() > 5)
                lstSong = new ArrayList<>(lstSong.subList(0, 4));
            lstSearchResult.addAll(lstSong);
        }
        if (!lstAlbum.isEmpty()) {
            lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.album));
            Log.d("searchAlbumSize", lstAlbum.size() + "");
            if (lstAlbum.size() > 5)
                lstAlbum = new ArrayList<>(lstAlbum.subList(0, 4));
            lstSearchResult.addAll(lstAlbum);
        }

        return lstSearchResult;
    }
}
