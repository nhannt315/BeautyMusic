
package nhannt.musicplayer.data.network.lastfmapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("resultDetail")
    @Expose
    private ResultDetail resultDetail;

    public ResultDetail getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(ResultDetail resultDetail) {
        this.resultDetail = resultDetail;
    }

}
