package utils

import com.google.gson.JsonObject

/**
 * Created by nicola on 10/07/2017.
 */
fun JsonObject.filterFacebookUser(userID : String) : Boolean {
    val fromUser = this.get("from").asString
    val toUser = this.get("to").asString
    return fromUser != userID && toUser == userID
}