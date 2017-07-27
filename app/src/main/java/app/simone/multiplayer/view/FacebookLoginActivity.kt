package app.simone.multiplayer.view

import android.content.Intent
import android.widget.AdapterView
import android.widget.Toolbar
import app.simone.R
import app.simone.multiplayer.controller.DataManager
import app.simone.multiplayer.controller.FacebookManagerActor
import app.simone.multiplayer.controller.PubnubController
import app.simone.multiplayer.messages.*
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.model.OnlineMatch
import app.simone.shared.application.App
import app.simone.shared.utils.Utilities
import app.simone.shared.utils.filterFacebookUser
import app.simone.singleplayer.view.GameActivity
import com.facebook.Profile
import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import io.realm.Realm


class FacebookLoginActivity : android.support.v7.app.AppCompatActivity() {

    var listView : android.widget.ListView? = null
    var btnPlay : android.widget.Button? = null

    var friends = ArrayList<app.simone.multiplayer.model.FacebookUser>()
    var adapter : FacebookFriendsAdapter? = null

    var pubnubController = PubnubController("multiplayer")

    var listViewRequests : android.widget.ListView? = null
    var requestsUsers = ArrayList<OnlineMatch>()
    var requestsAdapter : PubnubAdapter? = null

    var currentUser : FacebookUser? = null
    var selectedUser : FacebookUser? = null
    var realm: io.realm.Realm? = null

    companion object {
        val PENDING_REQUESTS : String = "Pending requests:"
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        DataManager.Companion.instance.resetOpponentScore()

        pubnubController.subscribeToChannel()
        this.addPubnubListener(pubnubController.pubnub)

        setContentView(app.simone.R.layout.activity_facebook_login)

        initMainList()
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
            if(selectedUser != null) {
                val activityIntent = Intent(baseContext, GameActivity::class.java)
                activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                println("ME: "+ com.facebook.Profile.getCurrentProfile().firstName.toString()+" "+ com.facebook.Profile.getCurrentProfile().lastName.toString())
                setUser()

                val onlineMatch=OnlineMatch(currentUser,selectedUser)
                onlineMatch.kindOfMsg="insert"
                DataManager.instance.saveRequestLocally(onlineMatch)

                //sending data to the GameActivity
                activityIntent.putExtra("sender", currentUser?.id)
                activityIntent.putExtra("recipient", selectedUser?.id)
                baseContext.startActivity(activityIntent)
            }
        })
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
        requestsAdapter = PubnubAdapter(requestsUsers, applicationContext)
        listViewRequests?.adapter = requestsAdapter
        listViewRequests?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val model = requestsUsers[position]
            println("ARRAY: " + position)
        }
    }

    fun displayToast(text: String) {
        this.runOnUiThread {
            android.widget.Toast.makeText(this, text, android.widget.Toast.LENGTH_SHORT).show()
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

    fun addPubnubListener(obj: PubNub){

        obj.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
            }

            override fun message(pubnub: PubNub, message: PNMessageResult) {

                if (message.channel != null) {
                    runOnUiThread {

                        val msg = message.message as JsonObject
                        if(msg.filterFacebookUser(Profile.getCurrentProfile().id.toString())) {
                            displayToast("msg ricevuto..")
                            //displayToast(msg.toString())
                            DataManager.Companion.instance.saveRequest(msg.asJsonObject)
                            updateRequests()
                        }
                    }
                }
            }
            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {

            }
        })

    }

    fun setUser() {
        val profile = Profile.getCurrentProfile()
        currentUser = FacebookUser(profile.id, profile.name)
    }

    fun updateRequests() {
        this.runOnUiThread {
            val requests = DataManager.Companion.instance.getPendingRequests()
            if (requests.isNotEmpty()) {
                //Log.d("SAM",requests.first().firstPlayer.name+" "+requests.first().secondPlayer.name + " " +requests.first().firstPlayer.score+" "+requests.first().secondPlayer.score)
                var tv = this.findViewById(app.simone.R.id.textView3) as android.widget.TextView
                tv.text = Companion.PENDING_REQUESTS

                requestsAdapter?.clear()
                requestsAdapter?.addAll(requests)
            }
        }
    }


}
