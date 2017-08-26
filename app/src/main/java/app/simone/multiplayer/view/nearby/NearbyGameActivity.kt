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

class NearbyGameActivity : AppCompatActivity(), NearbyView {

    private var handler: Handler? = null
    private var playerID = ""
    private var playerColor: SColor? = null
    private var matchID = ""
    private var db: DatabaseReference? = null
    private val CHILD_PLAYERS = "users"
    private val NODE_REF_ROOT = "matches"
    private val CHILD_PLAYERSSEQUENCE = "playersSequence"
    private val CHILD_CPUSEQUENCE = "cpuSequence"
    private val CHILD_INDEX = "index"
    private var cpuSequenceRef: DatabaseReference? = null
    private var buttonColor: Button? = null
    private var blinkCount = 0
    private var player = AudioPlayer()
    private var presenter: NearbyViewPresenter? = null
    var context: NearbyView = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_set_up)
        db = FirebaseDatabase.getInstance().getReference(NODE_REF_ROOT)
        buttonColor = findViewById(R.id.beautifulButton) as Button

        matchID = intent.getStringExtra("match")
        playerID = Profile.getCurrentProfile().id




    }

    public override fun onResume() {
        super.onResume()

        db?.child(matchID)?.child(CHILD_PLAYERS)?.child(playerID)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val child = dataSnapshot.child("color")

                if (child.value != null) {
                    val color = child.value.toString()
                    playerColor = SColor.valueOf(color)
                    val pl = Player(playerColor, playerID)
                    val match = Match(matchID, pl)
                    presenter = NearbyViewPresenter(match, context)
                    presenter?.onResume()
                }
            }
        })
    }

    override fun getHandler(): Handler? {
        return handler

    }

    override fun updateButtonBlink(blinkTonality: Float) {
        buttonColor?.alpha = blinkTonality
        player.play(this, playerColor!!.soundId)
    }

    override fun listenOnCpuIndexChange() {


        db?.child(matchID)?.child("blink")?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                Log.d("BLINKLISTENERTEST", "I have changed blink")
                Log.d("NEARBYVIEW", " color: " + p0?.child("color")?.value + " index " + p0?.child("index")?.value.toString())

                if (p0?.child("color")?.value == playerColor.toString()) {
                    val index = p0?.child("index").value.toString()

                    handler = Handler()

                    presenter?.listenOnBlinkChange(index.toLong())
                }
            }

        })


    }

    override fun updateButtonText(text: String) {
        buttonColor?.text = text
    }

    override fun updateColor(color: SColor) {

        buttonColor?.background = resources?.getDrawable(color.colorId)
    }


    @Throws(ExecutionException::class, InterruptedException::class)
    fun sendColor(view: View) {

        if (playerColor != null) {
            player.play(this, playerColor!!.soundId)
            presenter?.updatePlayersSequence()
        }


    }




}
