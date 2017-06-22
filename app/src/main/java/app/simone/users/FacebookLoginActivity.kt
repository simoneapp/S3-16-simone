package app.simone.users

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import app.simone.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


class FacebookLoginActivity : AppCompatActivity() {

    var callbackManager : CallbackManager? = null
    var loginButton : LoginButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_facebook_login)

        callbackManager = CallbackManager.Factory.create()

        loginButton = this.findViewById(R.id.login_button) as LoginButton
        loginButton?.setReadPermissions(arrayOf("email").toMutableList())

        // If using in a fragment
        //loginButton.setFragment(this)
        // Other app specific specialization

        val context = this

        // Callback registration
        loginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                Toast.makeText(context, "FBLogin with Success! " + loginResult.toString(), 3000)
            }

            override fun onCancel() {
                // App code
                Toast.makeText(context, "FBLogin Cancel!", 3000)
            }

            override fun onError(exception: FacebookException) {
                // App code
                Toast.makeText(context, "FBLogin Error! " + exception.toString(), 3000)
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

}
