package app.simone.multiplayer.view.nearby


import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import app.simone.R
import app.simone.shared.utils.AudioPlayer
import app.simone.singleplayer.model.SColor
import java.util.concurrent.ExecutionException
import android.widget.Toast


class NearbyGameActivity : AppCompatActivity(),DistributedView.NearbyView {


    private var handler: Handler? = null
    private var matchID = ""
    private var buttonColor: Button? = null
    private var player = AudioPlayer()
    var presenter: NearbyViewPresenter? = null
    private var enhancedPresenter:EnhancedNearbyViewPresenter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_set_up)
        buttonColor = findViewById(R.id.beautifulButton) as Button
        handler = Handler()
        matchID = intent.getStringExtra("match")
        presenter = NearbyViewPresenter(matchID, this)
        enhancedPresenter= EnhancedNearbyViewPresenter(presenter!!)

        presenter?.onCreate()
        enhancedPresenter?.onShamePlayer()

    }


    override fun getHandler(): Handler? {
        return handler

    }

    override fun updateButtonBlink(blinkTonality: Float) {
        buttonColor?.alpha = blinkTonality
        if (blinkTonality < 1.0F) {
            ring()
        }
    }

    override fun startGame() {

        presenter?.blink()
    }

    override fun showMessage(text: String) {
        Toast.makeText(this.applicationContext, text,
                Toast.LENGTH_LONG).show()
    }


    override fun updateButtonText(text: String) {
        buttonColor?.text = text

    }

    override fun updateColor(color: SColor) {

        buttonColor?.background = resources?.getDrawable(color.colorId)
    }


    @Throws(ExecutionException::class, InterruptedException::class)
    fun sendColor(view: View) {
        if (presenter?.player != null) {
            ring()
            presenter?.updatePlayersSequence()
        }


    }

    private fun ring() {
        player.play(this, presenter?.player!!.color!!.soundId)

    }


}
