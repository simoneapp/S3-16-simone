package app.simone.Controller.ControllerImplementations

import app.simone.DataModel.Match
import io.realm.RealmList
import java.util.*

class UserDataAccessControllerImpl : app.simone.Controller.UserDataAccessController {

    val realm = DataManager.instance.realm

    override fun deleteMatches(playerName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val FIELD_SCORE: String
        get() = "score"
    val PLAYER_NAME_DBFIELD = "name"

    override fun getMatches(playerName: String): RealmList<Match> {
        return realm?.where(app.simone.DataModel.Player::class.java)?.equalTo(PLAYER_NAME_DBFIELD, playerName)?.findFirst()?.matches ?: RealmList<Match>()
    }

    override fun getDate(matchList: RealmList<Match>, match: Match): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}