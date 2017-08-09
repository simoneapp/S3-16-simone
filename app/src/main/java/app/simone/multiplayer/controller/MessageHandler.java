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



    public OnlineMatch createMatch(Request rec) {
        return new OnlineMatch(rec.getSender(), rec.getRecipient());
    }


}
