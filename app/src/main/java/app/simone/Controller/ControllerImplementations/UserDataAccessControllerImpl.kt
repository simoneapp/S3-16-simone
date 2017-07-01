package app.simone.Controller.ControllerImplementations

import app.simone.Controller.UserDataAccessController
import app.simone.DataModel.Match
import app.simone.DataModel.Player
import io.realm.Realm
import io.realm.RealmList
import java.util.*

class UserDataAccessControllerImpl(val realm: Realm) : UserDataAccessController {


    override val FIELD_SCORE: String
        get() = "score"
    val PLAYER_NAME_DBFIELD = "name"

    override fun getMatches(playerName: String): RealmList<Match> {
        return realm.where(Player::class.java).equalTo(PLAYER_NAME_DBFIELD, playerName).findFirst().matches
    }




}