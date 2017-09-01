package app.simone.multiplayer.view.nearby

/**
 * Created by gzano on 24/08/2017.
 */
interface MatchBehaviour {

    /**
     * rendering logic for the blink action
     */
    fun blink()

    /**
     * feature added to increase game speed
     * @param threshold that indicates how speed increases
     */
    fun increaseSpeed(threshold:Long):Long{
        when(threshold){
            2.toLong(),3.toLong()-> return 1800
            6.toLong()->return 1500
            8.toLong()->return 1000
            else -> return 500
        }
    }
}