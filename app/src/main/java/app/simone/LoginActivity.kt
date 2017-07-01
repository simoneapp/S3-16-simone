package app.simone

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView

import app.simone.Controller.UserMatchController
import app.simone.Controller.ControllerImplementations.UserMatchControllerImpl
import app.simone.Controller.ControllerImplementations.ResultNotFoundException
import app.simone.Controller.ControllerImplementations.UserDataAccessControllerImpl
import app.simone.Controller.UserDataAccessController
import app.simone.DataModel.Player

import io.realm.Realm
import io.realm.RealmConfiguration


class LoginActivity : AppCompatActivity() {
    private var realm: Realm? = null
    private var controller: UserMatchController? = null
    private var editText: EditText? = null
    private var playerName: String? = null
    val PLAYER_NAME = "player_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("DBPlayers.realm").deleteRealmIfMigrationNeeded().schemaVersion(5).build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
        controller = UserMatchControllerImpl(realm!!)
        editText = findViewById(R.id.editText) as EditText

    }

    @Throws(ResultNotFoundException::class)
    fun onSubscribe(view: View) {
        playerName = editText?.text.toString().toLowerCase()

        realm?.executeTransaction { realm ->

            if (realm.where(Player::class.java).equalTo("name", playerName).findAll().isEmpty()) {
                realm.createObject(Player::class.java, playerName)
            }

        }
        controller !!. insertMatch (playerName!!,10) //di prova!!
        val textView = findViewById(R.id.loginTextView) as TextView
        val size=UserDataAccessControllerImpl(realm!!).getMatches(playerName!!).size.toString()
        val score=UserDataAccessControllerImpl(realm!!).getMatches(playerName!!).first().score.toString()
        textView.text = "$playerName , $size with score: $score"

    }

    override fun onDestroy() {
        super.onDestroy()
        realm!!.close()
    }

    fun checkPlayerInfo(view: View) {
        val intent = Intent(this, DBShowActivity::class.java)
        intent.putExtra(PLAYER_NAME, playerName)
        startActivity(intent)
    }


}
