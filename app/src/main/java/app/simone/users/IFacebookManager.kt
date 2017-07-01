package app.simone.users

import app.simone.users.model.FacebookFriend

/**
 * Created by nicola on 26/06/2017.
 */
interface IFacebookManager {
    fun updateScore(score: Int, completion: (success: Boolean, error: String?) -> Unit)
    fun getMyScore(completion: (success: Boolean, score: Int, error: String?) -> Unit)
    fun getFriendScore(user: FacebookFriend?, completion: (success: Boolean, score: Int, error: String?) -> Unit) //get freind score
}