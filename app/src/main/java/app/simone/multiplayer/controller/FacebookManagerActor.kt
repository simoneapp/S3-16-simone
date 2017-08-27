package app.simone.multiplayer.controller

import akka.actor.UntypedActor
import android.os.Bundle
import app.simone.multiplayer.messages.FbRequestFriendsMsgMock
import app.simone.multiplayer.messages.FbResponseFriendsMsg
import app.simone.multiplayer.model.FacebookUser
import app.simone.shared.messages.IMessage
import app.simone.singleplayer.messages.MessageType
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.google.gson.Gson
import com.google.gson.JsonElement

/**
 * Created by nicola on 01/07/2017.
 */

class FacebookManagerActor : UntypedActor() {

    val FRIENDS_PATH = "/me/friends"
    val FRIENDS_LIMIT = 5000
    val FIELDS = "name,picture,id"

    override fun onReceive(message: Any?) {

        when ((message as IMessage).type) {
            MessageType.FB_REQUEST_FRIENDS_MSG -> {
                this.getFacebookFriends(null)
            }

            MessageType.FB_REQUEST_FRIENDS_MSG_MOCK -> {
                val msg = message as FbRequestFriendsMsgMock
                this.getFacebookFriends(msg.bundle, msg.request)
            }
        }
    }

    fun getFacebookFriends(parameters: Bundle?, request: GraphRequest?) {

        var params = parameters
        if(params == null) {
            params = Bundle()
        } else {
            this.handleFriendsResponse()
            //// METTERE A POSTO, CHIAMARE DIRETTAMENTE IL METODO?
        }

        params.putInt("limit",FRIENDS_LIMIT)
        params.putString("fields", FIELDS)


        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                FRIENDS_PATH,
                parameters,
                HttpMethod.GET,
                GraphRequest.Callback(this::handleFriendsResponse)
        ).executeAsync()
    }

    fun handleFriendsResponse(response: GraphResponse) {
        if(response.error == null) {
            val gson = Gson()
            val jsonFriends = gson
                    .fromJson(response.rawResponse, JsonElement::class.java)
                    .asJsonObject.get("data").asJsonArray

            val list = FacebookUser.listFromJson(jsonFriends)
            sender.tell(FbResponseFriendsMsg(list), self)
        } else {
            sender.tell(FbResponseFriendsMsg(response.error.errorUserMessage), self)
        }
    }

    companion object {
        fun isLoggedIn() : Boolean {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null
        }
    }
}
