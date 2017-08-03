package app.simone.multiplayer.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import app.simone.R
import app.simone.multiplayer.controller.DataManager
import app.simone.multiplayer.controller.FacebookManagerActor
import app.simone.multiplayer.controller.PubnubController
import app.simone.multiplayer.messages.FbItemClickMsg
import app.simone.multiplayer.messages.FbResponseFriendsMsg
import app.simone.multiplayer.messages.FbSendGameRequestMsg
import app.simone.multiplayer.messages.FbViewSetupMsg
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.model.OnlineMatch
import app.simone.shared.application.App
import app.simone.shared.utils.Utilities
import app.simone.singleplayer.view.GameActivity
import com.facebook.Profile
import java.util.*






class FacebookLoginActivity : Fragment() {

    var rootView : View? = null

    var listView : ListView? = null
    var btnPlay : Button? = null

    var friends = ArrayList<FacebookUser>()
    var adapter : FacebookFriendsAdapter? = null

    var pubnubController = PubnubController("multiplayer")

    var listViewRequests : ListView? = null
    var selectedUsers = ArrayList<FacebookUser>()

    var requestsUsers = ArrayList<OnlineMatch>()
    var requestsAdapter : PubnubAdapter? = null

    var currentUser : FacebookUser? = null
    var realm: io.realm.Realm? = null

    companion object {
        val PENDING_REQUESTS : String = "Pending requests:"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater?.inflate(R.layout.activity_facebook_login, container, false)

        DataManager.Companion.instance.resetOpponentScore()

        initMainList()
        initRequestsList()

        val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR
                + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                App.getInstance().actorSystem)

        actor.tell(FbViewSetupMsg(this), akka.actor.ActorRef.noSender())

        val btnInvites = rootView?.findViewById(app.simone.R.id.btn_invite) as android.widget.Button
        btnInvites.setOnClickListener({
            actor.tell(FbSendGameRequestMsg(), akka.actor.ActorRef.noSender())
        })

        btnPlay = rootView?.findViewById(app.simone.R.id.playButton) as android.widget.Button
        btnPlay?.setOnClickListener({
            if(selectedUsers.count() > 0) {
                val activityIntent = Intent(context, GameActivity::class.java)
                activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                println("ME: "+ com.facebook.Profile.getCurrentProfile().firstName.toString()+" "+ com.facebook.Profile.getCurrentProfile().lastName.toString())
                setUser()

                val user = selectedUsers.first()

                val onlineMatch=OnlineMatch(currentUser,user)
                onlineMatch.kindOfMsg="insert"
                DataManager.instance.saveRequestLocally(onlineMatch)

                //sending data to the GameActivity
                activityIntent.putExtra("sender", currentUser?.id)
                activityIntent.putExtra("recipient", user.id)
                context.startActivity(activityIntent)
            }
        })

        return rootView
    }

    override fun onResume() {
        super.onResume()
        if(FacebookManagerActor.Companion.isLoggedIn()) {
            updateRequests()
        }
    }

    fun initMainList() {
        listView = rootView?.findViewById(app.simone.R.id.list_friends) as android.widget.ListView
        adapter = FacebookFriendsAdapter(context, friends, this)
        listView?.adapter = adapter
        listView?.onItemClickListener = android.widget.AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val actor = Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                    App.getInstance().actorSystem)
            val friend = adapter?.getItem(i)
            actor.tell(FbItemClickMsg(friend), akka.actor.ActorRef.noSender())
            selectUser(friend)
        }
        btnPlay?.isEnabled = false
    }

    fun initRequestsList() {
        listViewRequests = rootView?.findViewById(app.simone.R.id.listView_requests) as android.widget.ListView
        requestsAdapter = PubnubAdapter(requestsUsers, context)
        listViewRequests?.adapter = requestsAdapter
        listViewRequests?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val model = requestsUsers[position]
            println("ARRAY: " + position)
        }
    }

    fun displayToast(text: String) {
        this.activity.runOnUiThread {
            android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_LONG).show()
        }
    }

    fun updateList (response : FbResponseFriendsMsg) {
        this.activity.runOnUiThread {
            adapter?.clear()
            if (response.isSuccess) {
                friends = response.data as ArrayList<FacebookUser>
                adapter?.addAll(friends)
            } else {
                displayToast(response.errorMessage)
            }
        }
    }

    fun selectUser(friend: FacebookUser?){

        if(friend != null) {

            val isSelected = !selectedUsers.contains(friend)

            if(isSelected)  {
                selectedUsers.add(friend)
            } else {
                selectedUsers.remove(friend)
            }

            btnPlay?.isEnabled = selectedUsers.count() > 0

            val pos = friends.indexOf(friend)
            val visiblePosition = listView?.firstVisiblePosition

            if(visiblePosition != null) {
                val view = listView?.getChildAt(pos - visiblePosition)
                listView?.adapter?.getView(pos, view, listView)
            }
        }
    }

    /*
    fun addPubnubListener(obj: PubNub){

        obj.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
            }

            override fun message(pubnub: PubNub, message: PNMessageResult) {

                if (message.channel != null) {
                    runOnUiThread {
                        val msg = message.message as JsonObject
                        if(msg.filterFacebookUser(Profile.getCurrentProfile().id.toString())) {
                            //displayToast("msg ricevuto..")
                            displayToast(msg.toString())
                            DataManager.Companion.instance.saveRequest(msg.asJsonObject)
                            updateRequests()
                        }
                    }
                }
            }
            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {

            }
        })

    }*/

    fun setUser() {
        val profile = Profile.getCurrentProfile()
        currentUser = FacebookUser(profile.id, profile.name)
    }

    fun updateRequests() {
        this.activity.runOnUiThread {
            val requests = DataManager.Companion.instance.getPendingRequests()
            if (requests.isNotEmpty()) {
                //Log.d("SAM",requests.first().firstPlayer.name+" "+requests.first().secondPlayer.name + " " +requests.first().firstPlayer.score+" "+requests.first().secondPlayer.score)
                var tv = rootView?.findViewById(app.simone.R.id.textView3) as android.widget.TextView
                tv.text = Companion.PENDING_REQUESTS

                requestsAdapter?.clear()
                requestsAdapter?.addAll(requests)
            }
        }
    }


}
