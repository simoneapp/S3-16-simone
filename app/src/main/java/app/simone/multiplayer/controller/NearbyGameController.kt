package app.simone.multiplayer.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.simone.DistributedSimon.Activities.ColorSetUpActivity
import app.simone.R
import app.simone.multiplayer.view.nearby.WaitingRoomActivity
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

    fun createMatch(userIDs: List<String>, playerID: String): String {

        val matchName = UUID.randomUUID().toString()
        val users = HashMap<String,Boolean>()
        for (userID in userIDs) {
            users[userID] = (userID == playerID)
        }

        db.getReference(MATCHES_REF).child(matchName).child(USERS_REF).setValue(users)
        return matchName
    }

    fun getAndListenForNewPlayers(match: String, activity: WaitingRoomActivity, users: List<Map<String,String>>?) {

        val ref = db.getReference(MATCHES_REF).child(match).child(USERS_REF)

        val adapter = object : FirebaseListAdapter<Boolean>(activity, Boolean::class.java, R.layout.cell_friends, ref) {
            override fun populateView(view: View, s: Boolean, i: Int) {
                val itemRef = getRef(i)
                val key = itemRef.key
                val name = view.findViewById(R.id.text_name) as TextView
                val image = view.findViewById(R.id.img_profile) as ImageView

                val user = users?.filter { it["id"] == key }?.first()
                if(user != null) {
                    name.text = user["name"]
                    setImage(view, user["picture"])
                    setSelected(view, s, activity)
                }
            }
        }

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                checkMatch(p0, activity)
            }

        })


        activity.listView?.adapter = adapter
    }

    fun checkMatch(p0: DataSnapshot?, activity: WaitingRoomActivity) {
        if(p0?.children?.filter { it.value == false }?.count() == 0){
            val intent = Intent(activity, ColorSetUpActivity::class.java)
            activity.startActivity(intent)
        }
    }


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
        db.getReference(MATCHES_REF).child(match).child(USERS_REF).child(user).setValue(true)
    }
}