package app.simone.users

import PubNub.CustomAdapter
import PubNub.PubnubController
import akka.actor.ActorRef
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import app.simone.Controller.ControllerImplementations.DataManager
import app.simone.GameActivity
import app.simone.R
import app.simone.users.model.FacebookUser
import application.App
import com.facebook.Profile
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import io.realm.Realm
import messages.*
import utils.Constants
import utils.Utilities
import utils.filterFacebookUser

class FacebookLoginActivity : AppCompatActivity() {

    var listView : ListView? = null
    var btnPlay : Button? = null

    var friends = ArrayList<FacebookUser>()
    var adapter : FacebookFriendsAdapter? = null

    var pubnubController = PubnubController("multiplayer")

    var listViewRequests : ListView? = null
    var requestsUsers = ArrayList<FacebookUser>()
    var requestsAdapter : CustomAdapter? = null

    var currentUser : FacebookUser? = null
    var selectedUser : FacebookUser? = null
    var realm: Realm ? = null

    companion object {
        val PENDING_REQUESTS : String = "Pending requests:"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pubnubController.subscribeToChannel()
        this.addPubnubListener(pubnubController.pubnub)

        setContentView(R.layout.activity_facebook_login)

        initMainList()
        initRequestsList()

        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME,
                App.getInstance().getActorSystem())
        actor.tell(FbViewSetupMsg(this), ActorRef.noSender())

        val btnInvites = this.findViewById(R.id.btn_invite) as Button
        btnInvites.setOnClickListener({
            actor.tell(FbSendGameRequestMsg(), ActorRef.noSender())
        })

        btnPlay = this.findViewById(R.id.playButton) as Button
        btnPlay?.setOnClickListener({
            if(selectedUser != null) {
                val activityIntent = Intent(baseContext, GameActivity::class.java)
                activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                println("ME: "+Profile.getCurrentProfile().firstName.toString()+" "+
                        Profile.getCurrentProfile().lastName.toString())
                activityIntent.putExtra("player", currentUser)
                activityIntent.putExtra("toPlayer", selectedUser) // fromFriendToPlayer(selectedUser!!))
                baseContext.startActivity(activityIntent)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME,
                App.getInstance().getActorSystem())
        actor.tell(FbOnActivityResultMsg(requestCode, resultCode, data), ActorRef.noSender())
    }

    override fun onResume() {
        super.onResume()
        if(FacebookManagerActor.isLoggedIn()) {
            updateRequests()
        }
    }

    fun initMainList() {
        listView = this.findViewById(R.id.list_friends) as ListView
        adapter = FacebookFriendsAdapter(this, friends)
        listView?.adapter = adapter
        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

            val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME,
                    App.getInstance().getActorSystem())
            val friend = adapter?.getItem(i)
            actor.tell(FbItemClickMsg(friend), ActorRef.noSender())
            enablePlayButton(friend!!)
        }
    }

    fun initRequestsList() {
        listViewRequests = this.findViewById(R.id.listView_requests) as ListView
        requestsAdapter = CustomAdapter(requestsUsers, applicationContext)
        listViewRequests?.adapter = requestsAdapter
        listViewRequests?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val model = requestsUsers[position]
            println("ARRAY: " + position)
        }
    }

    fun displayToast(text: String) {
        this.runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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

    fun enablePlayButton(user: FacebookUser){
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
                        val msg = message.message.asJsonObject
                        if(msg.filterFacebookUser(Profile.getCurrentProfile().id.toString())) {
                            DataManager.instance.saveRequest(msg.asJsonObject)
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
        currentUser = FacebookUser.with(profile.id, profile.name)
    }

    fun updateRequests(){
        this.runOnUiThread {
            val requests = DataManager.instance.getPendingRequests()
            if (requests.isNotEmpty()) {
                var tv = this.findViewById(R.id.textView3) as TextView
                tv.text = FacebookLoginActivity.PENDING_REQUESTS

                requestsAdapter?.clear()
                requestsAdapter?.addAll(requestsUsers)
            }
        }
    }

}
