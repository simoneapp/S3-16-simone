package app.simone.multiplayer.view.nearby

import app.simone.singleplayer.model.SimonColor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

/**
 * Created by gzano on 23/08/2017.
 */
/**
 * data class conserving player status
 * @param color is the color for the player
 * @param id is the id of the player
 */
class Player (var color:SimonColor?,val id:String){



    var blinkCount=0

    /**
     * reset the blinkcount to 0
     */
    fun resetBlinkCount(){
        blinkCount=0
    }




}