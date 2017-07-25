package app.simone.multiplayer.controller

import app.simone.multiplayer.model.PushNotification
import android.content.Context
import android.util.Log
import app.simone.multiplayer.model.OnlineMatch
import com.facebook.Profile
import com.google.gson.JsonObject
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import app.simone.shared.utils.filterNotifications

/**
 * Created by nicola on 07/07/2017.
 */


class DataManager private constructor() {

    var realm: Realm? = null
    var context: Context? = null
    var opponentTemporaryScore = ""

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
                .build()
        Realm.setDefaultConfiguration(config)

        realm = Realm.getDefaultInstance()
    }

    fun saveRequest(obj: JsonObject) {

        var pr = OnlineMatch.with(obj)

        if(opponentTemporaryScore != ""){
            pr.secondPlayer.score = opponentTemporaryScore
            opponentTemporaryScore = ""
        }

        try {
            realm?.executeTransaction { realm ->
                val fbU1=pr.firstPlayer
                val fbU2=pr.secondPlayer

                val otherMatches = realm.where(OnlineMatch::class.java).findAll().filter { it ->
                    it.secondPlayer.id == fbU2.id
                }

                var onlineMatch : OnlineMatch?

                if(otherMatches.size == 0){
                    onlineMatch=realm.createObject(OnlineMatch::class.java,fbU2.id)
                } else {
                    onlineMatch = otherMatches.first()
                }

                val first = realm.copyToRealm(pr.firstPlayer)
                val second = realm.copyToRealm(pr.secondPlayer)

                onlineMatch.firstPlayer = first
                onlineMatch.secondPlayer = second

            }

            if(obj.filterNotifications(Profile.getCurrentProfile().id.toString())) {
                PushNotification(this.context, pr.firstPlayer.name).init()
            }
        } catch (e: RealmPrimaryKeyConstraintException) {
            Log.d("DB", "The value is already in the database!")
        }
    }


    fun getPendingRequests(): RealmResults<OnlineMatch> {
        return realm!!.where(OnlineMatch::class.java).findAll()
    }


    fun resetOpponentScore() {
        opponentTemporaryScore = ""
    }

}
