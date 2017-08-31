package app.simone.multiplayer.controller

import akka.actor.ActorRef
import akka.actor.UntypedActor
import android.os.Bundle
import app.simone.multiplayer.messages.FbRequestFriendsMsgMock
import app.simone.multiplayer.messages.FbResponseFriendsMsg
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.model.GraphRequestWrapper
import app.simone.multiplayer.model.RealGraphRequestWrapper
import app.simone.shared.messages.IMessage
import app.simone.singleplayer.messages.MessageType
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.google.gson.Gson
import com.google.gson.JsonElement

/**
 * Actor that handles the communication with the Facebook Graph, e.g. for fetching the
 * list of friends.
 * @author Nicola Giancecchi
 */
class FacebookManagerActor : UntypedActor() {

    val FRIENDS_PATH = "/me/friends"
    val FRIENDS_LIMIT = 5000
    val FIELDS = "name,picture,id"
    var currentSender : ActorRef? = null

    /**
     * Akka's onReceive function, called everytime the current actor receives a message.
     * @param message the incoming message.
     */
    override fun onReceive(message: Any?) {

        currentSender = sender

        when ((message as IMessage).type) {
            MessageType.FB_REQUEST_FRIENDS_MSG -> {
                this.getFacebookFriends(null, null)
            }

            MessageType.FB_REQUEST_FRIENDS_MSG_MOCK -> {
                val msg = message as FbRequestFriendsMsgMock
                this.getFacebookFriends(msg.bundle, msg.request)
            }
        }
    }

    /**
     * Handles the download of the list of Facebook friends from the current logged user.
     * @param parameters A real or mocked bundle object that will contain the parameters of the request
     * @param request A real or mocked graph request
     */
    fun getFacebookFriends(parameters: Bundle?, request: GraphRequestWrapper?) {

        var params = parameters
        if(params == null) {
            params = Bundle()
        }

        params.putInt("limit",FRIENDS_LIMIT)
        params.putString("fields", FIELDS)

        var request = request
        if(request == null) {
            request = RealGraphRequestWrapper(
                    AccessToken.getCurrentAccessToken(),
                    FRIENDS_PATH,
                    parameters,
                    HttpMethod.GET,
                    null)
        }

        request?.request.callback = GraphRequest.Callback { this.handleFriendsResponse(it) }

        request.executeAsync()
    }

    /**
     * Handles the response containing friends (or error) from the server
     * @param response the GraphResponse object received from the server or mocked
     */
    fun handleFriendsResponse(response: GraphResponse) {
        if(response.error == null) {
            val gson = Gson()
            val jsonFriends = gson
                    .fromJson(response.rawResponse, JsonElement::class.java)
                    .asJsonObject.get("data").asJsonArray

            val list = FacebookUser.listFromJson(jsonFriends)
            currentSender?.tell(FbResponseFriendsMsg(list), self)
        } else {
            currentSender?.tell(FbResponseFriendsMsg(response.error.errorUserMessage), self)
        }
    }

    /**
     * Helper method to know if there's a user currently logged
     * @return boolean indicating if the user is logged or not.
     */
    companion object {
        fun isLoggedIn() : Boolean {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null
        }
    }
}
