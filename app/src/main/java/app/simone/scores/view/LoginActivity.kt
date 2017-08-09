package app.simone.scores.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import app.simone.R
import app.simone.scores.controller.ResultNotFoundException


class LoginActivity : AppCompatActivity() {

    private var editText: EditText? = null
    private var playerName: String? = null
    val PLAYER_NAME="player_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editText = findViewById(R.id.editText) as EditText
    }

    @Throws(ResultNotFoundException::class)
    fun onSubscribe(view: View) {
        playerName = editText?.text.toString()
        val textView = findViewById(R.id.loginTextView) as TextView
        textView.text = playerName

    }

    fun checkPlayerInfo(view: View) {
        val intent = Intent(this, DBShowActivity::class.java)
        intent.putExtra(PLAYER_NAME, playerName)
        startActivity(intent)
    }

}
