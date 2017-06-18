package Model

/**
 * Created by gzano on 18/06/2017.
 */

//pattern strategy: algorithm changes at run time, when player choose the difficulty
interface AIColorSequenceGenerator {
    fun generateSequence(numberOfColorsToGuess:Int):MutableList<Color>



}