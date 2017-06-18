package Model

import akka.actor.AbstractActor
import akka.actor.UntypedAbstractActor

/**
 * Created by gzano on 18/06/2017.
 */
class PlayerActor : UntypedAbstractActor() {
    override fun onReceive(p0: Any?) {
        println(p0.toString())
    }


}