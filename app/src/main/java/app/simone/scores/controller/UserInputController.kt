package app.simone.scores.controller

/**
 * Created by gzano on 22/06/2017.
 */

interface UserInputController {

    @Throws(ResultNotFoundException::class)
    fun insertPlayer(playerName:String)

    fun insertMatch(playerName: String)
}
