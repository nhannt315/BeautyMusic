package nhannt.musicplayer.objectmodel;

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

    public Artist() {
    }

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

    public Artist copy(){
        return new Artist(this.id,this.name,this.numberOfAlbum, this.numberOfSong);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        return prime + this.id + this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Artist) {
            Artist temp = (Artist) obj;
            if (this.hashCode() == temp.hashCode())
                return true;
        }
        return false;
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
