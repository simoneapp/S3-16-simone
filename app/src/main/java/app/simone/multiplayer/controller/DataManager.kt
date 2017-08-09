package app.simone.multiplayer.controller

import android.content.Context
import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.FirebaseDatabase



/**
 * Created by nicola on 07/07/2017.
 */


class DataManager private constructor() {

    var context: Context? = null
    var database = FirebaseDatabase.getInstance().getReference("multiplayer")

    private object Holder {
        val INSTANCE = DataManager()
    }

    companion object {
        val instance: DataManager by lazy { Holder.INSTANCE }
    }


    fun createMatch(match: OnlineMatch):String {
        val matchList = database.push()
        matchList.setValue(match)
        return matchList.key
    }

    fun filterRequests(matches: ArrayList<OnlineMatch>,id: String):ArrayList<OnlineMatch>{

        var filteredArray: ArrayList<OnlineMatch> = arrayListOf()

        repeat(matches.size){ i->
            if ((matches[i].firstplayer.id==id) ||(matches[i].secondplayer.id==id)){
                //keep it
                filteredArray.add(matches[i])
            }

        }
        return filteredArray
    }

}
