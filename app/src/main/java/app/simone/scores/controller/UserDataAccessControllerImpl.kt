package app.simone.scores.controller

import app.simone.scores.model.Match
import app.simone.scores.model.Player

class UserDataAccessControllerImpl : UserDataAccessController {

    val realm = app.simone.multiplayer.controller.DataManager.Companion.instance.realm

    override fun deleteMatches(playerName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val FIELD_SCORE: String
        get() = "score"
    val PLAYER_NAME_DBFIELD = "name"

    override fun getMatches(playerName: String): io.realm.RealmList<Match> {
        return realm?.where(Player::class.java)?.equalTo(PLAYER_NAME_DBFIELD, playerName)?.findFirst()?.matches ?: io.realm.RealmList<Match>()
    }

    override fun getDate(matchList: io.realm.RealmList<Match>, match: Match): java.util.Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}