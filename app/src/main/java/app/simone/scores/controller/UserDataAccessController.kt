package app.simone.scores.controller

import app.simone.scores.model.Match

/**
 * Created by gzano on 26/06/2017.
 */
interface UserDataAccessController {
    val FIELD_SCORE: String
    fun getMatches(playerName:String): io.realm.RealmList<Match>

    fun getMatchesSortedByScore(playerName: String): io.realm.RealmResults<Match>? {
        return getMatches(playerName).sort(FIELD_SCORE, io.realm.Sort.DESCENDING)
    }
    fun getDate(matchList: io.realm.RealmList<Match>, match: Match): java.util.Date
    fun deleteMatches(playerName: String)
    fun deleteMatch(playerName: String,match: Match){
        getMatches(playerName).deleteAllFromRealm()
    }

}