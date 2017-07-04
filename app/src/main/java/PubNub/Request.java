package PubNub;

/**
 * Created by Giacomo on 04/07/2017.
 */

public class Request {

    private OnlinePlayer player;
    private OnlinePlayer toPlayer;


    public Request(OnlinePlayer player, OnlinePlayer toPlayer) {
        this.player = player;
        this.toPlayer = toPlayer;
    }

    public OnlinePlayer getPlayer() {
        return player;
    }

    public OnlinePlayer getToPlayer() {
        return toPlayer;
    }
}
