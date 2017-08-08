package app.simone.multiplayer.controller

import app.simone.multiplayer.model.OnlineMatch
import com.google.firebase.database.DataSnapshot

/**
 * Created by Giacomo on 07/08/2017.
 */
class ParsingMatches(val result: (DataSnapshot) ->(ArrayList<OnlineMatch>)) {

    fun printString(s: DataSnapshot)= println(result.invoke(s))

    val keysArray = KeysHandler()
    val requestsUser = null
}

val keysFunction = {it: String -> it.toLowerCase()}
val requestFunction = {it: String -> it.toUpperCase()}