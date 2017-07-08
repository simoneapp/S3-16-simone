package app.simone.users.model

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by nicola on 23/06/2017.
 */

class FacebookFriend(json : JSONObject) {

    var name : String? = null
    var picture : FacebookPicture? = null
    var friendId : String? = null
    var invitable : Boolean = false

    init {
        this.name = json.getString("name")
        this.picture = FacebookPicture(json.getJSONObject("picture").getJSONObject("data"))
        this.friendId = json.getString("id")
    }

    companion object {
        fun listFromJson(json : JSONArray) : List<FacebookFriend> {

            val objectFriends = ArrayList<FacebookFriend>()

            (0..json.length()-1)
                    .map { json.get(it) as JSONObject }
                    .mapTo(objectFriends) { FacebookFriend(it) }

            return objectFriends
        }
    }
}