package app.simone.multiplayer.controller

import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.view.nearby.WaitingRoomActivity
import app.simone.shared.utils.Constants
import com.facebook.Profile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

/**
 * Controller containing useful functions for communicating with Firebase about the Nearby multiplayer.
 * @author Nicola Giancecchi
 */
class NearbyGameController {

    companion object {
        const val USERS_REF = "users"
        const val MATCHES_REF = "matches"
        const val TAKEN = "taken"
        const val TOKEN = "token"
        const val STARTED = "started"
    }

    val db = FirebaseDatabase.getInstance()

    /**
     * Updates Firebase Cloud Notifications token
     * @param token notification token, as provided by FCM
     * @param fbid Facebook ID of the user linked to the token
     */
    fun updateToken(token: String, fbid: String) {
        db.getReference(USERS_REF).child(fbid).child(TOKEN).setValue(token)
    }

    /**
     * Updates the online profile of the user.
     */
    fun updateUserData(){
        if(FacebookManagerActor.isLoggedIn()){
            val profile = Profile.getCurrentProfile()
            val userNode = db.getReference(USERS_REF).child(profile.id)
            userNode.child(FacebookUser.kNAME).setValue(profile.name)
            val profileUri = profile.getProfilePictureUri(Constants.FB_IMAGE_PICTURE_SIZE,Constants.FB_IMAGE_PICTURE_SIZE)
            userNode.child(FacebookUser.kPICTURE).setValue(profileUri.toString())
        }
    }

    /**
     * Creates a node on the Realtime Database to start a new match
     * @param userIDs a list of strings containing the Facebook IDs of the participating users.
     * @param playerID the Facebook ID of the current player.
     */
    fun createMatch(userIDs: List<String>, playerID: String): String {

        val matchName = UUID.randomUUID().toString()
        val users = HashMap<String,Any>()
        for (userID in userIDs) {
            val value = HashMap<String,Boolean>()
            value["taken"] = (userID == playerID)
            users[userID] = value
        }

        db.getReference(MATCHES_REF).child(matchName).child(USERS_REF).setValue(users)
        return matchName
    }

    /**
     * Observes current players and updates the list when data changes.
     * @param match the ID of the match to observe
     * @param activity the activity to be attached.
     */
    fun getAndListenForNewPlayers(match: String, activity: WaitingRoomActivity) {

        val ref = db.getReference(MATCHES_REF).child(match)
        val adapter = FirebaseListAdapterFactory.getWaitingRoomAdapter(ref, activity)
        activity.listView?.adapter = adapter

        var activityStarted = false

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) { }

            override fun onDataChange(p0: DataSnapshot?) {
                val started = p0?.child(STARTED)
                val users = p0?.child(USERS_REF)
                if(users?.children?.filter { it.child(TAKEN).value == false }?.count() == 0
                        && !activityStarted){
                    activity.startGameActivity(match)
                    activityStarted = true
                }
            }

        })
    }

    /**
     * A call to Database made when the user accepts an invite to a match
     * @param user Facebook ID of the user
     * @param match ID of the match
     */
    fun acceptInvite(user: String, match: String) {
        db.getReference(MATCHES_REF).child(match).child(USERS_REF).child(user).child(TAKEN).setValue(true)
    }
}