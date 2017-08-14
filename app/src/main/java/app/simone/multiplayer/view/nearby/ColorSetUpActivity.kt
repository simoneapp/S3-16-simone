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

class ColorSetUpActivity : AppCompatActivity() {

    private var playerID = ""
    private var matchID = ""
    private var playerColor: SColor? = null
    private var db: DatabaseReference? = null
    private val CHILD_PLAYERS = "users"
    private val NODE_REF_ROOT = "matches"
    private val CHILD_PLAYERSSEQUENCE = "playersSequence"
    private val CHILD_CPUSEQUENCE = "cpuSequence"
    private val CHILD_INDEX = "index"
    private var cpuSequenceRef : DatabaseReference? = null
    private var buttonColor: Button? = null
    private var blinkCount = 0
    private var player = AudioPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_set_up)
        db = FirebaseDatabase.getInstance().getReference(NODE_REF_ROOT)
        buttonColor = findViewById(R.id.beautifulButton) as Button

        matchID = intent.getStringExtra("match")
        playerID = Profile.getCurrentProfile().id

        setColor()
        observePlayersSequence()
    }

    public override fun onResume() {
        super.onResume()
        blink()
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun sendColor(view: View) {

        if(playerColor != null) {
            player.play(this, playerColor!!.soundId)
        }

        db?.child(matchID)?.child(CHILD_PLAYERSSEQUENCE)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.value != "playing") {
                    val count = dataSnapshot.childrenCount
                    dataSnapshot.ref.child((count + 1).toString()).setValue(playerColor)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) { }
        })

    }

    private fun setColor() {
        Log.d("PROVA", "executing query")
        db?.child(matchID)?.child(CHILD_PLAYERS)?.child(playerID)?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val child = dataSnapshot.child("color")

                if(child.value != null) {
                    val color = child.value.toString()
                    playerColor = SColor.valueOf(color)
                    render()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) { }
        })
    }

    private fun observePlayersSequence() {

        /*
        db?.child(matchID)?.child(CHILD_PLAYERSSEQUENCE)?.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val value = p0?.value

                if(value == null || value == "wrong") {

                }

            }

        })*/

        db?.child(matchID)?.child("blink")?.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0?.child("color")?.value == playerColor.toString()) {
                    renderBlink(p0.child("index")?.value.toString())
                }
            }

        })
    }

    private fun renderBlink(sequenceIndex: String) {
        buttonColor?.alpha = 0.5F
        player.play(this, playerColor!!.soundId)

        val handler = Handler()
        handler.postDelayed(Runnable {
            buttonColor?.alpha = 1.0F

            //db?.child(matchID)?.child("blink")?.child("status")?.setValue("replied")

            val nextIndex = sequenceIndex.toLong() + 1

            val cpuSeqRef = db?.child(matchID)?.child(CHILD_CPUSEQUENCE)?.ref

            cpuSeqRef?.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if(p0 != null && nextIndex <= p0.childrenCount) {
                        val nextColor = p0?.child(nextIndex.toString()).value.toString()

                        val map = HashMap<String,String>()
                        map["color"] = nextColor
                        map["index"] = nextIndex.toString()
                        db?.child(matchID)?.child("blink")?.setValue(map)

                        if(nextIndex == p0.childrenCount) {
                            db?.child(matchID)?.child(CHILD_PLAYERSSEQUENCE)?.setValue("empty")
                        }
                    }
                }
            })

        }, 500)
    }

    private fun render() {
        if(playerColor != null) {
            buttonColor?.background = resources?.getDrawable(playerColor!!.colorId)
        }
    }

    private fun blink() {
        cpuSequenceRef = db?.child(matchID)?.child(CHILD_CPUSEQUENCE)?.ref
        db?.child(matchID)?.child(CHILD_INDEX)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("PROVAINDEX_BLINK", dataSnapshot.getValue(String.class));
                val index = dataSnapshot.getValue(String::class.java)
                if(index != null) {
                    observeCpuSequence(index)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) { }
        })

    }

    private fun observeCpuSequence(cpuIndex: String) {
        cpuSequenceRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val childrenCount = dataSnapshot.childrenCount.toString()
                for (child in dataSnapshot.children) {
                    val colorSequence = child.getValue(String::class.java)
                    val index = child.key
                    //Log.d("CHILDLOOP", colorSequence + " " + index);

                    if (colorSequence != null
                            && playerColor == SColor.valueOf(colorSequence)
                            && cpuIndex == index) {
                        ++blinkCount

                        if (index == childrenCount) {
                            buttonColor?.text = playerColor.toString() + " " + blinkCount + " your turn!"
                        } else {
                            val newIndex = Integer.parseInt(index) + 1
                            buttonColor?.text = playerColor.toString() + " " + blinkCount
                            db?.child(matchID)?.child(CHILD_INDEX)?.setValue(newIndex.toString())
                        }
                    } else {
                        buttonColor?.text = playerColor.toString() + " " + blinkCount
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) { }
        })
    }

    companion object {
        private val handler: android.os.Handler? = null
    }
}
