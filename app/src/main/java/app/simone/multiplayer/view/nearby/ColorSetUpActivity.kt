package app.simone.multiplayer.view.nearby


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import app.simone.R
import app.simone.singleplayer.model.SColor
import com.facebook.Profile
import com.google.firebase.database.*
import java.util.concurrent.ExecutionException

class ColorSetUpActivity : AppCompatActivity() {

    private var playerID = ""
    private var matchID = ""
    private var playerColor: SColor? = null
    private val sequenceIndex = ""
    private var db: DatabaseReference? = null
    private val CHILD_PLAYERS = "users"
    private val NODE_REF_ROOT = "matches"
    private val CHILD_PLAYERSSEQUENCE = "playersSequence"
    private val CHILD_CPUSEQUENCE = "cpuSequence"
    private val CHILD_INDEX = "index"

    private var buttonColor: Button? = null
    private var blinkCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_set_up)
        db = FirebaseDatabase.getInstance().getReference(NODE_REF_ROOT)
        buttonColor = findViewById(R.id.beautifulButton) as Button

        matchID = intent.getStringExtra("match")
        playerID = Profile.getCurrentProfile().id

        setColor()
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun sendColor(view: View) {
        db?.child(matchID)?.child(CHILD_PLAYERSSEQUENCE)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.childrenCount
                dataSnapshot.ref.child((count + 1).toString()).setValue(playerColor)
            }

            override fun onCancelled(databaseError: DatabaseError) { }
        })

    }

    public override fun onResume() {
        super.onResume()
        blink()
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

                /*
                for (child in dataSnapshot.children) {
                    val playerInfo = child.value as HashMap<String, Any>?
                    Log.d("PROVA", playerInfo?.toString())
                    //playerID = child.key
                    Log.d("PLAYERID", playerID)

                    if (playerInfo != null && !(playerInfo["taken"] as Boolean)) {
                        playerOwnColor = SColor.valueOf(playerInfo["color"] as String)
                        render()
                        db?.child(matchID)?.child(CHILD_PLAYERS)?.child(playerID)?.child("taken")?.setValue(true)
                        buttonColor?.text = playerID + " " + playerOwnColor
                        Log.d("CHILDSNAPSHOT", "changing value")
                        break
                    }
                }*/
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun render() {
        if(playerColor != null) {
            buttonColor?.background = resources?.getDrawable(playerColor!!.colorId)
        }
    }

    private fun blink() {
        val cpuSequenceRef = db?.child(matchID)?.child(CHILD_CPUSEQUENCE)?.ref
        db?.child(matchID)?.child(CHILD_INDEX)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("PROVAINDEX_BLINK", dataSnapshot.getValue(String.class));
                val cpuSequenceIndex = dataSnapshot.getValue(String::class.java)
                cpuSequenceRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val childrenCount = dataSnapshot.childrenCount.toString()
                        for (child in dataSnapshot.children) {
                            val colorSequence = child.getValue(String::class.java)
                            val index = child.key
                            //Log.d("CHILDLOOP", colorSequence + " " + index);

                            if (colorSequence != null && playerColor == SColor.valueOf(colorSequence) && cpuSequenceIndex == index) {
                                ++blinkCount
                                //Log.d("BLINKING", String.valueOf(blinkCount));
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

            override fun onCancelled(databaseError: DatabaseError) { }
        })

    }

    companion object {
        private val handler: android.os.Handler? = null
    }
}
