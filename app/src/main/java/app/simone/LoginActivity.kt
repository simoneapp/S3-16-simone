package app.simone

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView

import app.simone.Controller.UserInputController
import app.simone.Controller.ControllerImplementations.LoginController
import app.simone.Controller.ControllerImplementations.ResultNotFoundException

import io.realm.Realm
import io.realm.RealmConfiguration


class LoginActivity : AppCompatActivity() {
    private var realm: Realm? = null
    private var controller: UserInputController? = null
    private var editText: EditText? = null
    private var playerName: String? = null
    val PLAYER_NAME="player_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("players.realm").deleteRealmIfMigrationNeeded().schemaVersion(3).build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
        controller = LoginController(realm!!)
        editText = findViewById(R.id.editText) as EditText

    }

    @Throws(ResultNotFoundException::class)
    fun onSubscribe(view: View) {
        playerName = editText?.text.toString()
        controller!!.insertPlayer(playerName!!)
        controller!!.insertMatch(playerName!!)
        val textView = findViewById(R.id.loginTextView) as TextView
        textView.text = playerName

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
