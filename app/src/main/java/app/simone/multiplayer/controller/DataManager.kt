package app.simone.multiplayer.controller

import android.content.Context
import android.util.Log
import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.FirebaseDatabase
import io.realm.*





/**
 * Created by nicola on 07/07/2017.
 */


class DataManager private constructor() {

    var realm: Realm? = null
    var context: Context? = null
    var database = FirebaseDatabase.getInstance().getReference("multiplayer")

    private object Holder {
        val INSTANCE = DataManager()
    }

    companion object {
        val instance: DataManager by lazy { Holder.INSTANCE }
    }

    fun setup(context: Context) {

        this.context = context
        Realm.init(context)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                        .schemaVersion(3)
                        .name("default.realm")
                .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

    }


    fun createMatch(match: OnlineMatch) {
        val matchList = database.push()
        matchList.setValue(match)
    }

    fun filterRequests(matches: List<OnlineMatch>,id: String):List<OnlineMatch>{

        val filteredMatches : MutableList<OnlineMatch> = arrayListOf()
        repeat(matches.size){ i->
            if ((matches[i].firstplayer.id==id) ||(matches[i].secondplayer.id==id))
                filteredMatches.add(matches[i])

        }

        return filteredMatches

    }

}
