package app.simone.scores.view


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import app.simone.R
import app.simone.scores.controller.UserDataAccessControllerImpl


class DBShowActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbshow)
        val playerName = intent.getStringExtra("player_name")

        val controller = UserDataAccessControllerImpl()
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
