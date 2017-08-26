package app.simone.multiplayer.view.nearby

import android.util.Log
import app.simone.scores.google.AchievementCallback
import app.simone.singleplayer.model.SColor
import com.google.android.gms.internal.db
import com.google.firebase.database.*
import org.jetbrains.annotations.NotNull

/**
 * Created by gzano on 23/08/2017.
 */
class Match(var matchID: String, var player: Player) : MatchBehaviour {


    val CHILD_PLAYERS = "users"
    private val NODE_REF_ROOT = "matches"
    val CHILD_PLAYERSSEQUENCE = "playersSequence"
    val CHILD_CPUSEQUENCE = "cpuSequence"
    val CHILD_INDEX = "index"
    var updatedString = ""
    val databaseRootReference: DatabaseReference =FirebaseDatabase.getInstance().getReference(NODE_REF_ROOT)

    override fun listenOnCpuSequenceIndexChange(cpuIndex: Long) {
        val cpuSequenceRef = databaseRootReference?.child(matchID)?.child(CHILD_CPUSEQUENCE)?.ref

        cpuSequenceRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("TESTOBSERVE","I'm here")
                val playerColor = player.color
                var blinkCount = player.blinkCount
                val childrenCount = dataSnapshot.childrenCount
                for (child in dataSnapshot.children) {
                    val colorSequence = child.getValue(String::class.java)
                    val index = child.key.toLong()
                    if (colorSequence != null
                            && playerColor == SColor.valueOf(colorSequence)
                            && cpuIndex == index) {
                        ++blinkCount
                        when (index) {

                            childrenCount ->
                                updatedString = playerColor.toString() + " " + blinkCount + " your turn!"
                            else -> {
                                val newIndex = index + 1
                                updatedString = playerColor.toString() + " " + blinkCount
                                databaseRootReference?.child(matchID)?.child(CHILD_INDEX)?.setValue(newIndex.toString())
                            }
                        }


                    } else {
                        updatedString = playerColor.toString() + " " + blinkCount
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    fun setPlayerColor() {

        databaseRootReference?.child(matchID)?.child(CHILD_PLAYERS)?.child(player.id)?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val child = dataSnapshot.child("color")
                Log.d("TESTDB", "db value: " + dataSnapshot.child("color").value.toString())
                if (child.value != null) {
                    val color = child.value.toString()
                    player.color = SColor.valueOf(color)
                    Log.d("COLORTEST", "color is: " + player.color)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}