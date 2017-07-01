package app.simone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListAdapter

import android.widget.TextView
import app.simone.Controller.UserDataAccessController
import app.simone.Controller.ControllerImplementations.UserDataAccessControllerImpl
import app.simone.DataModel.Match
import app.simone.DataModel.Player
import io.realm.*


class DBShowActivity : AppCompatActivity() {

    private var realm: Realm? = null
    private var textView: TextView? = null
    private var controller: UserDataAccessController? = null
    private var realmMatches: RealmResults<Match>? = null
    private var playerName: String? = null
    var mRecyclerView: RecyclerView? = null
    var mLinearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbshow)
        playerName = intent.getStringExtra("player_name")



        Realm.init(this)
        val config = RealmConfiguration.Builder().name("DBPlayers.realm").deleteRealmIfMigrationNeeded().schemaVersion(5).build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
        controller = UserDataAccessControllerImpl(realm!!)
        realmMatches = controller?.getMatchesSortedByScore(playerName!!)

        Log.d("REALM TEST", playerName + " " + realmMatches?.size.toString())
        textView = findViewById(R.id.textView3) as TextView
        textView?.text = """$playerName ${realmMatches?.size.toString()}"""

        mRecyclerView = findViewById(R.id.recyclerView) as RecyclerView
        mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerView?.layoutManager=mLinearLayoutManager

    }

    fun deleteMatches(view: View) {


    }
}