package app.simone.multiplayer.controller

import app.simone.multiplayer.model.PushNotification
import android.content.Context
import android.util.Log
import app.simone.multiplayer.model.FacebookUser
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
                .schemaVersion(4)
                .name("default.realm")
                .build()
        Realm.setDefaultConfiguration(config)

        realm = Realm.getDefaultInstance()
    }


    fun saveRequest(obj: JsonObject) {

        var pr = OnlineMatch.with(obj)
        try{
            opponentTemporaryScore=pr.secondPlayer.score
        }catch (e: Exception){
            System.out.println("score not available yet (P2)")
        }

        //pr.matchId=getNextId()
        printValues(pr)

        if(opponentTemporaryScore != ""){
            pr.secondPlayer.score = opponentTemporaryScore
            opponentTemporaryScore = ""
        }

        try {
            realm?.executeTransaction { realm ->
                if(pr.kindOfMsg=="insert") {
                    //new record on the DB
                    pr.matchId=getNextId()
                    realm.copyToRealm(pr)
                }else{
                    //updating an existing record

                    realm.copyToRealmOrUpdate(pr)
                }
            }
            if(obj.filterNotifications(Profile.getCurrentProfile().id.toString())) {
                PushNotification(this.context, pr.firstPlayer.name).init()
            }
        } catch (e: RealmPrimaryKeyConstraintException) {
            Log.d("DB", "The value is already in the database!")
        }
    }

    fun saveRequestLocally(pr: OnlineMatch) {


        if(opponentTemporaryScore != ""){
            pr.secondPlayer.score = opponentTemporaryScore
            opponentTemporaryScore = ""
        }


        try {
            realm?.executeTransaction { realm ->
                if(pr.kindOfMsg=="insert") {
                    //new record on the DB
                    pr.matchId=getNextId()
                    printValues(pr)
                    realm.copyToRealm(pr)
                }else{
                    //updating an existing record
                    realm.copyToRealmOrUpdate(pr)
                }
            }
        } catch (e: RealmPrimaryKeyConstraintException) {
            Log.d("DB", "The value is already in the database!")
        }
    }

    fun printValues(pr: OnlineMatch){
        Log.d("PLAYERONE1",pr.firstPlayer.name+" "+pr.firstPlayer.score)
        Log.d("PLAYERONE2",pr.secondPlayer.name+" "+pr.secondPlayer.score)
        Log.d("PLAYERONE_MATCH",""+pr.matchId)
    }


    fun getNextId():Int{
        var id = realm!!.where(OnlineMatch::class.java).max("matchId")
        if(id==null){
            return 0
        }else{
            id=id.toInt()
            return id+1
        }
    }

    fun getPendingRequests(): RealmResults<OnlineMatch> {
        return realm!!.where(OnlineMatch::class.java).findAll()
    }


    fun resetOpponentScore() {
        opponentTemporaryScore = ""
    }

}
