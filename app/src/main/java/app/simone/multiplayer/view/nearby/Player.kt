package app.simone.multiplayer.view.nearby

import app.simone.singleplayer.model.SColor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

/**
 * Created by gzano on 23/08/2017.
 */
class Player (var color:SColor?,val id:String){



    var blinkCount=0

    fun resetBlinkCount(){
        blinkCount=0
    }



}