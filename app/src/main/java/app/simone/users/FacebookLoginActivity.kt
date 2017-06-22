package app.simone.users

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.simone.R


class FacebookLoginActivity : AppCompatActivity() {

    var manager : FacebookManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_facebook_login)
        manager = FacebookManager(this)
        manager?.register()
    }

    fun updateFriendsList() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        manager?.onActivityResult(requestCode, resultCode, data)
    }

}
