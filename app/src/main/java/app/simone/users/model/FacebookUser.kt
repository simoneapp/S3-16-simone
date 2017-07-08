package app.simone.users.model

import android.hardware.camera2.params.Face
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

/**
 * Created by nicola on 23/06/2017.
 */

class FacebookUser : Serializable {

    var id : String? = null
    var name : String? = null
    var picture : FacebookPicture? = null
    var invitable : Boolean = false

    companion object {

        fun with(json : JSONObject) : FacebookUser {
            val user = FacebookUser()
            user.name = json.getString("name")
            user.picture = FacebookPicture(json.getJSONObject("picture").getJSONObject("data"))
            user.id = json.getString("id")
            return user
        }

        fun with(id: String, name: String) : FacebookUser {
            val user = FacebookUser()
            user.id = id
            user.name = name
            return user
        }

        fun listFromJson(json : JSONArray) : List<FacebookUser> {

            val objectFriends = ArrayList<FacebookUser>()

            (0..json.length()-1)
                    .map { json.get(it) as JSONObject }
                    .mapTo(objectFriends) { FacebookUser.with(it) }

            return objectFriends
        }
    }
}