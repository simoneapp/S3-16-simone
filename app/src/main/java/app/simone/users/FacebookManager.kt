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
import com.facebook.share.model.GameRequestContent
import com.facebook.share.widget.GameRequestDialog



/**
 * Created by nicola on 21/06/2017.
 */

class FacebookManager() : IFacebookManager {

    val FRIENDS_PATH = "/me/friends"
    val SCORE_PATH = "/109981342949411/scores"
    val FRIENDS_LIMIT = 5000
    val FIELDS = "name,picture,id"

    val FB_LOGIN_CANCELLED_MSG = "Facebook Login action cancelled from the user!"
    val FB_LOGIN_ERROR_MSG = "Facebook Login Error: "
    val FB_PERMISSIONS = arrayOf("email", "user_friends", "read_custom_friendlists").toMutableList()

    var callbackManager : CallbackManager? = null
    var loginButton : LoginButton? = null
    var requestDialog : GameRequestDialog? = null

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

        requestDialog = GameRequestDialog(context)

        requestDialog?.registerCallback(callbackManager, object: FacebookCallback<GameRequestDialog.Result> {
            override fun onSuccess(result: GameRequestDialog.Result?) {
                Log.v("FB Request", result?.toString())
            }

            override fun onCancel() {
                Log.v("FB Request", "Cancel")
            }

            override fun onError(error: FacebookException?) {
                Log.v("FB Request", error?.localizedMessage.toString())
            }

        })
    }

    fun getFacebookFriends(completion: (success: Boolean, data: List<FacebookFriend>?, error: String?) -> Unit) {

        val parameters = Bundle()
        parameters.putInt("limit",FRIENDS_LIMIT)
        parameters.putString("fields", FIELDS)

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                FRIENDS_PATH,
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

    override fun updateScore(score: Int, completion: (success: Boolean, error: String?) -> Unit) {

        val parameters = Bundle()
        parameters.putInt("score",score)

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                SCORE_PATH,
                parameters,
                HttpMethod.POST,
                GraphRequest.Callback { response ->
                    if(response.error == null) {
                        completion(true, null)
                    } else {
                        completion(false, response.error.errorUserMessage)
                    }
                }
        ).executeAsync()
    }

    override fun getScore(completion: (success: Boolean, score: Int, error: String?) -> Unit) {

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                SCORE_PATH,
                null,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    if(response.error == null) {
                        completion(true, 0, null)
                    } else {
                        completion(false, 0, response.error.errorUserMessage)
                    }
                }
        ).executeAsync()
    }

    fun sendGameRequest() {
        val content = GameRequestContent.Builder()
                .setMessage("If you move like Simone, you can't go wrong! Come and give a try to Simone for Android!")
                .build()

        requestDialog?.show(content)
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

}