package app.simone.users

import akka.actor.UntypedActor
import android.os.Bundle
import android.util.Log

import app.simone.users.model.FacebookUser
import application.App
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import messages.*
import org.json.JSONObject
import utils.Constants
import utils.Utilities

/**
 * Created by nicola on 01/07/2017.
 */

class FacebookManagerActor : UntypedActor() {

    val FRIENDS_PATH = "/me/friends"
    val SCORE_PATH = "/scores"
    val FRIENDS_LIMIT = 5000
    val FIELDS = "name,picture,id"

    val FB_WRITE_PERMISSIONS = arrayOf("publish_actions").toMutableList()

    override fun onReceive(message: Any?) {

        when ((message as IMessage).type) {
            MessageType.FB_REQUEST_FRIENDS_MSG -> {
                this.getFacebookFriends()
            }

            MessageType.FB_GET_FRIEND_SCORE_MSG -> {
                this.getFriendScore((message as FbRequestGetUserScoreMsg).friend)
            }

            MessageType.FB_UPDATE_SCORE_MSG -> {
                this.updateScore((message as FbRequestScoreUpdateMsg).score)
            }
        }
    }

    fun getFacebookFriends() {

        val parameters = Bundle()
        parameters.putInt("limit",FRIENDS_LIMIT)
        parameters.putString("fields", FIELDS)

        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME, App.getInstance().getActorSystem())

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                FRIENDS_PATH,
                parameters,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    Log.v("Sender Akka", sender.toString())
                    if(response.error == null) {
                        val jsonFriends = response.jsonObject.getJSONArray("data")
                        val list = FacebookUser.listFromJson(jsonFriends)
                        actor.tell(FbResponseFriendsMsg(list), self)
                        //sender.tell(FbResponseFriendsMsg(list), self)
                    } else {
                        actor.tell(FbResponseFriendsMsg(response.error.errorUserMessage), self)
                        //sender.tell(FbResponseFriendsMsg(response.error.errorUserMessage), self)
                    }
                }
        ).executeAsync()
    }

    fun updateScore(score: Int) {

        val parameters = Bundle()
        parameters.putString("score",score.toString())

        if(AccessToken.getCurrentAccessToken().permissions.containsAll(FB_WRITE_PERMISSIONS)) {

            GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    SCORE_PATH,
                    parameters,
                    HttpMethod.POST,
                    GraphRequest.Callback { response ->
                        if(response.error == null) {
                            sender.tell(FbResponseScoreUpdateMsg(true), self)
                        } else {
                            sender.tell(FbResponseScoreUpdateMsg(response.error.errorMessage), self)
                        }
                    }
            ).executeAsync()

        } else {
            //LoginManager.getInstance().logInWithPublishPermissions(activity, FB_WRITE_PERMISSIONS)
            sender.tell(FbResponseScoreUpdateMsg("You need to accept Facebook publish permissions"), self)
        }
    }

    fun getMyScore() {  getUserScore("me") }

    fun getFriendScore(user: FacebookUser?) {
        val id = user?.id ?: return
        getUserScore(id)
    }

    private fun getUserScore(id: String) {

        val actor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.FBVIEW_ACTOR_NAME, App.getInstance().getActorSystem())

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id + SCORE_PATH,
                null,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    if(response.error == null) {
                        val scores = response.jsonObject.getJSONArray("data")

                        if(scores.length() > 0) {
                            val score = (scores.get(0) as JSONObject).getInt("score")
                            actor.tell(FbResponseGetUserScoreMsg(score), self)
                            //sender.tell(FbResponseGetUserScoreMsg(score), self)
                        } else {
                            actor.tell(FbResponseGetUserScoreMsg("Score not available"), self)
                            //sender.tell(FbResponseGetUserScoreMsg("Score not available"), self)
                        }
                    } else {
                        //actor.tell(FbResponseGetUserScoreMsg(response.error.errorMessage), self)
                        actor.tell(FbResponseGetUserScoreMsg(0), self)
                        //sender.tell(FbResponseGetUserScoreMsg(response.error.errorMessage), self)
                    }
                }
        ).executeAsync()
    }

    companion object {
        fun isLoggedIn() : Boolean {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null
        }
    }
}
