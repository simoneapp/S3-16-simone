package app.simone.multiplayer.view.invites

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import app.simone.multiplayer.controller.DataManager
import app.simone.multiplayer.controller.FacebookManagerActor
import app.simone.multiplayer.model.OnlineMatch
import com.facebook.Profile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

/**
 * This class represents the GUI where a user, after the Facebook login, can see the list of friends and send them a game request.
 *
 * @author Giacomo
 */

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

    /**
     * This method initializes the list view and it hooks a click listener on each cell.
     *
     */
    private fun initRequestsList() {
        listViewRequests = rootView?.findViewById(app.simone.R.id.list_invites) as android.widget.ListView
        if(app.simone.multiplayer.controller.FacebookManagerActor.Companion.isLoggedIn()) {
            requestsAdapter = PendingRequestsAdapter(requestsUsers, activity)
            listViewRequests?.adapter = requestsAdapter
            listViewRequests?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                requestsUsers[position]
                println("ARRAY: " + position)
            }
            updateRequests()
        }
    }

    /**
     * This method simply updates the match list of a given user.
     *
     */
    fun updateRequests() {
        if (this.activity != null) {
        this.activity.runOnUiThread {
            if (requestsUsers.isNotEmpty() && FacebookManagerActor.Companion.isLoggedIn()) {
                requestsUsers = DataManager.Companion.instance.filterRequests(requestsUsers, Profile.getCurrentProfile().id)
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

    /**
     * This method is listening for changes on the database. In case some values are updated, the list is updated consequently.
     * It has been used a Strategy Pattern: the list of matches is filtered by userID. All the algorithm is encapsulated  inside the class StrategyImpl.
     * DataSnapshot is an object containing all the matches store into the database.
     */
    fun listenForChanges() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                requestsUsers.clear()
                requestsUsers.addAll(myStrategy.getRequestsUsers(dataSnapshot))
                updateRequests()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Error while retrieving data from DB")
            }
        }

        DataManager.Companion.instance.database.addValueEventListener(postListener)

    }


}
