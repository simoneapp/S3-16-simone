package app.simone.multiplayer.view.invites

import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.DataSnapshot

/**
 * Created by Giacomo on 08/08/2017.
 */
interface Strategy {
    fun getRequestsUsers(dataSnapshot: DataSnapshot): ArrayList<OnlineMatch>
}