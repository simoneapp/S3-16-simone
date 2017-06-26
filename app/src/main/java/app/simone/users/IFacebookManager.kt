package app.simone.users

/**
 * Created by nicola on 26/06/2017.
 */
interface IFacebookManager {
    fun updateScore(score: Int, completion: (success: Boolean, error: String?) -> Unit)
    fun getScore(completion: (success: Boolean, score: Int, error: String?) -> Unit)
}