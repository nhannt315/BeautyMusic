package nhannt.musicplayer.ui.home;


import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import nhannt.musicplayer.App;
import nhannt.musicplayer.R;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 05/12/2017.
 */

public class HomeInteractor implements IHomeInteractor {

    private final MediaProvider mediaProvider = MediaProvider.getInstance();
    private final IHomePresenter mPresenter;
    private AsyncTask<Void, Void, ArrayList> asyncSearhAll;
    private AsyncTask<Void, Void, ArrayList> asyncSearchAlbum;
    private AsyncTask<Void, Void, ArrayList> asyncSearchArtist;
    private AsyncTask<Void, Void, ArrayList> asyncSearchQueue;

    private ArrayList<Album> lstAlbumOrigin;
    private ArrayList<Artist> lstArtistOrigin;

    public HomeInteractor(IHomePresenter presenter) {
        this.mPresenter = presenter;
        lstAlbumOrigin = new ArrayList();
        lstArtistOrigin = new ArrayList();
    }

    @Override
    public void searchAll(final String query) {
        if (asyncSearhAll != null)
            asyncSearhAll.cancel(true);
        asyncSearhAll = new AsyncTask<Void, Void, ArrayList>() {


            @Override
            protected ArrayList doInBackground(Void... params) {
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
            protected void onPostExecute(ArrayList arrayList) {
                mPresenter.searchComplete(arrayList);
                super.onPostExecute(arrayList);

            }
        };
        asyncSearhAll.execute();
    }

    @Override
    public void searchAlbum(final String query, final int searchId) {
        if (asyncSearchAlbum != null)
            asyncSearchAlbum.cancel(true);
        asyncSearchAlbum = new AsyncTask<Void, Void, ArrayList>() {

            @Override
            protected ArrayList doInBackground(Void... params) {
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
            protected void onPostExecute(ArrayList arrayList) {
                mPresenter.searchComplete(arrayList);
                super.onPostExecute(arrayList);
            }
        };
        asyncSearchAlbum.execute();
    }

    @Override
    public void searchArtist(final String query, final int searchId) {
        if (asyncSearchArtist != null)
            asyncSearchArtist.cancel(true);
        asyncSearchArtist = new AsyncTask<Void, Void, ArrayList>() {

            @Override
            protected ArrayList doInBackground(Void... params) {
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

            @Override
            protected void onPostExecute(ArrayList arrayList) {
                mPresenter.searchComplete(arrayList);
                super.onPostExecute(arrayList);
            }
        };
        asyncSearchArtist.execute();
    }

    @Override
    public void searchPlayingQueue(final String keyword, final ArrayList<Song> lstSongPlaying) {
        if(asyncSearchQueue != null) asyncSearchQueue.cancel(true);
        asyncSearchQueue = new AsyncTask<Void, Void, ArrayList>() {
            @Override
            protected ArrayList doInBackground(Void... params) {

                ArrayList lstSearchResult = new ArrayList();
                if(lstSongPlaying == null || lstSongPlaying.size() == 0) return lstSearchResult;

                MediaProvider provider = MediaProvider.getInstance();
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
                super.onPostExecute(arrayList);
                mPresenter.searchComplete(arrayList);
            }
        };
        asyncSearchQueue.execute();
    }
}
