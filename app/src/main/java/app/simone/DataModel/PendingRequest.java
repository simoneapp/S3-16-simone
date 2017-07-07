package app.simone.DataModel;

import com.google.gson.JsonObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Giacomo on 05/07/2017.
 */

public class PendingRequest extends RealmObject{

    private String id;
    private String name;
    @PrimaryKey
    private String idTo;
    private String nameTo;

    public PendingRequest() {

    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getIdTo() {
        return idTo;
    }

    public String getNameTo() {
        return nameTo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public void setNameTo(String nameTo) {
        this.nameTo = nameTo;
    }


    public static PendingRequest fromJson(JsonObject obj) {

        PendingRequest pr = new PendingRequest();
        pr.id = obj.get("from").getAsString();
        pr.name = obj.get("fromName").getAsString();
        pr.idTo = obj.get("to").getAsString();
        pr.nameTo = obj.get("toName").getAsString();
        return pr;
    }


}
