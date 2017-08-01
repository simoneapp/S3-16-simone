package app.simone.multiplayer.controller

import app.simone.multiplayer.model.PushNotification
import android.content.Context
import android.util.Log
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.model.OnlineMatch
import com.facebook.Profile
import com.google.gson.JsonObject
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import app.simone.shared.utils.filterNotifications
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import io.realm.*
import io.realm.SyncCredentials





/**
 * Created by nicola on 07/07/2017.
 */


class DataManager private constructor() {

    val AUTH_URL = "http://178.62.127.147:9080/auth"
    val REALM_URL = "realm://178.62.127.147:9080/~/default"
    val DEFAULT_LIST_ID = "80EB1620-165B-4600-A1B1-D97032FDD9A0"
    var DEFAULT_LIST_NAME = "Simone"

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

        val token = AccessToken.getCurrentAccessToken().token // a string representation of a token obtained by Facebook Login API
        val myCredentials = SyncCredentials.facebook(token)

        Realm.init(context)
        val user = SyncUser.login(myCredentials, AUTH_URL)
        val config = SyncConfiguration.Builder(user, REALM_URL)
                .disableSSLVerification()
                .schemaVersion(4)
                .name("simone.realm")
                .build()
        Realm.setDefaultConfiguration(config)


        realm = Realm.getInstance(config)

        realm?.executeTransaction { realm ->
            val fb1 = FacebookUser("ciro","ferrara")
            val fb2 = FacebookUser("rino","gattuso")
            val om = OnlineMatch(fb1,fb2,10)
            realm.copyToRealm(om)
        }
        //realm = Realm.getDefaultInstance()


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
                    println("RECORD UPDATED ON THE DB")
                   // pr.matchId=findCorrectID() TO DO
                    printValues(pr)
                    //checking matching between IDs
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

   /* fun findCorrectID(pr: OnlineMatch):Int{
        if(this.getNextId()>1){
            //I need to find the correct match ID
            return 1
            Realm.getDefaultInstance().use {
                    //to do
            }

            }
        }else{
            return 0
        }
    }*/

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
