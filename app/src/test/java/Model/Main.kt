package Model

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

/**
 * Created by gzano on 18/06/2017.
 */
class Main {
    companion object {
        fun main(args: () -> String): Unit {
            val system = ActorSystem.create("sapighei")
            val actor = system.actorOf(Props.create(PlayerActor::class.java), "sapi")
            actor.tell(GameMessage("ciao sapi!"), ActorRef.noSender())
        }
    }

}
