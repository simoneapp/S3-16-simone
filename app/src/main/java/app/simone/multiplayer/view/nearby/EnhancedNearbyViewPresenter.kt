package app.simone.multiplayer.view.nearby

import android.util.Log
import app.simone.shared.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

/**
 * Created by gzano on 28/08/2017.
 */
/**
 * Decorator class that adds functionality to the basic presenter
 *
 */
class EnhancedNearbyViewPresenter(var nearbyViewPresenter: NearbyViewPresenter) : MatchBehaviour, Presenter {

    val GAME_OVER_TEXT = "GAMEOVER"
    val STATUS_VALUE_WRONG = "wrong"
    val GAME_OVER_SHAME = "what a shame you lost the game"

    /**
     * overrides basic presenter behaviour of class param nearby view presenter and set players turns or cpu turns
     */
    override fun onCreate() {
        nearbyViewPresenter.onCreate()
        nearbyViewPresenter.databaseRootReference?.child(nearbyViewPresenter.matchID)?.child(Constants.NODE_CHILD_STATUS)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0?.value.toString() == Constants.NODE_CHILD_STATUS_VALUE) {
                    nearbyViewPresenter.nearbyView.updateButtonText(Constants.BUTTON_TEXT_PLAYER_TUNR)
                }
               else{
                    nearbyViewPresenter.nearbyView.updateButtonText(Constants.BUTTON_TEXT_SIMONE_TURN)
                }
            }

        })
    }


    override fun blink() {
        nearbyViewPresenter.blink()

    }


    //Extended Behaviour
    /**
     * Extended behaviour, used to shame player that loses and shows a text
     */
    fun onShamePlayer() {

        nearbyViewPresenter.databaseRootReference?.child(nearbyViewPresenter.matchID)?.child(Constants.NODE_CHILD_STATUS)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                Log.d("TESTSHAME"," what is in here "+p0?.child(Constants.NODE_CHILD_CASE)?.value?.toString())
                val gameCase = p0?.child(Constants.NODE_CHILD_CASE)?.value?.toString()
                if (gameCase != null && gameCase == STATUS_VALUE_WRONG) {
                    val colorLose = p0?.child(Constants.NODE_CHILD_COLOR)?.value.toString()
                    if (nearbyViewPresenter.player?.color.toString() == colorLose) {
                        nearbyViewPresenter.nearbyView.updateButtonText(GAME_OVER_SHAME)
                    } else {
                        nearbyViewPresenter.nearbyView.updateButtonText(GAME_OVER_TEXT)
                    }
                }


            }

        })


    }
}