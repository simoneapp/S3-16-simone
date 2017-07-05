package app.simone.Controller

import app.simone.DataModel.Match
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

/**
 * Created by gzano on 26/06/2017.
 */
interface UserDataController {
    val FIELD_SCORE: String
    fun getMatches(): RealmList<Match>

    fun getMatchesSortedByScore(): RealmResults<Match>? {
        return getMatches().sort(FIELD_SCORE, Sort.DESCENDING)
    }
    fun getBestGame():Match{
        return getMatchesSortedByScore()!!.first()
    }
    fun getLastGame():Match{
        return getMatches().last()
    }
    fun insertMatch(score:Int,gameType:Int)




}