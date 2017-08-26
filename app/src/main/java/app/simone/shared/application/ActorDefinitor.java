package app.simone.shared.application;

/**
 * Created by nicola on 25/08/2017.
 */

public class ActorDefinitor {

    private Class actorClass;
    private String actorName;

    public ActorDefinitor(Class actorClass, String actorName) {
        this.actorClass = actorClass;
        this.actorName = actorName;
    }

    public Class getActorClass() {
        return actorClass;
    }

    public java.lang.String getActorName() {
        return actorName;
    }
}
