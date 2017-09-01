package app.simone.multiplayer.view.invites

import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.DataSnapshot

/**
 * This class is used for downloading matches from the database.
 *
 * @author Giacomo
 * @return requestsUser this is the list of previous/on going matches of a specific user.
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