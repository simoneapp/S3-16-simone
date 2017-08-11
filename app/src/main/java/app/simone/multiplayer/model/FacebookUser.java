package app.simone.multiplayer.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicola on 11/07/2017.
 */

public class FacebookUser {

    public static final String kNAME = "name";
    public static final String kPICTURE = "picture";
    public static final String kID = "id";
    public static final String kSCORE = "score";
    public static final String kDATA = "data";

    private String id;
    private String name;
    private FacebookPicture picture;

    private String score = "";

    public FacebookUser() {

    }

    public FacebookUser(JsonElement json) {

        JsonObject obj = json.getAsJsonObject();

        this.name = obj.get(kNAME).getAsString();
        if(obj.get(kPICTURE)!=null){
            this.picture = new FacebookPicture(obj.get(kPICTURE).getAsJsonObject().get(kDATA).getAsJsonObject());
        }
        this.id = obj.get(kID).getAsString();

        if(obj.get(kSCORE)!=null) {
            this.score = obj.get(kSCORE).getAsString();
        }
    }

    public FacebookUser(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<FacebookUser> listFromJson(JsonArray json) {

        List<FacebookUser> objectFriends = new ArrayList<FacebookUser>();

        for(JsonElement item : json) {
            objectFriends.add(new FacebookUser(item));
        }

        return objectFriends;
    }


    public Map<String,String> toDictionary() {
        Map<String,String> dict = new HashMap<>();
        dict.put(FacebookUser.kID, id);
        dict.put(FacebookUser.kNAME, name);
        dict.put(FacebookUser.kSCORE, score);
        dict.put(FacebookUser.kPICTURE, picture.getUrl());
        return dict;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FacebookPicture getPicture() {
        return picture;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(FacebookPicture picture) {
        this.picture = picture;
    }

}