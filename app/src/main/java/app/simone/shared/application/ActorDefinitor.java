package app.simone.shared.application;

/**
 * Created by nicola on 25/08/2017.
 */

public class ActorDefinitor {

    private String actorClass;
    private String actorName;

    public ActorDefinitor(String actorClass, String actorName) {
        this.actorClass = actorClass;
        this.actorName = actorName;
    }

    public String getActorClass() {
        return actorClass;
    }

    public String getActorName() {
        return actorName;
    }
}
