package app.simone.multiplayer.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.simone.R
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.model.MatchUserInfo
import app.simone.multiplayer.view.nearby.ColorSetUpActivity
import app.simone.multiplayer.view.nearby.WaitingRoomActivity
import com.facebook.Profile
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
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

    fun updateUserData(){
        val profile = Profile.getCurrentProfile()
        val userNode = db.getReference(USERS_REF).child(profile.id)
        userNode.child(FacebookUser.kNAME).setValue(profile.name)
        userNode.child(FacebookUser.kPICTURE).setValue(profile.getProfilePictureUri(250,250).toString())
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

        val adapter = object : FirebaseListAdapter<MatchUserInfo>(activity, MatchUserInfo::class.java,
                R.layout.cell_friends, ref.child(USERS_REF)) {
            override fun populateView(v: View?, model: MatchUserInfo?, position: Int) {
                val itemRef = getRef(position)
                val key = itemRef.key

                val userRef = db.getReference(USERS_REF).child(key)
                val name = v?.findViewById(R.id.text_name) as TextView

                userRef.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) { }

                    override fun onDataChange(p0: DataSnapshot?) {
                        name.text = p0?.child(FacebookUser.kNAME)?.value.toString()
                        setImage(v, p0?.child(FacebookUser.kPICTURE)?.value.toString())
                        if(model != null) {
                            setSelected(v, model.taken, activity)
                        }
                    }
                })
            }
        }

        var activityStarted = false

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) { }

            override fun onDataChange(p0: DataSnapshot?) {
                val started = p0?.child("started")
                val users = p0?.child(USERS_REF)
                if(users?.children?.filter { it.child("taken").value == false }?.count() == 0
                        && !activityStarted){
                    val intent = Intent(activity, ColorSetUpActivity::class.java)
                    intent.putExtra("match", match)
                    activity.startActivity(intent)
                    activityStarted = true
                }
            }

        })


        activity.listView?.adapter = adapter
    }

    /*fun checkMatch(p0: DataSnapshot?, activity: WaitingRoomActivity, match: String) {

    }*/


    fun setSelected(convertView: View?, isSelected: Boolean, activity: Activity) {
        if(isSelected){
            convertView?.background = activity?.resources?.getDrawable(R.color.myGreen)
        } else {
            convertView?.background = activity?.resources?.getDrawable(R.color.myWhite)
        }
    }

    fun setImage(convertView: View?, url: String?){

        val imgProfile = convertView!!.findViewById(R.id.img_profile) as ImageView

        if(url != null) {
            imgProfile.setImageDrawable(null)
            ImageLoader.getInstance().cancelDisplayTask(imgProfile)
            ImageLoader.getInstance().loadImage(url, object : SimpleImageLoadingListener() {
                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                    imgProfile.setImageBitmap(loadedImage)
                }
            })
        }
    }

    fun acceptInvite(user: String, match: String) {
        db.getReference(MATCHES_REF).child(match).child(USERS_REF).child(user).child("taken").setValue(true)
    }
}