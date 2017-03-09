package nhannt.musicplayer.model;

import java.io.Serializable;

/**
 * Created by nhannt on 01/03/2017.
 */

public class Album implements Serializable {
    private int id;
    private String title;
    private String artist;
    private String coverPath;
    private int songCount;

    public Album(int id, String title, String artist, String coverPath, int songCount) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.coverPath = coverPath;
        this.songCount = songCount;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}
