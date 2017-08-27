package app.simone.multiplayer.controller

import android.content.Context
import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.FirebaseDatabase

/**
 * This class handles all data sync with Firebase concerning the Instant Multiplayer mode.
 *
 * @author Giacomo
 * @author Nicola
 */


class DataManager private constructor() {

    var context: Context? = null
    val database = FirebaseDatabase.getInstance().getReference(MULTIPLAYER)!!

    private object Holder {
        val INSTANCE = DataManager()
    }

    companion object {
        val instance: DataManager by lazy { Holder.INSTANCE }
        const val MULTIPLAYER = "multiplayer"
    }

    /**
     * This method creates a new match on Firebase. Each match is identified by a key, which is immediatly generated
     * using the 'push' function (line 34). Then the match is stored into the db.
     *
     * @param match This is the match to be stored into the db.
     * @return int Firebase automatically assigns a key to the new match.
     */
    fun createMatch(match: OnlineMatch):String {
        val matchList = database.push()
        matchList.setValue(match)
        return matchList.key
    }

    /**
     * This method is used to filter all the match requests of a specific user.
     *
     * @param matches This is the list of all the matches stored on Firebase.
     * @param id This is the user ID
     * @return ArrayList<OnlineMatch> This is the list of all the matches that involve the given User ID.
     */
    fun filterRequests(matches: ArrayList<OnlineMatch>,id: String):ArrayList<OnlineMatch>{
        val filteredArray: ArrayList<OnlineMatch> = arrayListOf()

        repeat(matches.size){ i->
            if ((matches[i].firstplayer.id==id) ||(matches[i].secondplayer.id==id)){
                //keep it
                filteredArray.add(matches[i])
            }

        }
        return filteredArray
    }

}
