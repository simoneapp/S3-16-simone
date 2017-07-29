package app.simone.multiplayer.controller;

import android.util.Log;

import org.json.JSONException;

import app.simone.multiplayer.model.OnlineMatch;
import app.simone.multiplayer.model.Request;

/**
 * Created by Giacomo on 29/07/2017.
 */

public class MessageHandler {

    private PubnubController pnController;

    public MessageHandler(PubnubController pnController) {
        this.pnController=pnController;
        pnController.subscribeToChannel();
    }


    public void publishMessage(OnlineMatch match){
        try {
            pnController.publishToChannel(match);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("GameActivity", "Error while publishing the message on the channel");
        }
    }

    public void setMessageFields(OnlineMatch match){
        if(ScoreHandler.getUserScore(""+match.getMatchId())!=""){
            match.getSecondPlayer().setScore(ScoreHandler.getUserScore(""+match.getMatchId()));
            match.setKindOfMsg("update");
        }
    }

    public OnlineMatch createMatch(Request rec) {
        return new OnlineMatch(rec.getSender(), rec.getRecipient());
    }


}
