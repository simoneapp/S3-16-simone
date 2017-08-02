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
    var opponentTemporaryScore = ""
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


    fun printValues(pr: OnlineMatch){
        Log.d("PLAYERONE1",pr.firstplayer.name+" "+pr.firstplayer.score)
        Log.d("PLAYERONE2",pr.secondplayer.name+" "+pr.secondplayer.score)
    }


    fun getPendingRequests(): OnlineMatch {
        return database.child("multiplayer").limitToFirst(0) as OnlineMatch
    }


    fun createMatch(match: OnlineMatch) {
        val matchList = database.push()
        matchList.setValue(match)
    }

}
