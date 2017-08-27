package app.simone.multiplayer.view.nearby

import android.util.Log
import app.simone.singleplayer.model.SColor
import com.facebook.Profile
import com.google.firebase.database.*

/**
 * Created by gzano on 24/08/2017.
 */
class NearbyViewPresenter(var matchID: String, var nearbyView: NearbyView) : Presenter, MatchBehaviour {


    val CHILD_PLAYERSSEQUENCE = "playersSequence"
    val CHILD_CPUSEQUENCE = "cpuSequence"
    private val NODE_REF_ROOT = "matches"
    private val CHILD_PLAYERS = "users"
    val databaseRootReference = FirebaseDatabase.getInstance().getReference(NODE_REF_ROOT)
    val playerID = Profile.getCurrentProfile().id
    var player: Player? = null
    val cpuSeqRef = databaseRootReference?.child(matchID)?.child(CHILD_CPUSEQUENCE)?.ref


    init {


        databaseRootReference?.child(matchID)?.child(CHILD_PLAYERS)?.child(playerID)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val child = dataSnapshot.child("color")

                if (child.value != null) {
                    val color = child.value.toString()

                    val playerColor = SColor.valueOf(color)
                    player = Player(playerColor, playerID)
                    player?.resetBlinkCount()
                    nearbyView.updateColor(playerColor)
                    nearbyView.updateButtonText("color set! " + player?.color.toString())
                    nearbyView.startGame()


                }
            }
        })
    }

    override fun listenOnBlinkChange() {
        databaseRootReference?.child(matchID)?.child("blink")?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {


                if (p0?.child("color")?.value == player?.color.toString()) {
                    val index = p0?.child("index").value.toString()

                    renderBlink(index.toLong())

                }
            }

        })


    }

    override fun onCreate() {


        val text = "game is about to start"
        nearbyView.showMessage(text)
        checkIfWrong()
    }


    fun updatePlayersSequence() {
        Log.d("CHECKREFNULL", " players sequence ref " + (databaseRootReference?.child(matchID)?.child(CHILD_PLAYERSSEQUENCE)?.ref == null).toString())
        databaseRootReference?.child(matchID)?.child(CHILD_PLAYERSSEQUENCE)?.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("TESTBUTTON", " I'm in data change " + dataSnapshot.value)

                if (dataSnapshot.value != "playing") {
                    val count = dataSnapshot.childrenCount
                    dataSnapshot.ref.child((count + 1).toString()).setValue(player?.color)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }



    override fun increaseSpeed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun checkIfWrong(){
        databaseRootReference?.child(matchID)?.child("status")?.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0?.value?.toString()=="wrong"){
                    nearbyView.updateButtonText("GAMEOVER")
                }
            }

        })
    }

    private fun renderBlink(sequenceIndex: Long) {
        Log.d("TESTBLINK"," blink count "+player?.blinkCount)
        nearbyView.updateButtonBlink(0.5F)
        val handler = nearbyView.getHandler()


        handler?.postDelayed({
            nearbyView.updateButtonBlink(1.0F)
            if (player != null) {
                player!!.blinkCount++
            }
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
                        databaseRootReference?.child(matchID)?.child("blink")?.setValue(map)
                        nearbyView.updateButtonText(player?.blinkCount.toString())


                    }
                    if (p0 != null && nextIndex == p0.childrenCount) {
                        val nextColor = p0.child(nextIndex.toString()).value.toString()
                        val map = HashMap<String, String>()
                        map["color"] = nextColor
                        map["index"] = nextIndex.toString()
                        databaseRootReference?.child(matchID)?.child("blink")?.setValue(map)

                    }
                    if (p0 != null && nextIndex > p0.childrenCount) {
                        nearbyView.updateButtonText(player?.blinkCount.toString())
                        databaseRootReference?.child(matchID)?.child(CHILD_PLAYERSSEQUENCE)?.setValue("empty")

                    }
                }
            })


        }, 1000)
    }

    private fun setPlayersTurn() {

        databaseRootReference.child("blink").child("index").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                val blinkIndex=p0?.value.toString()
                cpuSeqRef?.addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot?) {
                        val childrenCount=p0?.childrenCount
                        if(blinkIndex.toLong()==childrenCount){
                            nearbyView.showMessage("PLAYERS TURN!")
                        }

                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })


            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
    }



}
