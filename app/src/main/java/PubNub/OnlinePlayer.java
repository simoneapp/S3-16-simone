package PubNub;

import java.io.Serializable;

/**
 * Created by Giacomo on 01/07/2017.
 */

public class OnlinePlayer implements Serializable {

    private String id;
    private String name;
    private String surname;
    private String score;

    public OnlinePlayer(){

    }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}