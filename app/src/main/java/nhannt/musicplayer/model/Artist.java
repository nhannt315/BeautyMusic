package nhannt.musicplayer.model;

import java.io.Serializable;

/**
 * Created by nhannt on 01/03/2017.
 */

public class Artist implements Serializable {
    private int id;
    private String name;
    private String imageUrl;
    private int numberOfAlbum;
    private int numberOfSong;

    public Artist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist(int id, String name, int numberOfAlbum, int numberOfSong) {
        this.id = id;
        this.name = name;
        this.numberOfAlbum = numberOfAlbum;
        this.numberOfSong = numberOfSong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNumberOfAlbum() {
        return numberOfAlbum;
    }

    public void setNumberOfAlbum(int numberOfAlbum) {
        this.numberOfAlbum = numberOfAlbum;
    }

    public int getNumberOfSong() {
        return numberOfSong;
    }

    public void setNumberOfSong(int numberOfSong) {
        this.numberOfSong = numberOfSong;
    }
}
