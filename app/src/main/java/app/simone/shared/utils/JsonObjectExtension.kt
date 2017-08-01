package app.simone.shared.utils

import android.util.Log
import com.google.gson.JsonObject

/**
 * Created by nicola on 10/07/2017.
 */
fun JsonObject.filterFacebookUser(userID : String) : Boolean {
    val fromUser = this.get("first").asJsonObject.get("id").asString
    val toUser = this.get("second").asJsonObject.get("id").asString

    Log.d("fromUser: ",fromUser)
    Log.d("toUser: ",toUser)

    val ciao=this.get("kindOfMsg").asString
    Log.d("CIAO",ciao)
    if(ciao=="update"){
        return fromUser == userID || toUser == userID
    }else{
        return toUser == userID
    }
}

fun JsonObject.filterNotifications(userID : String) : Boolean {

    val first = this.get("first").asJsonObject
    val second = this.get("second").asJsonObject
    val fromUser = first.get("id").asString
    val toUser = second.get("id").asString

    var scoreP1=""
    var scoreP2=""

    if(first.get("score").asString!=null){
        scoreP1 = first.get("score").asString
    }
    if(second.get("score").asString!=null){
        scoreP2 = second.get("score").asString
    }

    return fromUser != userID && toUser == userID && scoreP2 == "" && scoreP1 == ""
}