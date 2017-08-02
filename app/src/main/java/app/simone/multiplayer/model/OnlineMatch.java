package app.simone.multiplayer.model;



/**
 * Created by Giacomo on 05/07/2017.
 */

public class OnlineMatch{

    private FacebookUser firstplayer;
    private FacebookUser secondplayer;


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

}