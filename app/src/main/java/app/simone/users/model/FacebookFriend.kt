package app.simone.users.model

import org.json.JSONObject

/**
 * Created by nicola on 23/06/2017.
 */

class FacebookFriend(val json : JSONObject) {

    var name : String? = null
    var picture : FacebookPicture? = null
    var friendId : String? = null

    init {
        this.name = json.getString("name")
        this.picture = FacebookPicture(json.getJSONObject("picture").getJSONObject("data"))
        this.friendId = json.getString("id")
    }
}