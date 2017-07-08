package PubNub;

import app.simone.users.model.FacebookUser;

/**
 * Created by Giacomo on 04/07/2017.
 */

public class Request {

    private FacebookUser sender;
    private FacebookUser recipient;


    public Request(FacebookUser sender, FacebookUser recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    public FacebookUser getSender() {
        return sender;
    }

    public FacebookUser getRecipient() {
        return recipient;
    }

}
