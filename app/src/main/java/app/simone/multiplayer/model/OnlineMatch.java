package app.simone.multiplayer.model;


import java.util.List;

import app.simone.singleplayer.model.SColor;

/**
 * Created by Giacomo on 05/07/2017.
 */

public class OnlineMatch{

    private FacebookUser firstplayer;
    private FacebookUser secondplayer;
    private String key;
    private List<SColor> sequence;


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

    public List<SColor> getSequence() {
        return sequence;
    }

    public void setSequence(List<SColor> sequence) {
        this.sequence = sequence;
    }
}