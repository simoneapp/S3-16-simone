package app.simone.multiplayer.view.invites

import android.util.Log
import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.DataSnapshot

/**
 * Created by Giacomo on 08/08/2017.
 */
class StrategyImpl: Strategy {

    override fun getRequestsUsers(dataSnapshot: DataSnapshot): ArrayList<OnlineMatch> {
        val match = dataSnapshot.children
        val keysArray = ArrayList<String>();
        val requestsUsers = arrayListOf<OnlineMatch>()
        if (match != null) {
            for (data in match) {
                keysArray.add(data.key)
            }
            Log.d("TESTFRIEndsFilleR", " children count "+dataSnapshot.childrenCount)

            repeat(keysArray.size) { i ->
                    val onlineMatch = dataSnapshot.child(keysArray[i]).getValue(OnlineMatch::class.java)!!
                    onlineMatch.key = keysArray[i]
                    requestsUsers.add(onlineMatch)
                }
        }
        return requestsUsers
    }
}