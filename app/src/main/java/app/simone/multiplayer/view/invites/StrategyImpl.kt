package app.simone.multiplayer.view.invites

import app.simone.multiplayer.controller.KeysHandler
import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.DataSnapshot

/**
 * Created by Giacomo on 08/08/2017.
 */
class StrategyImpl: Strategy {

    override fun getRequestsUsers(dataSnapshot: DataSnapshot): ArrayList<OnlineMatch> {
        val match = dataSnapshot.children
        val keysArray = KeysHandler()
        val requestsUsers = arrayListOf<OnlineMatch>()
        if (match != null) {
            for (data in match) {
                keysArray.addToList(data.key)
            }
                repeat(keysArray.list.size) { i ->
                    val onlineMatch = dataSnapshot.child(keysArray.getElement(i)).getValue(OnlineMatch::class.java)!!
                    onlineMatch.key = keysArray.list[i]
                    requestsUsers.add(onlineMatch)
                }
        }
        return requestsUsers
    }
}