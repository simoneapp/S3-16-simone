package app.simone.multiplayer.controller

import akka.actor.ActorRef
import app.simone.R
import app.simone.multiplayer.messages.FbOnActivityResultMsg
import app.simone.multiplayer.messages.FbRequestFriendsMsg
import app.simone.multiplayer.messages.FbResponseFriendsMsg
import app.simone.multiplayer.messages.FbViewSetupMsg
import app.simone.multiplayer.view.pager.MultiplayerPagerActivity
import app.simone.shared.firebase.FCMTokenService
import app.simone.shared.messages.IMessage
import app.simone.shared.utils.Utilities
import app.simone.singleplayer.messages.MessageType
import app.simone.singleplayer.messages.TestMessage
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.share.model.GameRequestContent
import com.facebook.share.widget.GameRequestDialog

/**
 * Actor that handles the communication between the Manager actor (FacebookManagerActor)
 * and the Activity (MultiplayerPagerActivity)
 * @author Nicola Giancecchi
 */
class FacebookViewActor : akka.actor.UntypedActor() {

    var loginButton : LoginButton? = null
    var requestDialog : GameRequestDialog? = null
    var callbackManager : CallbackManager? = null
    var referenceActivity : MultiplayerPagerActivity? = null

    val FB_LOGIN_CANCELLED_MSG = "Facebook Login action cancelled from the user!"
    val FB_LOGIN_ERROR_MSG = "Facebook Login Error: "
    val FB_READ_PERMISSIONS = arrayOf("email", "user_friends", "read_custom_friendlists").toMutableList()

    var currentSender: ActorRef? = null

    /**
     * Akka's onReceive function, called everytime the current actor receives a message.
     * @param message the incoming message.
     */
    override fun onReceive(message: Any?) {

        currentSender = sender

        when((message as IMessage).type) {

        /**
         * Initial view configuration
         */
            MessageType.FB_VIEW_SETUP_MSG -> {
                val msg = message as FbViewSetupMsg
                this.registerAndAskFriends(msg)
            }

            // REQUESTS & INTERACTIONS

            MessageType.FB_GAME_REQUEST_MSG -> {
                this.sendGameRequest()
            }
        /**
         * Wrapper for Android's `onActivityResult` method, needed by the Facebook SDK.
         */
            MessageType.FB_ON_ACTIVITY_RESULT_MSG -> {
                val msg = message as FbOnActivityResultMsg
                callbackManager?.onActivityResult(msg.requestCode, msg.resultCode, msg.data)
            }

            // RESPONSES

        /**
         * Response from the server containing the list of friends
         */
            MessageType.FB_GET_FRIENDS_RESPONSE_MSG -> {
                val msg = message as FbResponseFriendsMsg
                referenceActivity?.friendsList?.updateList(msg)
            }
        }
    }

    /**
     * This function opens a Facebook GameRequest dialog inside the app. This component will handle
     * the entire process of inviting Facebook friends joining Simone.
     */
    fun sendGameRequest() {
        val content = GameRequestContent.Builder()
                .setMessage("If you move like Simone, you can't go wrong! Come and give a try to Simone for Android!")
                .build()

        requestDialog?.show(content)
        currentSender?.tell(TestMessage(), self)
    }

    /**
     * Registers the login button and asks for friends.
     */
    fun registerAndAskFriends(msg: FbViewSetupMsg) {
        this.registerLoginButton(msg)
        this.referenceActivity = msg.activity;

        if(FacebookManagerActor.Companion.isLoggedIn()) {
            msg.actorReference.tell(FbRequestFriendsMsg(), self)
            referenceActivity?.friendsList?.setUser()
            referenceActivity?.invites?.updateRequests()
        }
    }

    /**
     * Registration of callbacks for the Facebook Login Button
     * @param msg A FbViewSetupMsg instance containing the activity object.
     */
    fun registerLoginButton(msg: FbViewSetupMsg) {

        callbackManager = CallbackManager.Factory.create()

        loginButton = msg.activity?.findViewById(R.id.login_button) as LoginButton
        loginButton?.setReadPermissions(FB_READ_PERMISSIONS)

        loginButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                msg.actorReference.tell(FbRequestFriendsMsg(), self)
                msg.activity.setFacebookViewVisible(false)
                NearbyGameController().updateUserData()
                FCMTokenService.updateCurrentToken()
            }

            override fun onCancel() {
                Utilities.displayToast(FB_LOGIN_CANCELLED_MSG, msg.activity)
            }

            override fun onError(exception: FacebookException) {
                Utilities.displayToast(FB_LOGIN_ERROR_MSG + exception.localizedMessage, msg.activity)
            }
        })

        requestDialog = GameRequestDialog(msg.activity)

        requestDialog?.registerCallback(callbackManager, object: FacebookCallback<GameRequestDialog.Result> {
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
