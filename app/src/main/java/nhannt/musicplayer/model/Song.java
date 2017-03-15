package nhannt.musicplayer.model;

import java.io.Serializable;

/**
 * Created by nhannt on 01/03/2017.
 */

public class Song implements Serializable {
    private String id;
    private String title;
    private String album;
    private String artist;
    private String coverPath;
    private int year;
    private long duration;
    private String songPath;

    public Song(String id, String title, String album, String artist, String coverPath, int year, long duration, String songPath) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.coverPath = coverPath;
        this.year = year;
        this.duration = duration;
        this.songPath = songPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
