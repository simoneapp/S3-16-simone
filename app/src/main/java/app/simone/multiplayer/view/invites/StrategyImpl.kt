package app.simone.multiplayer.view.invites

import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.DataSnapshot

/**
 * Created by Giacomo on 08/08/2017.
 */
class StrategyImpl: Strategy {

    override fun getRequestsUsers(dataSnapshot: DataSnapshot): ArrayList<OnlineMatch> {
        val match = dataSnapshot.children
        val requestsUsers = arrayListOf<OnlineMatch>()
        if (match != null) {
            for (data in match) {
                //val ref = dataSnapshot.child(data.key)
                val onlineMatch = data.getValue(OnlineMatch::class.java)!!
                onlineMatch.key = data.key
                requestsUsers.add(onlineMatch)
            }
        }
        return requestsUsers
    }
}