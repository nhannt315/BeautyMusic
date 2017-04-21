package nhannt.musicplayer.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nhannt on 21/03/2017.
 */

public class DBHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "music_database";
    public static final int DB_VERSION = 1;

    //Recent play table
    public static final String RECENT_TABLE = "recent";
    public static final String RECENT_COLUMN_ID = "recent_id";
    public static final String RECENT_COLUMN_SONG_ID = "recent_song_id";
    public static final String RECENT_COLUMN_TIME = "recent_time_play";

    //Playlist table
    public static final String PLAYLIST_TABLE = "playlist";
    public static final String PLAYLIST_COLUMN_ID = "playlist_id";
    public static final String PLAYLIST_COLUMN_TITLE = "playlist_title";
    public static final String PLAYLIST_COLUMN_SONG_NUMS = "playlist_song_nums";

    //Playlist detail table
    public static final String PLAYLIST_DETAIL_TABLE = "playlist_detail";
    public static final String DETAIL_COLUMN_PLAYLIST_ID = "playlist_detail_playlist_id";
    public static final String DETAIL_COLUMN_SONG_ID = "playlist_detail_song_id";
    public static final String SONG_NUMS = "song_nums";


    private static DBHelper mInstance;


    private DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DBHelper(context);
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createRecentPlayTable(db);
        createPlayListDetailTable(db);
        createPlaylistTable(db);
    }

    private void createRecentPlayTable(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS " + RECENT_TABLE + "(");
        builder.append(RECENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        builder.append(RECENT_COLUMN_SONG_ID + " TEXT,");
        builder.append(RECENT_COLUMN_TIME + " INTEGER");
        builder.append(")");
        db.execSQL(builder.toString());
    }

    private void createPlaylistTable(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS " + PLAYLIST_TABLE + "(");
        builder.append(PLAYLIST_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        builder.append(PLAYLIST_COLUMN_TITLE + " TEXT,");
        builder.append(PLAYLIST_COLUMN_SONG_NUMS + " INTEGER");
        builder.append(")");
        db.execSQL(builder.toString());
    }

    private void createPlayListDetailTable(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS " + PLAYLIST_DETAIL_TABLE + "(");
        builder.append(DETAIL_COLUMN_PLAYLIST_ID + " INTEGER REFERENCES "
                + PLAYLIST_TABLE + "(" + PLAYLIST_COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,");
        builder.append(DETAIL_COLUMN_SONG_ID + " TEXT,");
        builder.append("PRIMARY KEY (" + DETAIL_COLUMN_PLAYLIST_ID + "," + DETAIL_COLUMN_SONG_ID + ")");
        builder.append(")");
        db.execSQL(builder.toString());
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RECENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_DETAIL_TABLE);
        onCreate(db);
    }
}
