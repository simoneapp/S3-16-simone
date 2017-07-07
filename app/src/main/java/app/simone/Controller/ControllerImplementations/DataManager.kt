package app.simone.Controller.ControllerImplementations

import PubNub.PushNotification
import android.content.Context
import android.util.Log
import app.simone.DataModel.PendingRequest
import com.google.gson.JsonObject
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.exceptions.RealmPrimaryKeyConstraintException

/**
 * Created by nicola on 07/07/2017.
 */


class DataManager private constructor() {

    var realm: Realm? = null
    var context: Context? = null

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
        Log.d("PR JSON", obj.toString())
        val pr = PendingRequest.fromJson(obj)
        Log.d("PR OBJ", pr.toString())

        try {
            realm?.executeTransaction { realm ->
                realm.copyToRealm(pr)
            }
            PushNotification(this.context, pr.name).init()
        } catch (e: RealmPrimaryKeyConstraintException) {
            Log.d("DB", "The value is already in the database!")
        }
    }

    fun getPendingRequests(): RealmResults<PendingRequest> {
        return realm!!.where(PendingRequest::class.java).findAll()
    }

}
