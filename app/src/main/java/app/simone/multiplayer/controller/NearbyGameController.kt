package app.simone.multiplayer.controller

import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * Created by nicola on 01/08/2017.
 */
class NearbyGameController {

    companion object {
        const val USERS_REF = "users"
        const val MATCHES_REF = "matches"
    }

    val db = FirebaseDatabase.getInstance()

    fun updateToken(token: String, fbid: String) {
        db.getReference(USERS_REF).child(fbid).child("token").setValue(token)
    }

    fun createMatch(userIDs: List<String>): String {

        val matchName = UUID.randomUUID().toString()
        val users = HashMap<String,Boolean>()
        for (userID in userIDs) { users[userID] = false }

        db.getReference(MATCHES_REF).child(matchName).child(USERS_REF).setValue(users)
        return matchName
    }

    fun acceptInvite(user: String, match: String) {
        db.getReference(MATCHES_REF).child(match).child(USERS_REF).child(user).setValue(true)
    }
}