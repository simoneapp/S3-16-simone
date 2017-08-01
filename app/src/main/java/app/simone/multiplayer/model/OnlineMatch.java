package app.simone.multiplayer.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import app.simone.singleplayer.model.SColor;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import scala.Int;


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

    private String kindOfMsg="";

    @PrimaryKey
    private int matchId;

    public static final String kFIRST = "first";
    public static final String kSECOND = "second";
    public static final String kMSG = "kindOfMsg";


    public OnlineMatch(){

    }

    public OnlineMatch(FacebookUser firstPlayer, FacebookUser secondPlayer,int matchId) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.matchId=matchId;
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

    public String getKindOfMsg() {
        return kindOfMsg;
    }

    public void setKindOfMsg(String kindOfMsg) {
        this.kindOfMsg = kindOfMsg;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setSecondPlayer(FacebookUser secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public static OnlineMatch with(JsonObject obj) {

        FacebookUser first = new FacebookUser(obj.get(kFIRST).getAsJsonObject());
        FacebookUser second = new FacebookUser(obj.get(kSECOND).getAsJsonObject());
        OnlineMatch pr = new OnlineMatch(first,second);
        pr.setKindOfMsg(obj.get(kMSG).getAsString());
        return pr;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add(kFIRST, firstPlayer.toJson());
        obj.add(kSECOND, secondPlayer.toJson());
        obj.addProperty(kMSG,this.kindOfMsg);
        return obj;
    }

}