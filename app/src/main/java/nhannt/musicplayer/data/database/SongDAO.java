package nhannt.musicplayer.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import nhannt.musicplayer.model.PlayList;
import nhannt.musicplayer.model.Song;

/**
 * Created by nhannt on 24/03/2017.
 */

public class SongDAO {
    private static SongDAO mInstance;
    private Context mContext;
    private SQLiteDatabase db;

    private SongDAO(Context context){
        this.mContext = context;
    }

    /**
     * Get instance of SongDAO
     * @param context
     * @return
     */
    public static synchronized SongDAO getInstance(Context context){
        if(mInstance == null){
            mInstance = new SongDAO(context);
        }
        return mInstance;
    }

    /**
     * Open song database
     * @return
     */
    private SQLiteDatabase getDatabase(){
        return getDatabase(true);
    }

    private SQLiteDatabase getDatabase(boolean isWrite) {
        if (isWrite)
            return MusicDatabase.getInstance(mContext).getWritableDatabase();
        else
            return MusicDatabase.getInstance(mContext).getReadableDatabase();
    }

    /**
     * Get list song recent play from database
     * @return list song
     */
    public ArrayList<Song> getListSongRecentPlay(){
        ArrayList<Song> lstSong = new ArrayList<>();
        db = getDatabase();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT "+MusicDatabase.SONG_TABLE_NAME+".* FROM "+MusicDatabase.SONG_TABLE_NAME);

        // TODO: 24/03/2017
        return lstSong;
    }

    public ArrayList<PlayList> getAllPlayList(){
        ArrayList<PlayList> lstPlayList;
        lstPlayList = new ArrayList<>();
        
        // TODO: 24/03/2017
        
        return lstPlayList;
    }

    public ArrayList<Song> getSongOfPlayList(int playListId){
        ArrayList<Song> lstSong;
        lstSong = new ArrayList<>();

        // TODO: 24/03/2017

        return lstSong;
    }
}
