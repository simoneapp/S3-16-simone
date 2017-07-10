package app.simone.users

import PubNub.PubnubController
import akka.actor.ActorRef
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import app.simone.R
import app.simone.users.model.FacebookFriend
import com.facebook.Profile
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.PNStatus
import PubNub.OnlinePlayer
import PubNub.CustomAdapter
import android.util.Log
import android.widget.*
import app.simone.DataModel.OnlineMatch
import com.google.gson.JsonObject
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import PubNub.PushNotification
import app.simone.GameActivity
import com.google.gson.JsonElement
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import application.mApplication
import messages.*
import utils.Constants
import utils.Utilities


class FacebookLoginActivity : AppCompatActivity() {

    var listView : ListView? = null
    var listViewRequests : ListView? =null

    var friends = ArrayList<FacebookFriend>()
    var adapter : FacebookFriendsAdapter? = null
    var pubnubController = PubnubController("multiplayer")
    var player: OnlinePlayer? = null
    var realm: Realm ? = null
    var tempScore: String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pubnubController.subscribeToChannel()
        this.addPubnubListener(pubnubController.pubnub)
        initRealm()

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
        disablePlayButton()
    }

    fun displayToast(text: String) {
        this.runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME, mApplication.getActorSystem())
        actor.tell(FbOnActivityResultMsg(requestCode, resultCode, data), ActorRef.noSender())
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
        player = OnlinePlayer(Profile.getCurrentProfile().id.toString(),Profile.getCurrentProfile().firstName.toString(),Profile.getCurrentProfile().lastName.toString())
    }

    fun enablePlayButton(friend: FacebookFriend){
        val btnPlay = this.findViewById(R.id.playButton) as Button
        btnPlay.isEnabled = true
        btnPlay.setOnClickListener({
            val activityIntent = Intent(baseContext, GameActivity::class.java)
            activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            println("ME: "+Profile.getCurrentProfile().firstName.toString()+" "+Profile.getCurrentProfile().lastName.toString())
            activityIntent.putExtra("player", player)
            activityIntent.putExtra("toPlayer", fromFriendToPlayer(friend))
            baseContext.startActivity(activityIntent)
            disablePlayButton()
        })
    }

    fun disablePlayButton(){
        val btnPlay = this.findViewById(R.id.playButton) as Button
        btnPlay.isEnabled = false
    }

    fun fromFriendToPlayer(friend: FacebookFriend):OnlinePlayer{
        return OnlinePlayer(friend.friendId,friend.name,"")
    }


    fun addPubnubListener(obj: PubNub){
        //Toast.makeText(applicationContext, "SONO IN ASCOLTO..", Toast.LENGTH_SHORT).show()
        obj.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
            }

            override fun message(pubnub: PubNub, message: PNMessageResult) {
                if (message.channel != null) {
                    val msg = message.message
                    runOnUiThread {
                        if(filter(msg)) {
                            //Toast.makeText(applicationContext, "Richiesta ricevuta da" + msg.asJsonObject.get("to").asString, Toast.LENGTH_SHORT).show()
                            saveOpponentScore(msg)
                            saveRequestId(msg.asJsonObject)
                            updateListViewRequests()
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
        val fromUser = msg.asJsonObject.get("idP1").asString
        val toUser = msg.asJsonObject.get("idP2").asString
        return toUser==myId || fromUser==myId
        //return fromUser!=myId && toUser==myId
    }

    fun filterNotification(msg: JsonElement):Boolean{
        val myId = Profile.getCurrentProfile().id.toString()
        val fromUser = msg.asJsonObject.get("idP1").asString
        val toUser = msg.asJsonObject.get("idP2").asString
        val scoreP2=msg.asJsonObject.get("scoreP2").asString
        val scoreP1=msg.asJsonObject.get("scoreP1").asString
        return fromUser!=myId && toUser==myId && scoreP2=="--" && scoreP1=="--"
    }

    fun saveOpponentScore(msg:JsonElement){
        val scoreP2=msg.asJsonObject.get("scoreP2").asString
        if(scoreP2!="--" && scoreP2!=null){
            tempScore=scoreP2
        }
    }

    fun saveRequestId(obj: JsonObject){
        Log.d("PR JSON",obj.toString())
        var pr = fromJSONtoObj(obj)
        if(tempScore!=""){
            pr.scoreP2=tempScore
            tempScore=""
        }
        Log.d("PR OBJ",pr.toString())

        try {
            realm?.executeTransaction { realm ->
                realm.insertOrUpdate(pr)
                //Toast.makeText(applicationContext,"TRANSAZIONE "+ pr.nameP1 +" "+pr.scoreP1+" "+pr.nameP2 +" :"+pr.scoreP2, Toast.LENGTH_SHORT).show()
            }

            if(filterNotification(obj)) {
                PushNotification(applicationContext, pr.nameP1).init()
            }
        }
        catch (e: RealmPrimaryKeyConstraintException) {
            Log.d("DB","The value is already in the database!")
        }

    }

    private fun fromJSONtoObj(obj: JsonObject): OnlineMatch {

        val idP1=obj.get("idP1").asString
        val nameP1=obj.get("nameP1").asString
        val scoreP1=obj.get("scoreP1").asString
        val idP2=obj.get("idP2").asString
        val nameP2=obj.get("nameP2").asString
        val scoreP2=obj.get("scoreP2").asString

        val pr = OnlineMatch()
        pr.idP1 =idP1
        pr.nameP1 =nameP1
        pr.idP2 =idP2
        pr.nameP2 =nameP2
        pr.scoreP1=scoreP1
        pr.scoreP2=scoreP2
        return pr
 }


    private fun initRealm(){
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(3)
                .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

    }

    fun getPendingRequests():RealmResults<OnlineMatch>{
        return realm!!.where(OnlineMatch::class.java).findAll()
    }

    fun updateListViewRequests(){
        this.runOnUiThread {
            if (getPendingRequests().isNotEmpty()) {
                var myTextView = this.findViewById(R.id.textView3) as TextView
                myTextView.text = "Sfide in sospeso:"
                listViewRequests = this.findViewById(R.id.listView_requests) as ListView
                var dataModels = java.util.ArrayList<OnlineMatch>()
                var pr = getPendingRequests()

                pr.forEach { request -> dataModels.add(OnlineMatch(request.idP1, request.nameP1, request.scoreP1, request.idP2, request.nameP2, request.scoreP2)) }

                val adapter = CustomAdapter(dataModels, applicationContext)
                listViewRequests?.adapter = adapter
                listViewRequests?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val dataModel = dataModels[position]
                }
            }
        }

    }

}
