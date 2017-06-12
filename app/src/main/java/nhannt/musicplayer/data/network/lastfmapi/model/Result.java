package nhannt.musicplayer.data.network.lastfmapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by NhanNT on 05/21/2017.
 */

public class Result {
    @SerializedName("results")
    @Expose
    ResultDetail resultDetail;

    public ResultDetail getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(ResultDetail resultDetail) {
        this.resultDetail = resultDetail;
    }
}
