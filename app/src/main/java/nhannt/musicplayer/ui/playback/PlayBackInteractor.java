package nhannt.musicplayer.ui.playback;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.R;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.utils.App;

/**
 * Created by NhanNT on 05/16/2017.
 */

public class PlayBackInteractor implements IPlayBackInteractor {

    private IPlayBackPresenter mPresenter;
    private ArrayList<Song> lstSongPlaying;
    private String keyword;
    private SearchNowPlayingSong asyncSearch;
    private ArrayList<Album> lstAlbumOrigin;
    private ArrayList<Artist> lstArtistOrigin;

    public PlayBackInteractor(IPlayBackPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void search(ArrayList<Song> lstSongPlaying, String keyword) {
        this.lstSongPlaying = lstSongPlaying;
        this.keyword = keyword;
        lstAlbumOrigin = new ArrayList<>();
        lstArtistOrigin = new ArrayList<>();
        if (asyncSearch != null)
            asyncSearch.cancel(true);
        asyncSearch = new SearchNowPlayingSong();
        asyncSearch.execute();
    }


    private class SearchNowPlayingSong extends AsyncTask<Void, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(Void... params) {
            MediaProvider provider = MediaProvider.getInstance();
            ArrayList lstSearchResult = new ArrayList();
            if (lstSongPlaying == null) return lstSearchResult;

            if (lstAlbumOrigin.isEmpty() && lstArtistOrigin.isEmpty())
                for (Song song : lstSongPlaying) {
                    lstAlbumOrigin.add(provider.getAlbumById(song.getAlbumId()));
                    lstArtistOrigin.add(provider.getArtistById(song.getArtistId()));
                }

            ArrayList<Song> lstSong = new ArrayList<>(lstSongPlaying);
            ArrayList<Album> lstAlbum = new ArrayList<>(lstAlbumOrigin);
            ArrayList<Artist> lstArtist = new ArrayList<>(lstArtistOrigin);

            //Remove duplicate elements
            ArrayList<Album> lstAlbumNew = new ArrayList<>();
            for (Album album : lstAlbum) {
                if (!lstAlbumNew.contains(album))
                    lstAlbumNew.add(album);
            }
            lstAlbum = lstAlbumNew;

            ArrayList<Artist> lstArtistNew = new ArrayList<>();
            for (Artist artist : lstArtist)
                if (!lstArtistNew.contains(artist))
                    lstArtistNew.add(artist);
            lstArtist = lstArtistNew;

            ArrayList<Song> lstSongRemove = new ArrayList<>();
            for (Song song : lstSong) {
                if (!song.getTitle().contains(keyword))
                    lstSongRemove.add(song);
            }
            lstSong.removeAll(lstSongRemove);

            //Remove all elements which doesnt contain keyword
            ArrayList<Album> lstAlbumRemove = new ArrayList<>();
            for (Album album : lstAlbum) {
                if (album == null) {
                    lstAlbumRemove.add(album);
                    continue;
                }
                if (!album.getTitle().contains(keyword))
                    lstAlbumRemove.add(album);
            }
            lstAlbum.removeAll(lstAlbumRemove);

            ArrayList<Artist> lstArtistRemove = new ArrayList<>();
            for (Artist artist : lstArtist) {
                if (artist == null) {
                    lstArtistRemove.add(artist);
                    continue;
                }
                if (!artist.getName().contains(keyword))
                    lstArtistRemove.add(artist);
            }
            lstArtist.removeAll(lstArtistRemove);

            //Make sure each type of element in result list have 5 elements max
            if (lstSong.size() > 0) {
                if (lstSong.size() > 5) {
                    lstSong = new ArrayList<>(lstSong.subList(0, 4));
                }
                lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.song));
                lstSearchResult.addAll(lstSong);

            }
            if (lstAlbum.size() > 0) {
                if (lstAlbum.size() > 5) {
                    lstAlbum = new ArrayList<>(lstAlbum.subList(0, 4));
                }
                lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.album));
                lstSearchResult.addAll(lstAlbum);
            }

            if (lstArtist.size() > 0) {
                if (lstArtist.size() > 5) {
                    lstArtist = new ArrayList<>(lstArtist.subList(0, 4));
                }
                lstSearchResult.add(App.getInstance().getApplicationContext().getString(R.string.artist));
                lstSearchResult.addAll(lstArtist);
            }

            return lstSearchResult;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            mPresenter.searchComplete(arrayList);
            super.onPostExecute(arrayList);
        }
    }
}
