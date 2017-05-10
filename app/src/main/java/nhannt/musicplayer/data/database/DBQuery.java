package nhannt.musicplayer.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by nhannt on 24/03/2017.
 */

public class DBQuery {
    private static DBQuery mInstance;
    private Context mContext;
    private SQLiteDatabase db;

    private DBQuery(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * Get instance of DBQuery
     *
     * @param context using this DbQuery
     * @return instance
     */
    public static synchronized DBQuery getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBQuery(context);
        }
        return mInstance;
    }

    /**
     * Open song database
     *
     * @return A opened database
     */
    private SQLiteDatabase getDatabase() {
        return getDatabase(true);
    }

    private SQLiteDatabase getDatabase(boolean isWrite) {
        if (isWrite)
            return DBHelper.getInstance(mContext).getWritableDatabase();
        else
            return DBHelper.getInstance(mContext).getReadableDatabase();
    }

    public boolean insertSongToPlaylist(int playlistID, String songID) {
        if (isSongExist(playlistID, songID))
            return false;
        db = getDatabase();
        ContentValues value = new ContentValues();
        value.put(DBHelper.DETAIL_COLUMN_PLAYLIST_ID, playlistID);
        value.put(DBHelper.DETAIL_COLUMN_SONG_ID, songID);
        long id = db.insert(DBHelper.PLAYLIST_DETAIL_TABLE, null, value);
        db.close();

        return (id != -1);
    }

    public PlayList getLastInsertedPlaylist() {
        PlayList playList = null;
        db = getDatabase();
        String sql = "SELECT * FROM " + DBHelper.PLAYLIST_TABLE + " ORDER BY " + DBHelper.PLAYLIST_COLUMN_ID + " DESC";
        Cursor result = db.rawQuery(sql, null);
        if (result != null && result.moveToFirst()) {
            try {
                playList = new PlayList();
                playList.setId(result.getInt(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_ID)));
                playList.setTitle(result.getString(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_TITLE)));
                playList.setSongNums(result.getInt(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_SONG_NUMS)));
            } finally {
                result.close();
            }

        }

        db.close();
        return playList;
    }

    public int insertSongToPlayList(int playlistID, ArrayList<Song> listSong) {
        ContentValues value = new ContentValues();
        int num = 0;
        for (Song song : listSong) {
            if (isSongExist(playlistID, song.getId()))
                continue;
            db = getDatabase();
            value.clear();
            value.put(DBHelper.DETAIL_COLUMN_PLAYLIST_ID, playlistID);
            value.put(DBHelper.DETAIL_COLUMN_SONG_ID, song.getId());
            long id = db.insert(DBHelper.PLAYLIST_DETAIL_TABLE, null, value);
            if (id != -1) num++;
        }
        db.close();
        return num;
    }

    public long insertPlaylist(String playlistName) {
        playlistName = playlistName.trim();
        db = getDatabase();
        ContentValues value = new ContentValues();
        value.put(DBHelper.PLAYLIST_COLUMN_TITLE, playlistName);
        long id = db.insert(DBHelper.PLAYLIST_TABLE, null, value);
        db.close();
        return id;
    }

    public boolean updatePlaylist(int playlistID, String playlistName) {
        playlistName = playlistName.trim();
        db = getDatabase();
        ContentValues value = new ContentValues();
        value.put(DBHelper.PLAYLIST_COLUMN_TITLE, playlistName);
        int num = db.update(DBHelper.PLAYLIST_TABLE, value, DBHelper.PLAYLIST_COLUMN_ID + "=" + playlistID, null);
        db.close();
        return (num > 0);
    }


    public long insertRecentPlay(Song song) {
        db = getDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.RECENT_COLUMN_SONG_ID, song.getId());
        long id = db.insertWithOnConflict(DBHelper.RECENT_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    /**
     * Get list song recent play from database
     *
     * @return list song
     */
    public ArrayList<Song> getListSongRecentPlay() {
        ArrayList<Song> lstSong = new ArrayList<>();
        MediaProvider provider = MediaProvider.getInstance();
        db = getDatabase();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT " + DBHelper.RECENT_TABLE + ".*");
        builder.append(" FROM " + DBHelper.RECENT_TABLE);
        Cursor result = db.rawQuery(builder.toString(), null);
        if (result != null && result.moveToFirst()) {
            try {
                do {
                    String songId = result.getString(result.getColumnIndex(DBHelper.RECENT_COLUMN_SONG_ID));
                    Song song = provider.getSongById(songId);
                    lstSong.add(song);
                } while (result.moveToNext());
            } finally {
                result.close();
            }
        }

        db.close();
        return lstSong;
    }

    public ArrayList<Song> getListSongByPlaylist(int pID) {
        ArrayList<Song> lstSong = new ArrayList<>();
        MediaProvider provider = MediaProvider.getInstance();
        db = getDatabase();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT " + DBHelper.PLAYLIST_DETAIL_TABLE + ".*");
        builder.append(" FROM " + DBHelper.PLAYLIST_DETAIL_TABLE);
        builder.append(" WHERE " + DBHelper.PLAYLIST_DETAIL_TABLE + "." + DBHelper.DETAIL_COLUMN_PLAYLIST_ID + " = " + pID);
        Cursor result = db.rawQuery(builder.toString(), null);
        if (result != null && result.moveToFirst()) {
            try {
                do {
                    String songId = result.getString(result.getColumnIndex(DBHelper.DETAIL_COLUMN_SONG_ID));
                    Song song = provider.getSongById(songId);
                    lstSong.add(song);
                } while (result.moveToNext());
            } finally {
                result.close();
            }
        }

        db.close();
        return lstSong;
    }

    public ArrayList<PlayList> getAllPlayList() {
        ArrayList<PlayList> lstPlayList;
        lstPlayList = new ArrayList<>();
        db = getDatabase();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM " + DBHelper.PLAYLIST_TABLE);
        Cursor result = db.rawQuery(builder.toString(), null);
        if (result != null && result.moveToFirst()) {
            try {
                do {
                    PlayList playList = new PlayList();
                    int id = result.getInt(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_ID));
                    String title = result.getString(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_TITLE));
                    int songNums = result.getInt(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_SONG_NUMS));
                    playList.setId(id);
                    playList.setTitle(title);
                    playList.setSongNums(songNums);

                    lstPlayList.add(playList);
                } while (result.moveToNext());
            } finally {
                result.close();
            }
        }
        Log.d("CheckPlaylist", lstPlayList.size() + "");
        db.close();
        return lstPlayList;
    }

    public PlayList getPlayListById(int playListId) {
        PlayList playList = null;
        db = getDatabase();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT " + DBHelper.PLAYLIST_COLUMN_ID + ", " + DBHelper.PLAYLIST_COLUMN_TITLE
                + "," + DBHelper.PLAYLIST_COLUMN_SONG_NUMS);
        builder.append(" FROM " + DBHelper.PLAYLIST_TABLE + " LEFT JOIN ").append(DBHelper.PLAYLIST_DETAIL_TABLE);
        builder.append(" ON " + DBHelper.PLAYLIST_TABLE + "." + DBHelper.PLAYLIST_COLUMN_ID + " = "
                + DBHelper.PLAYLIST_DETAIL_TABLE + ".").append(DBHelper.DETAIL_COLUMN_PLAYLIST_ID);
        builder.append(" WHERE " + DBHelper.PLAYLIST_TABLE + "." + DBHelper.PLAYLIST_COLUMN_ID + " = " + playListId);

        Cursor result = db.rawQuery(builder.toString(), null);
        if (result != null && result.moveToFirst()) {
            try {
                do {
                    playList = new PlayList();
                    playList.setId(result.getInt(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_ID)));
                    playList.setTitle(result.getString(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_TITLE)));
                    playList.setSongNums(result.getInt(result.getColumnIndex(DBHelper.PLAYLIST_COLUMN_SONG_NUMS)));
                } while (result.moveToNext());
            } finally {
                result.close();
            }
        }

        db.close();
        return playList;
    }

    public boolean removeSongFromPlaylist(int playlistId, String songID) {
        db = getDatabase();
        int num = db.delete(DBHelper.PLAYLIST_DETAIL_TABLE, DBHelper.DETAIL_COLUMN_PLAYLIST_ID + "=" + playlistId
                + " AND " + DBHelper.DETAIL_COLUMN_SONG_ID + " = '" + songID + "'", null);
        db.close();
        return (num > 0);
    }

    public int removeAllSongFromPlaylist(int playlistID) {
        db = getDatabase();
        int num = db.delete(DBHelper.PLAYLIST_DETAIL_TABLE, DBHelper.DETAIL_COLUMN_PLAYLIST_ID + "=" + playlistID, null);
        db.close();
        return num;
    }

    public boolean removePlaylist(int playlistID) {
        db = getDatabase();
        int num = db.delete(DBHelper.PLAYLIST_TABLE, DBHelper.PLAYLIST_COLUMN_ID + "='" + playlistID + "'", null);
        db.close();
        return (num > 0);
    }

    private boolean isSongExist(int playlistID, String songID) {
        boolean isExist = false;
        db = getDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DBHelper.DETAIL_COLUMN_SONG_ID + " FROM " + DBHelper.PLAYLIST_DETAIL_TABLE
                + " WHERE " + DBHelper.DETAIL_COLUMN_SONG_ID + " = '" + songID
                + "' AND " + DBHelper.DETAIL_COLUMN_PLAYLIST_ID + " = " + playlistID, null);
        if (cursor.moveToFirst())
            isExist = true;
        cursor.close();
        db.close();
        return isExist;
    }
}
