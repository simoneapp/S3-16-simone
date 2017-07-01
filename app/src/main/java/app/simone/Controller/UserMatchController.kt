package app.simone.Controller

import app.simone.Controller.ControllerImplementations.ResultNotFoundException

/**
 * Created by gzano on 22/06/2017.
 */

interface UserMatchController {

    fun insertMatch(playerName: String,score:Int)
}
