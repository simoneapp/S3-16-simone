package app.simone.users

import android.app.Activity
import android.content.Intent
import android.util.Log
import app.simone.R
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton



/**
 * Created by nicola on 21/06/2017.
 */

class FacebookManager(val context: Activity) {

    var callbackManager : CallbackManager? = null
    var loginButton : LoginButton? = null

    fun register() {

        callbackManager = CallbackManager.Factory.create()

        loginButton = context.findViewById(R.id.login_button) as LoginButton
        loginButton?.setReadPermissions(arrayOf("email", "user_friends", "read_custom_friendlists").toMutableList())

        val context = this

        // Callback registration
        val registerCallback = loginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                Log.d("FBLOGIN", "FBLogin With Success!  " + loginResult.toString())
                getFacebookFriends()
            }

            override fun onCancel() {
                // App code
                Log.d("FBLOGIN", "FBLogin Cancel!")
            }

            override fun onError(exception: FacebookException) {
                // App code
                Log.d("FBLOGIN", "FBLogin Error! " + exception.toString())
            }
        })
    }

    fun getFacebookFriends() {

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friendlists",
                null,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    /* handle the result */
                    Log.v("FRIENDS","Friends " + response.toString())
                }
        ).executeAsync()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

}