package nhannt.musicplayer.model;

import java.util.ArrayList;

/**
 * Created by nhannt on 24/03/2017.
 */

public class PlayList {
    private int id;
    private String title;
    private ArrayList<Song> lstSong;

    public PlayList(int id, String title, ArrayList<Song> lstSong) {
        this.id = id;
        this.title = title;
        this.lstSong = lstSong;
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

    public ArrayList<Song> getLstSong() {
        return lstSong;
    }

    public void setLstSong(ArrayList<Song> lstSong) {
        this.lstSong = lstSong;
    }
}
