package app.simone.users

import akka.actor.UntypedActor
import app.simone.R

import app.simone.users.model.FacebookUser
import application.App
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.share.model.GameRequestContent
import com.facebook.share.widget.GameRequestDialog
import messages.*
import utils.Constants
import utils.Utilities

/**
 * Created by nicola on 01/07/2017.
 */

class FacebookViewActor() : UntypedActor() {

    var activity : FacebookLoginActivity? = null

    var loginButton : LoginButton? = null
    var requestDialog : GameRequestDialog? = null
    var callbackManager : CallbackManager? = null
    var currentUser : FacebookUser? = null

    val FB_LOGIN_CANCELLED_MSG = "Facebook Login action cancelled from the user!"
    val FB_LOGIN_ERROR_MSG = "Facebook Login Error: "
    val FB_READ_PERMISSIONS = arrayOf("email", "user_friends", "read_custom_friendlists").toMutableList()

    override fun onReceive(message: Any?) {

        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FACEBOOK_ACTOR_NAME, App.getInstance().getActorSystem())

        when((message as IMessage).type) {

            // VIEW SETUP
            MessageType.FB_VIEW_SETUP_MSG -> {

                val msg = message as FbViewSetupMsg
                this.activity = msg.activity

                this.registerLoginButton()

                if(FacebookManagerActor.isLoggedIn()) {
                    actor.tell(FbRequestFriendsMsg(), self)
                    activity?.setUser()
                    activity?.updateRequests()
                }
            }

            // REQUESTS & INTERACTIONS

            MessageType.FB_ITEM_CLICK_MSG -> {
                val msg = message as FbItemClickMsg
                actor.tell(FbRequestGetUserScoreMsg(msg.friend), self)
            }

            MessageType.FB_GAME_REQUEST_MSG -> {
                this.sendGameRequest()
            }

            MessageType.FB_ON_ACTIVITY_RESULT_MSG -> {
                val msg = message as FbOnActivityResultMsg
                callbackManager?.onActivityResult(msg.requestCode, msg.resultCode, msg.data)
            }

            // RESPONSES

            MessageType.FB_GET_FRIENDS_RESPONSE_MSG -> {
                val msg = message as FbResponseFriendsMsg
                activity?.updateList(msg)
            }

            MessageType.FB_GET_SCORE_RESPONSE_MSG -> {

                val msg = message as FbResponseGetUserScoreMsg

                if(msg.isSuccess) {
                    //activity?.displayToast("Score: " + msg.data)
                } else  {
                    //activity?.displayToast("Error: cannot fetch user's score.")
                }
            }
        }
    }

    fun sendGameRequest() {
        val content = GameRequestContent.Builder()
                .setMessage("If you move like Simone, you can't go wrong! Come and give a try to Simone for Android!")
                .build()

        requestDialog?.show(content)
    }

    fun registerLoginButton() {

        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FACEBOOK_ACTOR_NAME, App.getInstance().getActorSystem())

        callbackManager = CallbackManager.Factory.create()

        loginButton = activity?.findViewById(R.id.login_button) as LoginButton

        loginButton?.setReadPermissions(FB_READ_PERMISSIONS)

        loginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                actor.tell(FbRequestFriendsMsg(), self)
            }

            override fun onCancel() {
                activity?.displayToast(FB_LOGIN_CANCELLED_MSG)
            }

            override fun onError(exception: FacebookException) {
                activity?.displayToast(FB_LOGIN_ERROR_MSG + exception.localizedMessage)
            }
        })

        requestDialog = GameRequestDialog(activity)

        requestDialog?.registerCallback(callbackManager, object: FacebookCallback<GameRequestDialog.Result> {
            override fun onSuccess(result: GameRequestDialog.Result?) {
                sendGameRequest()
            }

            override fun onCancel() {
                activity?.displayToast("Facebook Game Request canceled.")
            }

            override fun onError(error: FacebookException?) {
                activity?.displayToast(error?.localizedMessage.toString())
            }
        })
    }

}
