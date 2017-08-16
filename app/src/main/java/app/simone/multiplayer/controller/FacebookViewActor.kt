package app.simone.multiplayer.controller

import android.util.Log
import app.simone.multiplayer.messages.*
import app.simone.multiplayer.model.FacebookUser
import app.simone.shared.application.App
import app.simone.shared.firebase.FCMTokenService
import app.simone.shared.messages.IMessage
import app.simone.shared.utils.Utilities
import app.simone.singleplayer.messages.MessageType
import com.facebook.CallbackManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.share.widget.GameRequestDialog

/**
 * Created by nicola on 01/07/2017.
 */

class FacebookViewActor : akka.actor.UntypedActor() {

    //var activity : FriendsListFragment? = null

    var loginButton : LoginButton? = null
    var requestDialog : GameRequestDialog? = null
    var callbackManager : CallbackManager? = null

    val FB_LOGIN_CANCELLED_MSG = "Facebook Login action cancelled from the user!"
    val FB_LOGIN_ERROR_MSG = "Facebook Login Error: "
    val FB_READ_PERMISSIONS = arrayOf("email", "user_friends", "read_custom_friendlists").toMutableList()

    override fun onReceive(message: Any?) {

        val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR + app.simone.shared.utils.Constants.FACEBOOK_ACTOR_NAME, App.getInstance().getActorSystem())

        when((message as IMessage).type) {

            // VIEW SETUP
            MessageType.FB_VIEW_SETUP_MSG -> {

                val msg = message as FbViewSetupMsg

                this.registerLoginButton(msg)

                if(FacebookManagerActor.Companion.isLoggedIn()) {
                    actor.tell(FbRequestFriendsMsg(msg.activity), self)
                    msg.activity?.friendsList?.setUser()
                    msg.activity?.invites?.updateRequests()
                }
            }

            // REQUESTS & INTERACTIONS

            MessageType.FB_ITEM_CLICK_MSG -> {
                val msg = message as FbItemClickMsg
                //actor.tell(FbRequestGetUserScoreMsg(msg.friend), self)

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
                msg.activity?.friendsList?.updateList(msg)
                Log.d("List", "### Activity")
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
        val content = com.facebook.share.model.GameRequestContent.Builder()
                .setMessage("If you move like Simone, you can't go wrong! Come and give a try to Simone for Android!")
                .build()

        requestDialog?.show(content)
    }

    fun registerLoginButton(msg: FbViewSetupMsg) {


        val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR + app.simone.shared.utils.Constants.FACEBOOK_ACTOR_NAME, App.getInstance().getActorSystem())

        callbackManager = com.facebook.CallbackManager.Factory.create()

        loginButton = msg.activity?.findViewById(app.simone.R.id.login_button) as com.facebook.login.widget.LoginButton
        loginButton?.setReadPermissions(FB_READ_PERMISSIONS)
        loginButton?.registerCallback(callbackManager, object : com.facebook.FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: com.facebook.login.LoginResult) {
                actor.tell(FbRequestFriendsMsg(msg.activity), self)
                msg.activity.setFacebookViewVisible(false)
                NearbyGameController().updateUserData()
                FCMTokenService.updateCurrentToken()
            }

            override fun onCancel() {
                Utilities.displayToast(FB_LOGIN_CANCELLED_MSG, msg.activity)
            }

            override fun onError(exception: com.facebook.FacebookException) {
                Utilities.displayToast(FB_LOGIN_ERROR_MSG + exception.localizedMessage, msg.activity)
            }
        })

        requestDialog = GameRequestDialog(msg.activity)

        requestDialog?.registerCallback(callbackManager, object: com.facebook.FacebookCallback<GameRequestDialog.Result> {
            override fun onSuccess(result: com.facebook.share.widget.GameRequestDialog.Result?) {
                sendGameRequest()
            }

            override fun onCancel() {
                Utilities.displayToast("Facebook Game Request canceled.", msg.activity)
            }

            override fun onError(error: com.facebook.FacebookException?) {
                Utilities.displayToast(error?.localizedMessage.toString(), msg.activity)
            }
        })
    }

}
