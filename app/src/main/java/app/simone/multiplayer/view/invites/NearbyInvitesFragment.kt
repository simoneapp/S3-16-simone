package app.simone.multiplayer.view.invites

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import app.simone.R
import app.simone.multiplayer.controller.FacebookManagerActor
import app.simone.multiplayer.controller.NearbyGameController
import app.simone.multiplayer.view.nearby.WaitingRoomActivity
import app.simone.shared.utils.Utilities
import com.facebook.Profile
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.database.FirebaseDatabase



class NearbyInvitesFragment : Fragment() {

    var rootView : View? = null
    var listView : ListView? = null
    val db = FirebaseDatabase.getInstance()
    var adapter : FirebaseListAdapter<Long>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        rootView = inflater?.inflate(app.simone.R.layout.activity_nearby_invites_fragment, container, false)

        if(FacebookManagerActor.isLoggedIn()) {
            buildAdapter()
        }

        return rootView!!
    }

    override fun onResume() {
        super.onResume()

        if(FacebookManagerActor.isLoggedIn()) {
            buildAdapter()
        }
    }

    fun buildAdapter() {

        listView = rootView?.findViewById(R.id.list_nearby_invites) as ListView

        val ref = db.getReference(NearbyGameController.USERS_REF).child(Profile.getCurrentProfile().id).child(NearbyGameController.MATCHES_REF)

        adapter = object : FirebaseListAdapter<Long>(activity, Long::class.java, android.R.layout.simple_list_item_2, ref) {
            override fun populateView(v: View?, model: Long?, position: Int) {
                val itemRef = getRef(position)
                val key = itemRef.key

                val tv = v?.findViewById(android.R.id.text1) as TextView
                val tv2 = v?.findViewById(android.R.id.text2) as TextView
                if(model != null){
                    tv.text = "Match of " + Utilities.stringFromTimestamp(model)
                    tv2.text = "Key: " + key
                }
            }
        }
        listView?.adapter = adapter
        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val itemRef = adapter?.getRef(position)
            if(itemRef != null) {
                val intent = Intent(activity, WaitingRoomActivity::class.java)
                intent.putExtra("matchID", itemRef.key)
                activity.startActivity(intent)
            }
        }
    }
}
