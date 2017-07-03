package PubNub;

/**
 * Created by Giacomo on 01/07/2017.
 */

public class OnlinePlayer {

    private String id;
    private String name;
    private String surname;

    public OnlinePlayer(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
