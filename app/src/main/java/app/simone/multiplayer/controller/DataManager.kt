package app.simone.multiplayer.controller

import app.simone.multiplayer.model.PushNotification
import android.content.Context
import android.util.Log
import app.simone.multiplayer.model.OnlineMatch
import app.simone.multiplayer.model.FacebookUser
import com.facebook.Profile
import com.google.gson.JsonElement
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
                realm.insertOrUpdate(pr)
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

    fun saveOpponentScore(msg: JsonElement){
        val scoreP2=msg.asJsonObject.get(OnlineMatch.kSECOND).asJsonObject.get(FacebookUser.kSCORE).asString
        if(scoreP2 != "--" && scoreP2 != null){
            opponentTemporaryScore = scoreP2
        }
    }

    fun resetOpponentScore() {
        opponentTemporaryScore = ""
    }

}
