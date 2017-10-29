package app.simone.settings.model

import app.simone.R
import app.simone.singleplayer.model.SimonColor
import java.util.*

enum class SimonCreditsColor private constructor(private val buttonId: Int,
                                                 private val imageViewId: Int,
                                                 private val soundId: Int,
                                                 private val colorId: Int,
                                                 private val nameId: Int,
                                                 private val imageId: Int) : SimonColor {

    GREEN(R.id.credits_button_green, R.id.credits_image_green, R.raw.simonsound1, R.color.myGreen,
            R.string.michele_sapignoli, R.drawable.michele_sapignoli),

    RED(R.id.credits_button_red, R.id.credits_image_red, R.raw.simonsound2, R.color.myRed,
            R.string.nicola_giancecchi, R.drawable.nicola_giancecchi),

    BLUE(R.id.credits_button_yellow, R.id.credits_image_yellow, R.raw.simonsound3, R.color.myBlue,
            R.string.giacomo_zanotti, R.drawable.giacomo_zanotti),

    YELLOW(R.id.credits_button_blue, R.id.credits_image_blue, R.raw.simonsound4, R.color.myYellow,
            R.string.giacomo_bartoli, R.drawable.giacomo_bartoli);

    override fun getButtonId(): Int {
        return buttonId
    }

    override fun getSoundId(): Int {
        return soundId
    }

    override fun getColorId(): Int {
        return colorId
    }

    fun getImageViewId(): Int {
        return imageViewId
    }

    fun getNameId(): Int {
        return nameId
    }

    fun getImageId(): Int {
        return imageId
    }

    companion object {
        val allColors: MutableList<SimonCreditsColor>
            get() = Arrays.asList(GREEN,RED,BLUE,YELLOW)
    }
}