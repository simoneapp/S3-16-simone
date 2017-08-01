package app.simone.multiplayer.controller;

import android.util.Log;

import app.simone.multiplayer.model.FacebookUser;
import app.simone.multiplayer.model.OnlineMatch;
import io.realm.Realm;

/**
 * Created by Giacomo on 25/07/2017.
 */

public class ScoreHandler {

    private static final String SCORE_ERROR = "Error while retriving scores from DB";

    public static FacebookUser checkingExistingUser(String id){
        try{
            final FacebookUser fbUser;
            fbUser=Realm.getDefaultInstance().where(FacebookUser.class).equalTo("id", id).findFirst();
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (fbUser.getScore() == null) {
                        fbUser.setScore("--");
                    }
                }
            });
            return fbUser;
        }catch (ExceptionInInitializerError e){
            System.out.println(SCORE_ERROR);
        }
        return null;
    }

    public static String getUserScore(String matchId){

        try{
            OnlineMatch currentMatch = Realm.getDefaultInstance().where(OnlineMatch.class).equalTo("matchId",Integer.parseInt(matchId)).findFirst();
            FacebookUser secondP = currentMatch.getSecondPlayer();
            FacebookUser primoP = currentMatch.getFirstPlayer();
            String score = currentMatch.getFirstPlayer().getScore();
            Log.d("SCOREUSER2",score);
            return currentMatch.getSecondPlayer().getScore();
        }catch (Exception e){
            System.out.println(SCORE_ERROR);
        }

        return "";

    }

}
