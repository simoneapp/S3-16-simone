package app.simone.DataModel;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by gzano on 22/06/2017.
 */

public class Match extends RealmObject {
    private Date gameDate;
    private int score;



    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
