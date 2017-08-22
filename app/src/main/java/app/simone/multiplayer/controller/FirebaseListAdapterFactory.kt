package app.simone.multiplayer.controller

import android.view.View
import android.widget.TextView
import app.simone.R
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.model.MatchUserInfo
import app.simone.multiplayer.view.FriendsCellFiller
import app.simone.multiplayer.view.nearby.WaitingRoomActivity
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.database.*


/**
 * Created by nicola on 21/08/2017.
 */
class FirebaseListAdapterFactory {

    companion object {

        val db = FirebaseDatabase.getInstance()

        fun getWaitingRoomAdapter(ref: DatabaseReference, activity: WaitingRoomActivity) : FirebaseListAdapter<MatchUserInfo> {

            return object : FirebaseListAdapter<MatchUserInfo>(activity, MatchUserInfo::class.java,
                    R.layout.cell_friends, ref.child(NearbyGameController.USERS_REF)) {
                override fun populateView(v: View?, model: MatchUserInfo?, position: Int) {
                    val itemRef = getRef(position)
                    val key = itemRef.key

                    val userRef = db.getReference(NearbyGameController.USERS_REF).child(key)
                    val name = v?.findViewById(R.id.text_name) as TextView

                    userRef.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(p0: DatabaseError?) { }

                        override fun onDataChange(p0: DataSnapshot?) {
                            FriendsCellFiller.setName(v, p0?.child(FacebookUser.kNAME)?.value.toString())
                            FriendsCellFiller.setImage(v, p0?.child(FacebookUser.kPICTURE)?.value.toString(), activity)
                            if(model != null) {
                                FriendsCellFiller.setSelected(v, model.taken, activity)
                            }
                        }
                    })
                }
            }
        }
    }

}