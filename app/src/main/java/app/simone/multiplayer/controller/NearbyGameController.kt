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
 * Created by nicola on 01/08/2017.
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

    fun updateToken(token: String, fbid: String) {
        db.getReference(USERS_REF).child(fbid).child(TOKEN).setValue(token)
    }

    fun updateUserData(){
        if(FacebookManagerActor.isLoggedIn()){
            val profile = Profile.getCurrentProfile()
            val userNode = db.getReference(USERS_REF).child(profile.id)
            userNode.child(FacebookUser.kNAME).setValue(profile.name)
            val profileUri = profile.getProfilePictureUri(Constants.FB_IMAGE_PICTURE_SIZE,Constants.FB_IMAGE_PICTURE_SIZE)
            userNode.child(FacebookUser.kPICTURE).setValue(profileUri.toString())
        }
    }

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

    fun acceptInvite(user: String, match: String) {
        db.getReference(MATCHES_REF).child(match).child(USERS_REF).child(user).child(TAKEN).setValue(true)
    }
}