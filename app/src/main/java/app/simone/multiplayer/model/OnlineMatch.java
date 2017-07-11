package app.simone.multiplayer.model;

import com.google.gson.JsonObject;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Giacomo on 05/07/2017.
 */

public class OnlineMatch extends RealmObject implements Serializable {

    private FacebookUser firstPlayer;
    private FacebookUser secondPlayer;

    public static final String kFIRST = "first";
    public static final String kSECOND = "second";

    public OnlineMatch() {
    }

    public OnlineMatch(FacebookUser firstPlayer, FacebookUser secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public FacebookUser getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(FacebookUser firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public FacebookUser getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(FacebookUser secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public static OnlineMatch with(JsonObject obj) {

        OnlineMatch pr = new OnlineMatch();
        pr.firstPlayer = new FacebookUser(obj.get(kFIRST).getAsJsonObject());
        pr.secondPlayer = new FacebookUser(obj.get(kSECOND).getAsJsonObject());

        return pr;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add(kFIRST, firstPlayer.toJson());
        obj.add(kSECOND, secondPlayer.toJson());
        return obj;
    }
}