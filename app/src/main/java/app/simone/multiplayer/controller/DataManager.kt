package app.simone.multiplayer.controller

import android.content.Context
import app.simone.multiplayer.model.OnlineMatch
import app.simone.singleplayer.model.SimonColor
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



/**
 * Created by nicola on 07/07/2017.
 */


class DataManager private constructor() {

    var context: Context? = null
    val database : DatabaseReference? = FirebaseDatabase.getInstance().getReference("multiplayer")

    private object Holder {
        val INSTANCE = DataManager()
    }

    companion object {
        val instance: DataManager by lazy { Holder.INSTANCE }
    }


    fun createMatch(match: OnlineMatch):String {
        if(database == null) {
            return "";
        }

        val matchList = database?.push()
        matchList.setValue(match)
        return matchList.key
    }

    fun filterRequests(matches: ArrayList<OnlineMatch>,id: String):ArrayList<OnlineMatch>{
        val filteredArray: ArrayList<OnlineMatch> = arrayListOf()

        repeat(matches.size){ i->
            if ((matches[i].firstplayer.id==id) || (matches[i].secondplayer.id==id)){
                //keep it
                filteredArray.add(matches[i])
            }

        }
        return filteredArray
    }

    fun setMultiplayerSequence(matchID: String, sequence: List<SimonColor> ){
        database?.child(matchID)?.child("sequence")?.setValue(sequence)
    }

}
