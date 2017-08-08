package app.simone.multiplayer.view.invites

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import app.simone.multiplayer.controller.DataManager
import app.simone.multiplayer.controller.FacebookManagerActor
import app.simone.multiplayer.controller.KeysHandler
import app.simone.multiplayer.model.OnlineMatch
import com.facebook.Profile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class InvitesFragment : Fragment() {

    var rootView : View? = null

    var listViewRequests : ListView? = null
    var requestsAdapter : PendingRequestsAdapter? = null
    var requestsUsers = ArrayList<OnlineMatch>()
    var myStrategy = StrategyImpl()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater?.inflate(app.simone.R.layout.activity_invites_fragment, container, false)
        initRequestsList()
        listenForChanges()

        val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR
                + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                app.simone.shared.application.App.getInstance().actorSystem)

        val btnInvites = rootView?.findViewById(app.simone.R.id.btn_invite) as FloatingActionButton
        btnInvites.setOnClickListener({
            actor.tell(app.simone.multiplayer.messages.FbSendGameRequestMsg(), akka.actor.ActorRef.noSender())
        })

        return rootView!!
    }

    fun initRequestsList() {
        listViewRequests = rootView?.findViewById(app.simone.R.id.list_invites) as android.widget.ListView
        if(app.simone.multiplayer.controller.FacebookManagerActor.Companion.isLoggedIn()) {
            requestsAdapter = PendingRequestsAdapter(requestsUsers, activity)
            listViewRequests?.adapter = requestsAdapter
            listViewRequests?.onItemClickListener = android.widget.AdapterView.OnItemClickListener { parent, view, position, id ->
                val model = requestsUsers[position]
                println("ARRAY: " + position)
            }
            updateRequests()
        }
    }

    fun updateRequests() {
        if (this.activity != null) {
        this.activity.runOnUiThread {
            if (requestsUsers.isNotEmpty() && FacebookManagerActor.Companion.isLoggedIn()) {
                requestsUsers = app.simone.multiplayer.controller.DataManager.Companion.instance.filterRequests(requestsUsers, com.facebook.Profile.getCurrentProfile().id)
                requestsAdapter?.clear()
                requestsAdapter?.addAll(requestsUsers)
            }
        }
    }
    }

    override fun onResume() {
        super.onResume()
        if(FacebookManagerActor.Companion.isLoggedIn() && Profile.getCurrentProfile() != null) {
            updateRequests()
        }
    }


    fun listenForChanges() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                requestsUsers.clear()
                requestsUsers.addAll(myStrategy.getRequestsUsers(dataSnapshot))
                //Updating GUI
                updateRequests()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //displayToast("Error while retrieving data from DB")
                // ...
            }
        }

        DataManager.Companion.instance.database.addValueEventListener(postListener)

    }


}
