package nhannt.musicplayer.data.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nhannt.musicplayer.data.network.VolleyConnection;
import nhannt.musicplayer.model.Album;
import nhannt.musicplayer.model.Artist;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.utils.AppController;

/**
 * Created by nhannt on 01/03/2017.
 */

public class MediaProvider {
    private static MediaProvider mInstance = null;
    private Context mContext = AppController.getInstance().getContext();
    private static final String LAST_FM_API_KEY = "761226e2f2b94da7de6a61d73f50e33c";
    private static final String URL_1 = "http://ws.audioscrobbler.com/2.0/?method=artist.search&artist=";
    private static final String URL_2 = "&api_key=" + LAST_FM_API_KEY + "&format=json";

    public static MediaProvider getInstance() {
        if (mInstance == null) {
            mInstance = new MediaProvider();
        }
        return mInstance;
    }


    public ArrayList<Song> getListSong() {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.YEAR}
                , null, null, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumPath = getCoverArtPath(albumID);
                Song item = new Song(songId, title, album, artist, albumPath, year, duration, path);

                lstSong.add(item);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return lstSong;
    }

    public ArrayList<Album> getListAlbum() {
        ArrayList<Album> lstAlbum = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                        MediaStore.Audio.Albums.FIRST_YEAR}
                , null, null, MediaStore.Audio.Albums.ALBUM + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String pathArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                int songCount = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                int year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.FIRST_YEAR));
                Album item = new Album(id, title, artist, pathArt, year, songCount);
                lstAlbum.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return lstAlbum;
    }

    public ArrayList<Artist> getListArtist() {
        ArrayList<Artist> lstArtist = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS}
                , null, null, MediaStore.Audio.Artists.ARTIST + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                int numberSong = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                int numberAlbum = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
                Artist item = new Artist(id, title, numberAlbum, numberSong);
                lstArtist.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        for (Artist artist : lstArtist) {
            getArtistPhoto(artist);
        }
        return lstArtist;
    }

    private void getArtistPhoto(final Artist mArtist) {
        String requestLink = URL_1 + mArtist.getName() + URL_2;
        Log.d(mArtist.getName() + " request link", requestLink);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                requestLink, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = response.optJSONObject("results");
                        JSONObject artistMatches = result.optJSONObject("artistmatches");
                        JSONArray listArtist = artistMatches.optJSONArray("artist");
                        if (listArtist != null) {
                            JSONObject artist = listArtist.optJSONObject(0);
                            if (artist != null) {
                                JSONArray photoList = artist.optJSONArray("image");
                                JSONObject photo = photoList.optJSONObject(2);
                                String url = photo.optString("#text");
                                Log.d(mArtist.getName() + " link day nhe", url);
                                mArtist.setImageUrl(url);

                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyConnection.getInstance(mContext).addRequestToQueue(jsonObjectRequest);
    }

    public ArrayList<Song> getListSongOfAlbum(int albumId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.YEAR},
                MediaStore.Audio.Media.ALBUM_ID + "=?", new String[]{String.valueOf(albumId)}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumPath = getCoverArtPath(albumID);
                Song item = new Song(songId, title, album, artist, albumPath, year, duration, path);
                lstSong.add(item);

            } while (cursor.moveToNext());
            cursor.close();
        }
        return lstSong;
    }

    public ArrayList<Song> getListSongOfArtist(int artistId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.YEAR},
                MediaStore.Audio.Media.ARTIST_ID + "=?", new String[]{String.valueOf(artistId)}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumPath = getCoverArtPath(albumID);
                Song item = new Song(songId, title, album, artist, albumPath, year, duration, path);
                lstSong.add(item);

            } while (cursor.moveToNext());
            cursor.close();
        }
        return lstSong;
    }

    public ArrayList<Album> getListAlbumOfArtist(int artistId) {
        ArrayList<Album> lstAlbum = new ArrayList<>();
        Uri.Builder builder = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.buildUpon();
        builder.appendPath(artistId + "");
        builder.appendPath("albums");
//        Cursor cursor =
        return lstAlbum;
    }

    public String getCoverArtPath(long albumId) {
        Cursor albumCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }

}
