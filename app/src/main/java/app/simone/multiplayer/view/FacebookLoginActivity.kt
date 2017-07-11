package app.simone.multiplayer.view

import app.simone.multiplayer.controller.DataManager
import app.simone.multiplayer.controller.FacebookManagerActor
import app.simone.multiplayer.controller.PubnubController
import app.simone.multiplayer.messages.*
import app.simone.multiplayer.model.OnlineMatch
import app.simone.shared.application.App
import app.simone.shared.utils.filterFacebookUser
import app.simone.singleplayer.view.GameActivity


class FacebookLoginActivity : android.support.v7.app.AppCompatActivity() {

    var listView : android.widget.ListView? = null
    var btnPlay : android.widget.Button? = null

    var friends = ArrayList<app.simone.multiplayer.model.FacebookUser>()
    var adapter : FacebookFriendsAdapter? = null

    var pubnubController = PubnubController("multiplayer")

    var listViewRequests : android.widget.ListView? = null
    var requestsUsers = ArrayList<OnlineMatch>()
    var requestsAdapter : PubnubAdapter? = null

    var currentUser : app.simone.multiplayer.model.FacebookUser? = null
    var selectedUser : app.simone.multiplayer.model.FacebookUser? = null
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
                val activityIntent = android.content.Intent(baseContext, GameActivity::class.java)
                activityIntent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                println("ME: "+ com.facebook.Profile.getCurrentProfile().firstName.toString()+" "+
                        com.facebook.Profile.getCurrentProfile().lastName.toString())
                activityIntent.putExtra("sender", currentUser)
                activityIntent.putExtra("recipient", selectedUser)
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
            val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
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
        listViewRequests?.onItemClickListener = android.widget.AdapterView.OnItemClickListener { parent, view, position, id ->
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

    fun addPubnubListener(obj: com.pubnub.api.PubNub){

        obj.addListener(object : com.pubnub.api.callbacks.SubscribeCallback() {
            override fun status(pubnub: com.pubnub.api.PubNub, status: com.pubnub.api.models.consumer.PNStatus) {
            }

            override fun message(pubnub: com.pubnub.api.PubNub, message: com.pubnub.api.models.consumer.pubsub.PNMessageResult) {
                if (message.channel != null) {
                    runOnUiThread {
                        val msg = message.message.asJsonObject
                        if(msg.filterFacebookUser(com.facebook.Profile.getCurrentProfile().id.toString())) {
                            DataManager.Companion.instance.saveRequest(msg.asJsonObject)
                            DataManager.Companion.instance.saveOpponentScore(msg)
                            updateRequests()
                        }
                    }
                }
            }
            override fun presence(pubnub: com.pubnub.api.PubNub, presence: com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult) {
            }
        })

    }

    fun setUser() {
        val profile = com.facebook.Profile.getCurrentProfile()
        currentUser = app.simone.multiplayer.model.FacebookUser(profile.id, profile.name)
    }

    fun updateRequests() {
        this.runOnUiThread {
            val requests = DataManager.Companion.instance.getPendingRequests()
            if (requests.isNotEmpty()) {
                var tv = this.findViewById(app.simone.R.id.textView3) as android.widget.TextView
                tv.text = app.simone.multiplayer.view.FacebookLoginActivity.Companion.PENDING_REQUESTS

                requestsAdapter?.clear()
                requestsAdapter?.addAll(requestsUsers)
            }
        }
    }
}
