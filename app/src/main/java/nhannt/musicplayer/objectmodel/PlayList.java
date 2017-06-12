package nhannt.musicplayer.objectmodel;

import java.util.ArrayList;

/**
 * Created by nhannt on 24/03/2017.
 */

public class PlayList {
    private int id;
    private String title;
    private int songNums;
    private ArrayList<Song> lstSong;
    private boolean isAutoPlaylist = false;

    public PlayList() {
    }

    public PlayList(int id, String title, int songNums, ArrayList<Song> lstSong, boolean isAutoPlaylist) {
        this.id = id;
        this.title = title;
        this.songNums = songNums;
        this.lstSong = lstSong;
        this.isAutoPlaylist = isAutoPlaylist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSongNums() {
        return songNums;
    }

    public void setSongNums(int songNums) {
        this.songNums = songNums;
    }

    public ArrayList<Song> getLstSong() {
        return lstSong;
    }

    public void setLstSong(ArrayList<Song> lstSong) {
        this.lstSong = lstSong;
    }

    public boolean isAutoPlaylist() {
        return isAutoPlaylist;
    }

    public void setAutoPlaylist(boolean autoPlaylist) {
        isAutoPlaylist = autoPlaylist;
    }
}
