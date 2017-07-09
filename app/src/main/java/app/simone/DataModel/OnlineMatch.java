package app.simone.DataModel;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Giacomo on 05/07/2017.
 */

public class PendingRequest extends RealmObject implements Serializable {

    private String id;
    private String name;
    private String scoreP1;
    @PrimaryKey
    private String idTo;
    private String nameTo;
    private String scoreP2;

    public PendingRequest() {

    }

    public PendingRequest(String id, String name, String scoreP1,String idTo, String nameTo, String scoreP2) {
        this.id=id;
        this.name=name;
        this.scoreP1=scoreP1;
        this.idTo=idTo;
        this.nameTo=nameTo;
        this.scoreP2=scoreP2;
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

    public String getScoreP1() {
        return scoreP1;
    }

    public void setScoreP1(String scoreP1) {
        this.scoreP1 = scoreP1;
    }

    public String getScoreP2() {
        return scoreP2;
    }

    public void setScoreP2(String scoreP2) {
        this.scoreP2 = scoreP2;
    }
}
