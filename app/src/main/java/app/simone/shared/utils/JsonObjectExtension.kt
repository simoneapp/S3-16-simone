package app.simone.shared.utils

import com.google.gson.JsonObject

/**
 * Created by nicola on 10/07/2017.
 */
fun JsonObject.filterFacebookUser(userID : String) : Boolean {
    val fromUser = this.get("idP1").asString
    val toUser = this.get("idP2").asString
    return fromUser == userID || toUser == userID
}

fun JsonObject.filterNotifications(userID : String) : Boolean {
    val fromUser = this.get("idP1").asString
    val toUser = this.get("idP2").asString
    val scoreP1 = this.get("scoreP1").asString
    val scoreP2 = this.get("scoreP2").asString
    return fromUser != userID && toUser == userID && scoreP2 == "--" && scoreP1 == "--"
}