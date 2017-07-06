package app.simone.users

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
import application.mApplication
import messages.*
import utils.Constants
import utils.Utilities


class FacebookLoginActivity : AppCompatActivity() {

    var listView : ListView? = null
    var friends = ArrayList<FacebookFriend>()
    var adapter : FacebookFriendsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        }
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


}
