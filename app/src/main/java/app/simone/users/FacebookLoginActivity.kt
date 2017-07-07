package app.simone.users

import PubNub.CustomAdapter
import PubNub.OnlinePlayer
import PubNub.PubnubController
import akka.actor.ActorRef
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import app.simone.Controller.ControllerImplementations.DataManager
import app.simone.GameActivity
import app.simone.R
import app.simone.users.model.FacebookFriend
import application.mApplication
import com.facebook.Profile
import com.google.gson.JsonElement
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import io.realm.Realm
import messages.*
import utils.Constants
import utils.Utilities


class FacebookLoginActivity : AppCompatActivity() {

    var listView : ListView? = null
    var btnPlay : Button? = null

    var friends = ArrayList<FacebookFriend>()
    var adapter : FacebookFriendsAdapter? = null

    var pubnubController = PubnubController("multiplayer")
    var player: OnlinePlayer? = null

    var listViewRequests : ListView? = null
    var requestsUsers = ArrayList<OnlinePlayer>()
    var requestsAdapter : CustomAdapter? = null

    var selectedFriend : FacebookFriend? = null
    var realm: Realm ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pubnubController.subscribeToChannel()
        this.addPubnubListener(pubnubController.pubnub)

        setContentView(R.layout.activity_facebook_login)

        listView = this.findViewById(R.id.list_friends) as ListView
        adapter = FacebookFriendsAdapter(this, friends)
        listView?.adapter = adapter

        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME, mApplication.getActorSystem())
        actor.tell(FbViewSetupMsg(this), ActorRef.noSender())

        val btnInvites = this.findViewById(R.id.btn_invite) as Button
        btnInvites.setOnClickListener({
            actor.tell(FbSendGameRequestMsg(), ActorRef.noSender())
        })

        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

            val friend = adapter?.getItem(i)
            actor.tell(FbItemClickMsg(friend), ActorRef.noSender())
            enablePlayButton(friend!!)
        }


        listViewRequests = this.findViewById(R.id.listView_requests) as ListView

        listViewRequests?.adapter = requestsAdapter
        listViewRequests?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val model = requestsUsers[position]
            println("ARRAY: " + position)
        }

        btnPlay = this.findViewById(R.id.playButton) as Button
        btnPlay?.setOnClickListener({
            if(selectedFriend != null) {
                val activityIntent = Intent(baseContext, GameActivity::class.java)
                activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                println("ME: "+Profile.getCurrentProfile().firstName.toString()+" "+Profile.getCurrentProfile().lastName.toString())
                activityIntent.putExtra("player", player)
                activityIntent.putExtra("toPlayer", fromFriendToPlayer(selectedFriend!!))
                baseContext.startActivity(activityIntent)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME, mApplication.getActorSystem())
        actor.tell(FbOnActivityResultMsg(requestCode, resultCode, data), ActorRef.noSender())
    }

    override fun onResume() {
        super.onResume()
        if(FacebookManagerActor.isLoggedIn()) {
            updateRequests()
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


    fun setMyUsername(){
        player = OnlinePlayer(
                Profile.getCurrentProfile().id.toString(),
                Profile.getCurrentProfile().firstName.toString(),
                Profile.getCurrentProfile().lastName.toString()
        )
    }

    fun enablePlayButton(friend: FacebookFriend){
        selectedFriend = friend
        btnPlay?.isEnabled = true
    }

    fun fromFriendToPlayer(friend: FacebookFriend):OnlinePlayer{
        return OnlinePlayer(friend.friendId,friend.name,"")
    }

    fun addPubnubListener(obj: PubNub){

        obj.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
            }

            override fun message(pubnub: PubNub, message: PNMessageResult) {
                if (message.channel != null) {
                    val msg = message.message
                    runOnUiThread {
                        if(filter(msg)) {
                            //Toast.makeText(applicationContext, "Richiesta ricevuta da" + msg.asJsonObject.get("to").asString, Toast.LENGTH_SHORT).show()
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

    fun filter(msg: JsonElement):Boolean{
        val myId = Profile.getCurrentProfile().id.toString()
        val fromUser = msg.asJsonObject.get("from").asString
        val toUser = msg.asJsonObject.get("to").asString
        return fromUser != myId && toUser == myId
    }

    fun updateRequests(){
        this.runOnUiThread {
            val requests = DataManager.instance.getPendingRequests()
            if (requests.isNotEmpty()) {
                var tv = this.findViewById(R.id.textView3) as TextView
                tv.text = "Richieste in sospeso:"

                requestsAdapter?.clear()
                requests.forEach { request -> requestsUsers.add(OnlinePlayer(request.idTo, request.nameTo, "")) }
                requestsAdapter?.addAll(requestsUsers)
            }
        }
    }

}
