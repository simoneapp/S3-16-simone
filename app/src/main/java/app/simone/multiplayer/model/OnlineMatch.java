package app.simone.multiplayer.model;


import java.util.List;

import app.simone.singleplayer.model.SimonColor;
import app.simone.singleplayer.model.SimonColorImpl;

/**
 * Created by Giacomo on 05/07/2017.
 */

public class OnlineMatch{

    private FacebookUser firstplayer;
    private FacebookUser secondplayer;
    private String key;
    private List<SimonColorImpl> sequence;


    public OnlineMatch(){

    }


    public OnlineMatch(FacebookUser firstplayer, FacebookUser secondplayer) {
        this.firstplayer = firstplayer;
        this.secondplayer = secondplayer;
    }

    public FacebookUser getFirstplayer() {
        return firstplayer;
    }

    public void setFirstplayer(FacebookUser firstplayer) {
        this.firstplayer = firstplayer;
    }

    public FacebookUser getSecondplayer() {
        return secondplayer;
    }

    public void setSecondplayer(FacebookUser secondplayer) {
        this.secondplayer = secondplayer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<SimonColorImpl> getSequence() {
        return sequence;
    }

    public void setSequence(List<SimonColorImpl> sequence) {
        this.sequence = sequence;
    }
}