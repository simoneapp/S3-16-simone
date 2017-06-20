package messages;

import actors.PlayerActorJava;
import actors.ViewActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * IMessage interface.
 *
 * @author Michele Sapignoli
 */
public interface IMessage {
    MessageType getType();
}

