package app.simone.multiplayer.view.nearby

import android.os.Handler
import android.util.Log
import app.simone.singleplayer.model.SColor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

/**
 * Created by gzano on 24/08/2017.
 */
class NearbyViewPresenter(var match: Match, var nearbyView: NearbyView) : Presenter,MatchBehaviour {


    override fun listenOnBlinkChange(sequenceIndex: Long) {
        nearbyView.updateButtonBlink(0.5F)
        val handler = nearbyView.getHandler()


        handler?.postDelayed({
            nearbyView.updateButtonBlink(1.0F)
            val cpuSeqRef = match.databaseRootReference.child(match.matchID)?.child(match.CHILD_CPUSEQUENCE)?.ref
            match.player.blinkCount++
            val nextIndex = sequenceIndex + 1
            cpuSeqRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0 != null && nextIndex < p0.childrenCount) {
                        val nextColor = p0.child(nextIndex.toString()).value.toString()
                        val map = HashMap<String, String>()
                        map["color"] = nextColor
                        map["index"] = nextIndex.toString()
                        match.databaseRootReference?.child(match.matchID)?.child("blink")?.setValue(map)
                        nearbyView.updateButtonText(match.player.blinkCount.toString())


                    }
                    if (p0 != null && nextIndex == p0.childrenCount) {
                        val nextColor = p0.child(nextIndex.toString()).value.toString()
                        val map = HashMap<String, String>()
                        map["color"] = nextColor
                        map["index"] = nextIndex.toString()
                        match.databaseRootReference?.child(match.matchID)?.child("blink")?.setValue(map)
                        nearbyView.updateButtonText(match.player.blinkCount.toString())

                    }
                    if (p0 != null && nextIndex > p0.childrenCount) {
                        nearbyView.updateButtonText(match.player.blinkCount.toString() + " your turn! ")

                        match.databaseRootReference?.child(match.matchID)?.child(match.CHILD_PLAYERSSEQUENCE)?.setValue("empty")

                    }
                }
            })


        }, 1000)

    }

    override fun onCreate() {
        match.databaseRootReference?.child(match.matchID)?.child(match.CHILD_PLAYERS)?.child(match.player.id)?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val child = dataSnapshot.child("color")
                if (child.value != null) {
                    val color = child.value.toString()
                    match.player.color = SColor.valueOf(color)
                    nearbyView.updateColor(match.player.color!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        

    }

    override fun onResume() {
        val color = match.player.color
        if (color != null) {
            nearbyView.updateColor(color)
        }
        nearbyView.listenOnCpuIndexChange()
    }

    fun updatePlayersSequence(){
        match.databaseRootReference?.child(match.matchID)?.child(match.CHILD_PLAYERSSEQUENCE)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != "playing") {
                    val count = dataSnapshot.childrenCount
                    dataSnapshot.ref.child((count + 1).toString()).setValue(match.player.color)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }


}
