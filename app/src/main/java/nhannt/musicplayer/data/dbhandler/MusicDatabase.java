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
    public static final String DETAIL_COLUMN_PLAY_ID = "playlist_detail_playlist_id";
    public static final String DETAIL_COLUMN_SONG_ID = "playlist_detail_song_id";




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

    }

    private void createRecentPlayTable(){

    }

    private void createPlaylistTable(){

    }

    private void createPlayListDetailTable(){

    }

    private void createSongTable(){

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
