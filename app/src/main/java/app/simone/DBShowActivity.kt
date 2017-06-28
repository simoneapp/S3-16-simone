package app.simone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.widget.TextView
import app.simone.Controller.UserDataAccessController
import app.simone.Controller.ControllerImplementations.UserDataAccessControllerImpl


import io.realm.Realm

import io.realm.RealmConfiguration


class DBShowActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbshow)
        val playerName = intent.getStringExtra("player_name")

        val config = RealmConfiguration.Builder().name("players.realm").deleteRealmIfMigrationNeeded().schemaVersion(3).build()
        Realm.setDefaultConfiguration(config)
        var realm = Realm.getDefaultInstance()
        //val controller: UserDBController = ConcreteUserDBController(Realm.getDefaultInstance())
        val controller: UserDataAccessController = UserDataAccessControllerImpl(realm)
        val textView = findViewById(R.id.DBActivityNameShow) as TextView
        textView.text = "${playerName.toUpperCase()}    ${controller.getMatchesSortedByScore(playerName)?.first()?.score}"
        val arrayData = controller.getMatches(playerName).toArray()

    }


//protected override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    realm = Realm.getDefaultInstance()
//    realmListener = object : RealmChangeListener {
//        fun onChange(realm: Realm) {
//            // ... do something with the updates (UI, etc.) ...
//        }
//    }
//    realm.addChangeListener(realmListener)
//}

}
