package app.simone.users

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import app.simone.R
import app.simone.users.model.FacebookFriend
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONObject

/**
 * Created by nicola on 21/06/2017.
 */

class FacebookManager() {

    var callbackManager : CallbackManager? = null
    var loginButton : LoginButton? = null

    fun registerFacebookButton(context : Activity) {

        callbackManager = CallbackManager.Factory.create()

        loginButton = context.findViewById(R.id.login_button) as LoginButton
        loginButton?.setReadPermissions(arrayOf("email", "user_friends", "read_custom_friendlists").toMutableList())

        // Callback registration
        val registerCallback = loginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                Log.d("FBLOGIN", "FBLogin With Success!  " + loginResult.toString())

                getFacebookFriends { success, data, error ->


                }
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

    fun getFacebookFriends(completion: (success: Boolean, data: List<FacebookFriend>?, error: String?) -> Unit) {

        val parameters = Bundle()
        parameters.putString("limit","5000")
        parameters.putString("fields", "name,picture,id")

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/taggable_friends",
                parameters,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    /* handle the result */

                    if(response.error == null) {
                        val objectFriends = ArrayList<FacebookFriend>()
                        val jsonFriends = response.jsonObject.getJSONArray("data")

                        (0..jsonFriends.length()-1)
                                .map { jsonFriends.get(it) as JSONObject }
                                .mapTo(objectFriends) { FacebookFriend(it) }

                        Log.v("FRIENDS", objectFriends.toString())
                        completion(true, objectFriends, null)
                    } else {
                        completion(false, null, response.error.errorUserMessage)
                    }
                }
        ).executeAsync()
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

}