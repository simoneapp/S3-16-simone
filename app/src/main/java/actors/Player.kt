package actors

import akka.actor.*
import java.util.concurrent.ThreadLocalRandom

import akka.util.Timeout
import messages.IMessage
import messages.MessageType
import messages.StartGameVsCPUMsg

/**
 * Created by Michele Sapignoli on 19/06/2017.
 */

class Player : UntypedActor() {

    @Throws(Exception::class)
    override fun onReceive(message: Any) {
        when ((message as IMessage).type) {
            MessageType.STARTGAMEVSCPU -> {
                val viewActor = context.actorSelection("view-actor").resolveOne(Timeout.intToTimeout(50)).value()
                this.generateSequence((message as StartGameVsCPUMsg).getnColors())
            }
        }

    }

    private fun generateSequence(nColors: Int): IntArray? {
        return IntArray(200){ ThreadLocalRandom.current().nextInt(0, nColors)}
    }

}
