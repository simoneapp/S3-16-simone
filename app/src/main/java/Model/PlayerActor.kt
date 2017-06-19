package Model

import akka.actor.UntypedActor
import android.util.Log


/**
 * Created by gzano on 18/06/2017.
 */
class PlayerActor : UntypedActor() {
    override fun onReceive(p0: Any?) {
        Log.d("MESSAGGIO RICEVUTO", p0.toString())
    }


}