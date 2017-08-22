package app.simone.multiplayer.model;
import com.google.gson.JsonObject;

/**
 * Created by nicola on 11/07/2017.
 */

public class FacebookPicture {

    private String url;
    private boolean isSilhouette;

    public FacebookPicture() {

    }

    public FacebookPicture(JsonObject json) {
        this.url = json.get("url").getAsString();
        this.isSilhouette = json.get("is_silhouette").getAsBoolean();
    }

    public String getUrl() {
        return url;
    }

    public boolean isSilhouette() {
        return isSilhouette;
    }

}