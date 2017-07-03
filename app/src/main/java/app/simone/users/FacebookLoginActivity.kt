package app.simone.users

import PubNub.PubnubController
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.simone.GameActivity
import app.simone.R
import app.simone.users.model.FacebookFriend
import com.facebook.Profile
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.PNStatus
import PubNub.OnlinePlayer
import android.preference.PreferenceManager
import PubNub.CustomAdapter
import android.widget.*


class FacebookLoginActivity : AppCompatActivity() {

    var manager = FacebookManager()
    var listView : ListView? = null
    var listViewRequests : ListView? =null

    var friends = ArrayList<FacebookFriend>()
    var adapter : FacebookFriendsAdapter? = null
    var pubnubController = PubnubController("multiplayer")
    var myId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Pubnub handler
        pubnubController.subscribeToChannel()
        this.addPubnubListener(pubnubController.pubnub)

        setContentView(R.layout.activity_facebook_login)
        manager.registerFacebookButton(this, { success, data, error -> updateList(success, data, error) })

        adapter = FacebookFriendsAdapter(this, friends, manager)

        listView = this.findViewById(R.id.list_friends) as ListView
        listView?.adapter = adapter

        if(manager.isLoggedIn()) {
            manager.getFacebookFriends { success, data, error -> updateList(success, data, error) }
            setMyUsername()
        }


        val btnInvites = this.findViewById(R.id.btn_invite) as Button
        btnInvites.setOnClickListener({
            manager?.sendGameRequest()
        })

        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->

            val friend = adapter?.getItem(i)

            manager.getFriendScore(friend) {
                success, score, error ->

                if(success) {
                   // Toast.makeText(this, "Score: " + score, Toast.LENGTH_SHORT).show()

                    //pubnubController.publishToChannel("");
                    //Toast.makeText(this,"Ricevuta richiesta da Ciccio", Toast.LENGTH_SHORT).show()
                    enablePlayButton()
                } else {
                    Toast.makeText(this, "Error: cannot fetch user's score.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //disablePlayButton()
    }

    val updateList = { success: Boolean, data: List<FacebookFriend>?, error: String? ->

        adapter?.clear()

        if(success) {
            adapter?.addAll(data)
        } else {
            Toast.makeText(this, error, Toast.LENGTH_SHORT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        manager.onActivityResult(requestCode, resultCode, data)
    }

    fun setMyUsername(){
        myId=Profile.getCurrentProfile().id.toString()
        println("MY ID is: "+myId)
    }

    fun enablePlayButton(){
        val btnPlay = this.findViewById(R.id.playButton) as Button
        btnPlay.isEnabled = true
        btnPlay.setOnClickListener({
            println("starting game..")
            val activityIntent = Intent(baseContext, GameActivity::class.java)
            activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            println("ME: "+Profile.getCurrentProfile().firstName.toString()+" "+Profile.getCurrentProfile().lastName.toString())
            activityIntent.putExtra("id",myId)
            activityIntent.putExtra("firstname",Profile.getCurrentProfile().firstName.toString())
            activityIntent.putExtra("surname",Profile.getCurrentProfile().lastName.toString())
            baseContext.startActivity(activityIntent)

        })
    }

    fun addPubnubListener(obj: PubNub){

        obj.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {

            }

            override fun message(pubnub: PubNub, message: PNMessageResult) {
                if (message.channel != null) {
                    val msg = message.message
                    //var firstname: String by msg.byString("firstname")
                    //var firstname: String = "${msg.get("firstname")}"

                    System.out.println(msg)
                    runOnUiThread {
                        //msgView.setText(msg)
                        if(msg.asString !=Profile.getCurrentProfile().id.toString() )
                        saveRequestId(msg.asString)
                        //Toast.makeText(applicationContext, "RICHIESTA RICEVUTA DA: "+msg.asString, Toast.LENGTH_SHORT).show()
                        updateListViewRequests()

                    }
                }
            }

            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {

            }
        })

    }

    fun saveRequestId(id: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putString("id", id)
        editor.apply()
    }

    fun getPendingRequests():String{
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var idReq: String = preferences.getString("id", "")
        if (idReq != null) {
            return idReq
        }else{
            return ""
        }

    }

    fun updateListViewRequests(){

        if(getPendingRequests()!="") {

            var myTextView = this.findViewById(R.id.textView3) as TextView
            myTextView.text = "Richieste in sospeso:"

            listViewRequests = this.findViewById(R.id.listView_requests) as ListView

            var dataModels = java.util.ArrayList<OnlinePlayer>()

           /* dataModels.add(OnlinePlayer("1", "Ciccio", "1"))
            dataModels.add(OnlinePlayer("2", "Ciccio", "2"))
            dataModels.add(OnlinePlayer("3", "Ciccio", "3")) */

            dataModels.add(OnlinePlayer("ID: "+getPendingRequests(),"Ciccio","1"))

            val adapter = CustomAdapter(dataModels, applicationContext)

            listViewRequests?.adapter = adapter

            listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val dataModel = dataModels[position]
                println("cliccato posizione " + dataModel.toString())
            }
        }

    }

}
