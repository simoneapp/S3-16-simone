package app.simone.multiplayer.view.invites

import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.DataSnapshot

/**
 * This interface is used to implement the strategy algorithm for retrieving matches from the database
 *
 * @author Giacomo.
 */
interface Strategy {
    fun getRequestsUsers(dataSnapshot: DataSnapshot): ArrayList<OnlineMatch>
}