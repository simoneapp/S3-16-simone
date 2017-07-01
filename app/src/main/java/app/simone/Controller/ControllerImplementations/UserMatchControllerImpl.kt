package app.simone.Controller.ControllerImplementations

import app.simone.Controller.UserMatchController
import app.simone.DataModel.Match

import app.simone.DataModel.Player
import io.realm.Realm
import io.realm.RealmList

/**
 * Created by gzano on 22/06/2017.
 */

class UserMatchControllerImpl(private val realm: Realm) : UserMatchController {

    val FIELD_PLAYER_NAME="name"

    override fun insertMatch(playerName: String,score:Int) {
        realm.executeTransaction { realm->
            val player=realm.where(Player::class.java).equalTo(FIELD_PLAYER_NAME,playerName).findFirst()
            val match=realm.createObject(Match::class.java)
            match.score=score


            player.matches.add(match)

        }
    }










}