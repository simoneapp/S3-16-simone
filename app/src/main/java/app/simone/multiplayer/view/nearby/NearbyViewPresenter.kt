package app.simone.multiplayer.view.nearby

import app.simone.shared.utils.Constants
import app.simone.singleplayer.model.SimonColor
import app.simone.singleplayer.model.SimonColorImpl
import com.facebook.Profile
import com.google.firebase.database.*

/**
 * Created by gzano on 24/08/2017.
 */
/**
 * presenter handling the rendering logic for the nearby game activity to which is assigned
 * @param matchID is the id of the match Firebase ValueEventListeners are focused on
 * @param nearbyView is the view implemented by the activity the presenter is assigned to
 */
class NearbyViewPresenter(var matchID: String, var nearbyView: DistributedView.NearbyView) : Presenter, MatchBehaviour {

    val STATUS_VALUE_PLAYING = "playing"
    val PLAYERS_SEQUENCE_VALUE_EMPTY = "empty"
    val GAME_ABOUT_TO_START_VALUE = "game is about to start"
    val BLINK_TONALITY_NORMAL = 1.0F
    val BLINK_TONALITY_TRANSPARENT = 0.5F
    var GAME_SPEED: Long = 1000
    var databaseRootReference = FirebaseDatabase.getInstance().getReference(Constants.NODE_ROOT)!!
    val playerID = Profile.getCurrentProfile().id!!
    var player: Player? = null
    var cpuSeqRef = databaseRootReference.child(matchID)?.child(Constants.NODE_CHILD_CPUSEQUENCE)?.ref


    init {
        databaseRootReference.child(matchID)?.child(Constants.NODE_CHILD_PLAYERS)?.child(playerID)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val child = dataSnapshot.child(Constants.NODE_CHILD_COLOR)

                if (child.value != null) {
                    val color = child.value.toString()

                    val playerColor = SimonColorImpl.valueOf(color)
                    player = Player(playerColor, playerID)
                    player?.resetBlinkCount()
                    nearbyView.updateColor(playerColor)
                    nearbyView.updateButtonText("color set! " + player?.color.toString())
                    nearbyView.startGame()


                }
            }
        })


    }


    override fun onCreate() {
        nearbyView.showMessage(GAME_ABOUT_TO_START_VALUE)
    }

    override fun blink() {
        databaseRootReference.child(matchID)?.child(Constants.NODE_CHILD_BLINK)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {


                if (p0?.child(Constants.NODE_CHILD_COLOR)?.value == player?.color.toString()) {
                    val index = p0.child(Constants.NODE_CHILD_INDEX).value.toString()

                    renderBlink(index.toLong())

                }
            }

        })


    }

    /**
     * update players sequence as the button is pressed
     */
    fun updatePlayersSequence() {
        databaseRootReference.child(matchID)?.child(Constants.NODE_CHILD_PLAYERSSEQUENCE)?.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.value != STATUS_VALUE_PLAYING) {
                    val count = dataSnapshot.childrenCount
                    dataSnapshot.ref.child((count + 1).toString()).setValue(player?.color)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }


    private fun renderBlink(sequenceIndex: Long) {
        nearbyView.updateButtonBlink(BLINK_TONALITY_TRANSPARENT)
        val handler = nearbyView.getHandler()


        handler?.postDelayed({
            nearbyView.updateButtonBlink(BLINK_TONALITY_NORMAL)
            if (player != null) {
                player!!.blinkCount++
            }
            val nextIndex = sequenceIndex + 1
            cpuSeqRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0 != null && nextIndex <= p0.childrenCount) {
                        val nextColor = p0.child(nextIndex.toString()).value.toString()
                        val map = HashMap<String, String>()
                        map[Constants.NODE_CHILD_COLOR] = nextColor
                        map[Constants.NODE_CHILD_INDEX] = nextIndex.toString()
                        databaseRootReference.child(matchID)?.child(Constants.NODE_CHILD_BLINK)?.setValue(map)
                       // nearbyView.updateButtonText(player?.blinkCount.toString())

                    }


                    if (p0 != null && nextIndex > p0.childrenCount) {
                      //  nearbyView.updateButtonText(player?.blinkCount.toString())
                        databaseRootReference.child(matchID)?.child(Constants.NODE_CHILD_PLAYERSSEQUENCE)?.setValue(PLAYERS_SEQUENCE_VALUE_EMPTY)
                        databaseRootReference.child(matchID)?.child(Constants.NODE_CHILD_STATUS)?.setValue(Constants.NODE_CHILD_STATUS_VALUE)

                    }
                }
            })


        }, GAME_SPEED)
    }


}
