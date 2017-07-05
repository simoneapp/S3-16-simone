package app.simone.Controller.ControllerImplementations

import android.util.Log
import app.simone.Controller.UserDataController
import app.simone.DataModel.Match
import app.simone.DataModel.Player
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class LocalDataControllerImpl(val realm: Realm) : UserDataController {


    val simpleDateFormat: SimpleDateFormat
        get() = SimpleDateFormat("dd/MM")

    override val FIELD_SCORE: String
        get() = "score"
    val PLAYER_NAME_DBFIELD = "name"

    override fun getMatches(): RealmList<Match> {

        return realm.where(Player::class.java).equalTo(PLAYER_NAME_DBFIELD, Constants.DEFAULT_PLAYER).findFirst().matches
    }

    override fun insertMatch(score: Int, gameType: Int) {
        realm.executeTransaction { realm ->
            val player = realm.where(Player::class.java).equalTo(PLAYER_NAME_DBFIELD, Constants.DEFAULT_PLAYER).findFirst()
            val match = realm.createObject(Match::class.java)
            match.score = score
            match.gameDate = simpleDateFormat.format(Date())
            match.gameType=gameType
            player.matches.add(match)
            Log.d("DATE TEST ", player.matches.size.toString() + match.gameDate.toString())

        }

    }


}


