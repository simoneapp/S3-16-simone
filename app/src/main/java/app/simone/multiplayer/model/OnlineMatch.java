package app.simone.multiplayer.model;


import java.util.List;
import app.simone.singleplayer.model.SimonColorImpl;

/**
 * This class models an online match (Instant Multiplayer mode), which is made by two players, a match id (key) and a sequence of colors.
 *
 * @author Giacomo
 *
 */

public class OnlineMatch{

    private FacebookUser firstplayer;
    private FacebookUser secondplayer;
    private String key;
    private List<SimonColorImpl> sequence;

    public OnlineMatch(){

    }

    /**
     * The constructor is made by two players. The key is automatically generated while the list of colors is provided by CPU Actor (package singleplayer.controller.CPUActor)
     *
     * @param firstplayer first player of the match
     * @param secondplayer second plyaer of the match
     */
    public OnlineMatch(FacebookUser firstplayer, FacebookUser secondplayer) {
        this.firstplayer = firstplayer;
        this.secondplayer = secondplayer;
    }

    public FacebookUser getFirstplayer() {
        return firstplayer;
    }

    public FacebookUser getSecondplayer() {
        return secondplayer;
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
}