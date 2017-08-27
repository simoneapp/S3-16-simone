package app.simone.multiplayer.view.nearby


import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import app.simone.R
import app.simone.shared.utils.AudioPlayer
import app.simone.singleplayer.model.SColor
import com.facebook.Profile
import com.google.firebase.database.*
import java.util.concurrent.ExecutionException
import android.widget.Toast


class NearbyGameActivity : AppCompatActivity(), NearbyView {


    private var handler: Handler? = null
    private var playerID = ""
    private var playerColor: SColor? = null
    private var matchID = ""
    private var buttonColor: Button? = null
    private var player = AudioPlayer()
    private var presenter: NearbyViewPresenter? = null
    var context: NearbyView = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_set_up)
        buttonColor = findViewById(R.id.beautifulButton) as Button
        handler = Handler()
        matchID = intent.getStringExtra("match")
        playerID = Profile.getCurrentProfile().id
        presenter = NearbyViewPresenter(matchID, this)
        presenter?.onCreate()

    }


    override fun getHandler(): Handler? {
        return handler

    }

    override fun updateButtonBlink(blinkTonality: Float) {
        buttonColor?.alpha = blinkTonality
        if (blinkTonality < 1.0F) {
            Log.d("RINGTEST"," blink tonality: "+blinkTonality.toString())
            ring()
        }
    }

    override fun startGame() {

        presenter?.listenOnBlinkChange()
    }

    override fun showMessage(text: String) {
        Toast.makeText(this.applicationContext, text,
                Toast.LENGTH_LONG).show()
    }


    override fun updateButtonText(text: String) {
        buttonColor?.text = text
       // player.play(this, presenter?.player!!.color!!.soundId)

    }

    override fun updateColor(color: SColor) {

        buttonColor?.background = resources?.getDrawable(color.colorId)
    }


    @Throws(ExecutionException::class, InterruptedException::class)
    fun sendColor(view: View) {
        Log.d("BUTTONTEST", "button pressed")
        if (presenter?.player != null) {
            ring()
            presenter?.updatePlayersSequence()
        }


    }

    private fun ring() {
        player.play(this, presenter?.player!!.color!!.soundId)

    }


}
