package app.simone.multiplayer.controller

import android.app.Activity
import android.view.View
import android.widget.TextView
import app.simone.R
import app.simone.multiplayer.view.nearby.WaitingRoomActivity
import com.firebase.ui.database.FirebaseListAdapter
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

    fun getAndListenForNewPlayers(match: String, activity: WaitingRoomActivity, users: List<Map<String,String>>?) {

        val ref = db.getReference(MATCHES_REF).child(match).child(USERS_REF)

        val adapter = object : FirebaseListAdapter<Boolean>(activity, Boolean::class.java, R.layout.cell_friends, ref) {
            override fun populateView(view: View, s: Boolean, i: Int) {
                val itemRef = getRef(i)
                val itemKey = itemRef.key
                val text = view.findViewById(R.id.text_name) as TextView

                val user = users?.filter { it["id"] == itemKey }?.first()
                if(user != null) {
                    text.text = user["name"]
                    setSelected(view, s, activity)
                }
            }
        }
        activity.listView?.adapter = adapter
    }


    fun setSelected(convertView: View?, isSelected: Boolean, activity: Activity) {
        if(isSelected){
            convertView?.background = activity?.resources?.getDrawable(R.color.myGreen)
        } else {
            convertView?.background = activity?.resources?.getDrawable(R.color.myWhite)
        }
    }

    fun acceptInvite(user: String, match: String) {
        db.getReference(MATCHES_REF).child(match).child(USERS_REF).child(user).setValue(true)
    }
}