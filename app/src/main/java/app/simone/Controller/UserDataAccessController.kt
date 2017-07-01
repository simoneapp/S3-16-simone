package app.simone.Controller

import app.simone.DataModel.Match
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

/**
 * Created by gzano on 26/06/2017.
 */
interface UserDataAccessController {
    val FIELD_SCORE: String
    fun getMatches(playerName: String): RealmList<Match>

    fun getMatchesSortedByScore(playerName: String): RealmResults<Match>? {
        return getMatches(playerName).sort(FIELD_SCORE, Sort.DESCENDING)
    }

    fun getDate(match: Match): Date{
        return match.gameDate
    }




}