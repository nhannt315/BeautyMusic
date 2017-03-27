package nhannt.musicplayer.data.dbhandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nhannt on 21/03/2017.
 */

public class MusicDatabase extends SQLiteOpenHelper {


    public static final String DB_NAME = "music_database";
    public static final int DB_VERSION = 1;

    //Recent play table
    public static final String RECENT_TABLE_NAME="recent";
    public static final String RECENT_COLUMN_ID="recent_id";
    public static final String RECENT_COLUMN_SONG_ID="recent_song_id";
    public static final String RECENT_COLUMN_TIME="recent_time_play";

    //Playlist table
    public static final String PLAYLIST_TABLE_NAME="playlist";
    public static final String PLAYLIST_COLUMN_ID="playlist_id";
    public static final String PLAYLIST_COLUMN_TITLE="playlist_title";

    //Playlist detail table
    public static final String PLAYLIST_DETAIL_TABLE_NAME = "playlist_detail";
    public static final String DETAIL_COLUMN_PLAYLIST_ID = "playlist_detail_playlist_id";
    public static final String DETAIL_COLUMN_SONG_ID = "playlist_detail_song_id";

    //Song table
    public static final String SONG_TABLE_NAME="song";
    public static final String SONG_COLUMN_ID="song_id";
    public static final String SONG_COLUMN_TILE="song_title";
    public static final String SONG_COLUMN_ABLUM="song_album";
    public static final String SONG_COLUMN_ARTIST="song_artist";
    public static final String SONG_COLUMN_DURATION="song_duration";
    public static final String SONG_COLUMN_PATH="song_path";
    public static final String SONG_COLUMN_COVER_PATH="song_cover_path";
    public static final String SONG_COLUMN_YEAR="song_year";



    private static MusicDatabase mInstance;
    private Context mContext;


    private MusicDatabase(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    public static synchronized MusicDatabase getInstance(Context context){
        if(mInstance == null)
            mInstance = new MusicDatabase(context);
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createRecentPlayTable(db);
        createPlayListDetailTable(db);
        createPlaylistTable(db);
        createSongTable(db);
    }

    private void createRecentPlayTable(SQLiteDatabase db){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS "+RECENT_TABLE_NAME+"(");
        builder.append(RECENT_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,");
        builder.append(RECENT_COLUMN_SONG_ID+" TEXT REFERENCE "
                +SONG_TABLE_NAME+"("+SONG_COLUMN_ID+") ON DELETE CASCADE ON UPDATE CASCADE UNIQUE, " );
        builder.append(RECENT_COLUMN_TIME+" INTEGER");
        builder.append(")");
        db.execSQL(builder.toString());
    }

    private void createPlaylistTable(SQLiteDatabase db){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS "+PLAYLIST_TABLE_NAME+"(");
        builder.append(PLAYLIST_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,");
        builder.append(PLAYLIST_COLUMN_TITLE+ " TEXT");
        builder.append(")");
        db.execSQL(builder.toString());
    }

    private void createPlayListDetailTable(SQLiteDatabase db){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS "+PLAYLIST_DETAIL_TABLE_NAME+"(");
        builder.append(DETAIL_COLUMN_PLAYLIST_ID + " INTEGER REFERENCE "
                +PLAYLIST_TABLE_NAME+"("+PLAYLIST_COLUMN_ID+") ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,");
        builder.append(DETAIL_COLUMN_SONG_ID+" TEXT REFERENCES "
                +SONG_TABLE_NAME+"("+SONG_COLUMN_ID+") ON DELETE CASCADE ON UPDATE CASCADE NOT NULL");
        builder.append("PRIMARY KEY ("+DETAIL_COLUMN_PLAYLIST_ID+","+DETAIL_COLUMN_SONG_ID+")");
        builder.append(")");
        db.execSQL(builder.toString());
    }

    private void createSongTable(SQLiteDatabase db){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS "+SONG_TABLE_NAME+"(");
        builder.append(SONG_COLUMN_ID+" TEXT PRIMARY KEY, ");
        builder.append(SONG_COLUMN_TILE+ " TEXT, ");
        builder.append(SONG_COLUMN_ARTIST+ " TEXT, ");
        builder.append(SONG_COLUMN_ABLUM+ " TEXT, ");
        builder.append(SONG_COLUMN_COVER_PATH+ " TEXT, ");
        builder.append(SONG_COLUMN_PATH+ " TEXT, ");
        builder.append(SONG_COLUMN_DURATION+ " INTEGER, ");
        builder.append(SONG_COLUMN_YEAR+ " INTEGER");
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
        db.execSQL("DROP TABLE IF EXISTS " + SONG_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RECENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_DETAIL_TABLE_NAME);
        onCreate(db);
    }
}
