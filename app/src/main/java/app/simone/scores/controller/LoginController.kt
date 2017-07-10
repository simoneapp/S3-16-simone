package app.simone.scores.controller

import app.simone.scores.model.Match
import app.simone.scores.model.Player

/**
 * Created by gzano on 22/06/2017.
 */

class LoginController() : UserInputController {

    val FIELD_PLAYER_NAME = "name"
    val realm = app.simone.multiplayer.controller.DataManager.Companion.instance.realm

    override fun insertMatch(playerName: String) {

        realm?.executeTransaction { realm->
            val player=realm.where(Player::class.java).equalTo(FIELD_PLAYER_NAME,playerName).findFirst()
            val match1 = realm.createObject(Match::class.java)
            val match2 = realm.createObject(Match::class.java)
            val match3 = realm.createObject(Match::class.java)
            val match4 = realm.createObject(Match::class.java)

            match1.score = 200//Random().nextInt(20)
            match2.score = 3//Random().nextInt(20)
            match3.score = 2//Random().nextInt(20)
            match4.score = 1//Random().nextInt(20)

            player.matches = io.realm.RealmList(match1, match2, match3, match4)
        }
    }

    override fun insertPlayer(playerName:String) {

        realm?.executeTransaction { realm ->
            if (realm.where(Player::class.java).equalTo("name", playerName).findAll().isEmpty()) {
                 realm.createObject(Player::class.java, playerName)
            }
        }

    }

}