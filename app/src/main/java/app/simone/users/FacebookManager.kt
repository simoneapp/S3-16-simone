package app.simone.users

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import app.simone.R
import app.simone.users.model.FacebookFriend
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

/**
 * Created by nicola on 21/06/2017.
 */

class FacebookManager() {

    val GRAPH_PATH = "/me/taggable_friends"
    val FRIENDS_LIMIT = "5000"
    val FIELDS = "name,picture,id"

    val FB_LOGIN_CANCELLED_MSG = "Facebook Login action cancelled from the user!"
    val FB_LOGIN_ERROR_MSG = "Facebook Login Error: "
    val FB_PERMISSIONS = arrayOf("email", "user_friends", "read_custom_friendlists").toMutableList()

    var callbackManager : CallbackManager? = null
    var loginButton : LoginButton? = null

    fun registerFacebookButton(context : Activity, update: (success: Boolean, data: List<FacebookFriend>?, error: String?) -> Unit) {

        callbackManager = CallbackManager.Factory.create()

        loginButton = context.findViewById(R.id.login_button) as LoginButton
        loginButton?.setReadPermissions(FB_PERMISSIONS)

        loginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                getFacebookFriends { success, data, error -> update(success, data, error) }
            }

            override fun onCancel() {
                update(false, null, FB_LOGIN_CANCELLED_MSG)
            }

            override fun onError(exception: FacebookException) {
                update(false, null, FB_LOGIN_ERROR_MSG + exception.localizedMessage)
            }
        })
    }

    fun getFacebookFriends(completion: (success: Boolean, data: List<FacebookFriend>?, error: String?) -> Unit) {

        val parameters = Bundle()
        parameters.putString("limit",FRIENDS_LIMIT)
        parameters.putString("fields", FIELDS)

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                GRAPH_PATH,
                parameters,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    if(response.error == null) {
                        val jsonFriends = response.jsonObject.getJSONArray("data")
                        completion(true, FacebookFriend.listFromJson(jsonFriends), null)
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