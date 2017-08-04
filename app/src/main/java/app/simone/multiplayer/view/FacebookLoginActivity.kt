package app.simone.multiplayer.view

import android.content.Intent
import android.widget.AdapterView
import app.simone.multiplayer.controller.DataManager
import app.simone.multiplayer.controller.FacebookManagerActor
import app.simone.multiplayer.controller.KeysHandler
import app.simone.multiplayer.messages.*
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.model.OnlineMatch
import app.simone.shared.application.App
import app.simone.shared.utils.Utilities
import app.simone.singleplayer.view.GameActivity
import com.facebook.Profile
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList


class FacebookLoginActivity : android.support.v7.app.AppCompatActivity() {

    var listView: android.widget.ListView? = null
    var btnPlay: android.widget.Button? = null

    var friends = ArrayList<app.simone.multiplayer.model.FacebookUser>()
    var adapter: FacebookFriendsAdapter? = null

    var listViewRequests: android.widget.ListView? = null
    var requestsUsers:MutableList<OnlineMatch> = arrayListOf()
    var requestsAdapter: PendingRequestsAdapter? = null

    var currentUser: FacebookUser? = null
    var selectedUser: FacebookUser? = null
    var realm: io.realm.Realm? = null

    companion object {
        val PENDING_REQUESTS: String = "Pending requests:"
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(app.simone.R.layout.activity_facebook_login)


        initMainList()
        listenForChanges()
        initRequestsList()
        val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR
                + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                App.getInstance().actorSystem)

        actor.tell(FbViewSetupMsg(this), akka.actor.ActorRef.noSender())

        val btnInvites = this.findViewById(app.simone.R.id.btn_invite) as android.widget.Button
        btnInvites.setOnClickListener({
            actor.tell(FbSendGameRequestMsg(), akka.actor.ActorRef.noSender())
        })

        btnPlay = this.findViewById(app.simone.R.id.playButton) as android.widget.Button
        btnPlay?.setOnClickListener({
            if (selectedUser != null) {
                val activityIntent = Intent(baseContext, GameActivity::class.java)
                activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                println("ME: " + com.facebook.Profile.getCurrentProfile().firstName.toString() + " " + com.facebook.Profile.getCurrentProfile().lastName.toString())
                setUser()

                val onlineMatch = OnlineMatch(currentUser, selectedUser)
                activityIntent.putExtra("multiplayerMode", "multiplayerMode")
                activityIntent.putExtra("key", DataManager.instance.createMatch(onlineMatch))
                activityIntent.putExtra("whichPlayer","firstplayer")
                baseContext.startActivity(activityIntent)
            }
        })
    }


    fun listenForChanges() {

            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    requestsUsers?.clear()
                    val match = dataSnapshot.children
                    val keysArray = KeysHandler()
                    if (match != null) {

                        for (data in match) {
                            keysArray.addToList(data.key)
                        }
                        if (keysArray.list.size > 0) {
                            repeat(keysArray.list.size) { i ->
                                val onlineMatch = dataSnapshot.child(keysArray.getElement(i)).getValue(OnlineMatch::class.java)!!
                                onlineMatch.key = keysArray.list[i]
                                requestsUsers.add(onlineMatch)
                            }
                        }


                    }

                    //Updating GUI
                    updateRequests()

                }


                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    displayToast("Error while retrieving data from DB")
                    // ...
                }
            }
            DataManager.instance.database.addValueEventListener(postListener)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                App.getInstance().actorSystem)
        actor.tell(FbOnActivityResultMsg(requestCode, resultCode, data), akka.actor.ActorRef.noSender())
    }

    override fun onResume() {
        super.onResume()
        if(FacebookManagerActor.Companion.isLoggedIn()) {
            updateRequests()
        }
    }

    fun initMainList() {
        listView = this.findViewById(app.simone.R.id.list_friends) as android.widget.ListView
        adapter = FacebookFriendsAdapter(this, friends)
        listView?.adapter = adapter
        listView?.onItemClickListener = android.widget.AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val actor = Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                    App.getInstance().actorSystem)
            val friend = adapter?.getItem(i)
            actor.tell(FbItemClickMsg(friend), akka.actor.ActorRef.noSender())
            enablePlayButton(friend!!)
        }
        btnPlay?.isEnabled = false
    }

    fun initRequestsList() {
        listViewRequests = this.findViewById(app.simone.R.id.listView_requests) as android.widget.ListView
        if(FacebookManagerActor.isLoggedIn()) {
            requestsAdapter = PendingRequestsAdapter(requestsUsers as ArrayList<OnlineMatch>, applicationContext)
            listViewRequests?.adapter = requestsAdapter
            listViewRequests?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val model = requestsUsers[position]
                println("ARRAY: " + position)
            }
            updateRequests()
        }
    }

    fun displayToast(text: String) {
        this.runOnUiThread {
            android.widget.Toast.makeText(this, text, android.widget.Toast.LENGTH_LONG).show()
        }
    }

    fun updateList (response : FbResponseFriendsMsg) {

        this.runOnUiThread {
            adapter?.clear()
            if(response.isSuccess) {
                adapter?.addAll(response.data)
            } else {
                displayToast(response.errorMessage)
            }
        }
    }

    fun enablePlayButton(user: app.simone.multiplayer.model.FacebookUser){
        selectedUser = user
        btnPlay?.isEnabled = true
    }

    fun setUser() {
        val profile = Profile.getCurrentProfile()
        currentUser = FacebookUser(profile.id, profile.name)
    }

    fun updateRequests() {
        this.runOnUiThread {
            if (requestsUsers.isNotEmpty()) {
                var tv = this.findViewById(app.simone.R.id.textView3) as android.widget.TextView
                tv.text = Companion.PENDING_REQUESTS
                if (FacebookManagerActor.isLoggedIn()) {
                    requestsUsers=DataManager.instance.filterRequests(requestsUsers, Profile.getCurrentProfile().id)
                    requestsAdapter?.clear()
                    //requestsAdapter = PendingRequestsAdapter(requestsUsers as ArrayList<OnlineMatch>, applicationContext)
                    requestsAdapter?.addAll(requestsUsers)

                }
            }
        }
    }


}
