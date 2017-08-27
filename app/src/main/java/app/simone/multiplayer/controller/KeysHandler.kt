package app.simone.multiplayer.controller

import java.util.ArrayList

/**
 * Created by Giacomo on 22/08/2017.
 */
class KeysHandler {

    var list: ArrayList<String> = ArrayList()

    fun addToList(s: String) {
        this.list.add(s)
    }

    fun getElement(index: Int): String = list[index]

}