package app.simone.multiplayer.model;

import com.google.gson.JsonObject;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by Giacomo on 05/07/2017.
 */

public class OnlineMatch extends RealmObject {

    private FacebookUser firstPlayer;
    private FacebookUser secondPlayer;

    public RealmList<FacebookUser> getUsersList() {
        return usersList;
    }

    public void setUsersList(RealmList<FacebookUser> usersList) {
        this.usersList = usersList;
    }

    private RealmList<FacebookUser> usersList=new RealmList<>();

    @PrimaryKey
    private String idSecondUser;

    public static final String kFIRST = "first";
    public static final String kSECOND = "second";

    public OnlineMatch() {
    }

    public OnlineMatch(FacebookUser firstPlayer, FacebookUser secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.idSecondUser=secondPlayer.getId();

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

        FacebookUser first = new FacebookUser(obj.get(kFIRST).getAsJsonObject());
        FacebookUser second = new FacebookUser(obj.get(kSECOND).getAsJsonObject());
        OnlineMatch pr = new OnlineMatch(first,second);
        return pr;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add(kFIRST, firstPlayer.toJson());
        obj.add(kSECOND, secondPlayer.toJson());
        return obj;
    }

}