package nhannt.musicplayer.data.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import nhannt.musicplayer.App;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by nhannt on 01/03/2017.
 */

public class MediaProvider {
    private static MediaProvider mInstance = null;
    private final Context mContext;

    public static MediaProvider getInstance() {
        if (mInstance == null) {
            mInstance = new MediaProvider();
        }
        return mInstance;
    }

    private MediaProvider() {
        this.mContext = App.getInstance().getApplicationContext();
    }


    public ArrayList<Song> getListSong() {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ARTIST_ID,
                        MediaStore.Audio.Media.YEAR}
                , null, null, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {

                    lstSong.add(makeSongCursor(cursor));

                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }

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
            try {
                do {

                    lstAlbum.add(makeAlbumCursor(cursor));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
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
            try {
                do {
                    lstArtist.add(makeArtistCursor(cursor));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return lstArtist;
    }

    public ArrayList<Song> searchSongs(String keyword) {
        ArrayList<Song> lstSong = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.YEAR};

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection
                , MediaStore.Audio.Media.TITLE + " like ?", new String[]{"%" + keyword + "%"}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    lstSong.add(makeSongCursor(cursor));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }

        return lstSong;
    }

    public ArrayList<Album> searchAlbums(String keyword) {
        ArrayList<Album> lstAlbum = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.FIRST_YEAR};

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection
                , MediaStore.Audio.Albums.ALBUM + " like ?", new String[]{"%" + keyword + "%"}, MediaStore.Audio.Albums.ALBUM + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    lstAlbum.add(makeAlbumCursor(cursor));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return lstAlbum;
    }

    public ArrayList<Artist> searchArtists(String keyword) {
        ArrayList<Artist> lstArtist = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS}
                , MediaStore.Audio.Artists.ARTIST + " like ?", new String[]{"%" + keyword + "%"}, MediaStore.Audio.Artists.ARTIST + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    lstArtist.add(makeArtistCursor(cursor));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }

        return lstArtist;
    }

    public Artist getArtistById(int artistId) {
        Artist artist = new Artist();
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS}
                , MediaStore.Audio.Artists._ID + " =?", new String[]{artistId + ""}, MediaStore.Audio.Artists.ARTIST + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            try {
                artist = makeArtistCursor(cursor);
            } finally {
                cursor.close();
            }
        }
        return artist;
    }


    public Song getSongById(String songId) {
        Song song = null;
        Uri mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.YEAR};
        String selection = MediaStore.Audio.Media._ID + "=?";
        String[] selectionArgs = new String[]{"" + songId};
        Cursor cursor = mContext.getContentResolver().query(mediaContentUri, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                song = makeSongCursor(cursor);
            } finally {
                cursor.close();
            }
        }
        return song;
    }

    public ArrayList<Song> getListSongOfAlbum(int albumId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ARTIST_ID,
                        MediaStore.Audio.Media.YEAR},
                MediaStore.Audio.Media.ALBUM_ID + "=?", new String[]{String.valueOf(albumId)}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {

                    lstSong.add(makeSongCursor(cursor));

                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return lstSong;
    }

    public ArrayList<Song> getListSongOfArtist(int artistId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ARTIST_ID,
                        MediaStore.Audio.Media.YEAR},
                MediaStore.Audio.Media.ARTIST_ID + "=?", new String[]{String.valueOf(artistId)}, MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    lstSong.add(makeSongCursor(cursor));

                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return lstSong;
    }

    public Album getAlbumById(int albumId) {
        Album album = null;
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                        MediaStore.Audio.Albums.FIRST_YEAR}
                , MediaStore.Audio.Albums._ID + "=?", new String[]{albumId+""}, MediaStore.Audio.Albums.ALBUM + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            try {
                album = makeAlbumCursor(cursor);

            } finally {
                cursor.close();
            }
        }

        return album;
    }


    public ArrayList<Album> getListAlbumOfArtist(int artistId) {
        ArrayList<Song> lstSong = getListSongOfArtist(artistId);
        ArrayList<Album> lstAlbum = new ArrayList<>();
        for (Song song : lstSong) {
            Album album = getAlbumById(song.getAlbumId());
            lstAlbum.add(album);
        }
        return lstAlbum;
    }

    private String getCoverArtPath(long albumId) {
        Cursor albumCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        String result = null;
        if (albumCursor != null) {
            try {
                boolean queryResult = albumCursor.moveToFirst();

                if (queryResult) {
                    result = albumCursor.getString(0);
                }
            } finally {
                albumCursor.close();
            }
        }
        return result;
    }

    private Song makeSongCursor(Cursor cursor){
        Song song = new Song();

        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        int year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR));
        int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        int artistId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
        String albumPath = getCoverArtPath(albumID);

        song.setTitle(title);
        song.setAlbum(album);
        song.setId(songId);
        song.setDuration(duration);
        song.setSongPath(path);
        song.setCoverPath(albumPath);
        song.setYear(year);
        song.setArtist(artist);
        song.setArtistId(artistId);
        song.setAlbumId(albumID);
        return song;
    }

    private Album makeAlbumCursor(Cursor cursor){
        Album album = new Album();
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        String pathArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
        int songCount = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
        int year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.FIRST_YEAR));

        album.setId(id);
        album.setTitle(title);
        album.setCoverPath(pathArt);
        album.setArtist(artist);
        album.setSongCount(songCount);
        album.setYear(year);

        return album;
    }

    private Artist makeArtistCursor(Cursor cursor){
        Artist artist = new Artist();
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
        int numberSong = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
        int numberAlbum = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));

        artist.setName(title);
        artist.setId(id);
        artist.setNumberOfSong(numberSong);
        artist.setNumberOfAlbum(numberAlbum);

        return artist;
    }

}
