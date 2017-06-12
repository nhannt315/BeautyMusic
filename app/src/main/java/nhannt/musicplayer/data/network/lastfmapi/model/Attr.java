
package nhannt.musicplayer.data.network.lastfmapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attr {

    @SerializedName("for")
    @Expose
    private String _for;

    public String getFor() {
        return _for;
    }

    public void setFor(String _for) {
        this._for = _for;
    }

}
