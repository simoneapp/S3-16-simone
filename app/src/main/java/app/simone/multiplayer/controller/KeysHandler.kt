package app.simone.multiplayer.controller

import java.util.ArrayList

/**
 * This class is just a container where match keys are stored while retrieving data from Firebase
 *
 * @author Giacomo
 */

class KeysHandler {

    var list: ArrayList<String> = ArrayList()

    fun addToList(s: String) {
        this.list.add(s)
    }

    fun getElement(index: Int): String = list[index]

}