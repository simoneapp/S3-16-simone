package app.simone.DataModel;

import io.realm.RealmObject;

/**
 * Created by Giacomo on 05/07/2017.
 */

public class PendingRequest extends RealmObject{

    private String id;
    private String name;
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
}
