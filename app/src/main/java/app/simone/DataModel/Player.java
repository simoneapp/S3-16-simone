package app.simone.DataModel;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gzano on 19/06/2017.
 */

public  class Player extends RealmObject {

    @PrimaryKey
    private String name = "";
    private RealmList<Match> matches = null;

    public RealmList<Match> getMatches() {
        return matches;
    }

    public void setMatches(RealmList<Match> matches) {
        this.matches = matches;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
