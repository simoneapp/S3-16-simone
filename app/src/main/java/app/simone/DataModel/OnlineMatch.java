package app.simone.DataModel;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Giacomo on 05/07/2017.
 */

public class OnlineMatch extends RealmObject implements Serializable {

    private String idP1;
    private String nameP1;
    private String scoreP1;
    @PrimaryKey
    private String idP2;
    private String nameP2;
    private String scoreP2;

    public OnlineMatch() {

    }

    public OnlineMatch(String idP1, String nameP1, String scoreP1, String idP2, String nameP2, String scoreP2) {
        this.idP1 = idP1;
        this.nameP1 = nameP1;
        this.scoreP1=scoreP1;
        this.idP2 = idP2;
        this.nameP2 = nameP2;
        this.scoreP2=scoreP2;
    }

    public String getIdP1() {
        return idP1;
    }


    public String getNameP1() {
        return nameP1;
    }


    public String getIdP2() {
        return idP2;
    }

    public String getNameP2() {
        return nameP2;
    }

    public void setIdP1(String idP1) {
        this.idP1 = idP1;
    }

    public void setNameP1(String nameP1) {
        this.nameP1 = nameP1;
    }

    public void setIdP2(String idP2) {
        this.idP2 = idP2;
    }

    public void setNameP2(String nameP2) {
        this.nameP2 = nameP2;
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