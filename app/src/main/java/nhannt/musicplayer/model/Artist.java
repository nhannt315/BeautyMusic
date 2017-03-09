package nhannt.musicplayer.model;

import java.io.Serializable;

/**
 * Created by nhannt on 01/03/2017.
 */

public class Artist implements Serializable {
    private int id;
    private String name;
    private String imageUrl;

    public Artist(int id, String name) {
        this.id = id;
        this.name = name;
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
}
