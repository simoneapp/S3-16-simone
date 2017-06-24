package app.simone.users

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.Toast
import app.simone.R
import app.simone.users.model.FacebookFriend


class FacebookLoginActivity : IFacebookActivity, AppCompatActivity() {

    var manager = FacebookManager()
    var listView : ListView? = null

    var friends = ArrayList<FacebookFriend>()
    var adapter : FacebookFriendsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_facebook_login)
        manager.registerFacebookButton(this, { success, data, error -> updateList(success, data, error) })

        adapter = FacebookFriendsAdapter(this, friends, manager)

        listView = this.findViewById(R.id.list_friends) as ListView
        listView?.adapter = adapter

        if(manager.isLoggedIn()) {
            manager.getFacebookFriends { success, data, error -> updateList(success, data, error) }
        }
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

}
