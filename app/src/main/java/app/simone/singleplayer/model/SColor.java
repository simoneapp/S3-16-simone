package app.simone.singleplayer.model;
import app.simone.R;

/**
 * SColor, enum.
 * Each value (GREEN, RED, BLUE, YELLOW) is associated with a button, a sound, and a color.
 *
 * @author Michele Sapignoli
 */

public enum SColor {
    GREEN(R.id.GREEN, R.raw.simonsound1, R.color.myGreen),
    RED(R.id.RED, R.raw.simonsound2, R.color.myRed),
    BLUE(R.id.BLUE, R.raw.simonsound3, R.color.myBlue),
    YELLOW(R.id.YELLOW, R.raw.simonsound4, R.color.myYellow);

    private final int buttonId;
    private final int soundId;
    private final int colorId;

    SColor(final int buttonId, final int soundId, final int colorId) {
        this.buttonId = buttonId;
        this.soundId = soundId;
        this.colorId = colorId;
    }

    /**
     * Gets the buttonId
     * @return buttonId id of the button
     */
    public int getButtonId() { return buttonId; }

    /**
     * Gets the soundId
     * @return soundId id of the sound
     */
    public int getSoundId() { return soundId; }

    /**
     * Gets the colorId
     * @return colorId id of the color
     */
    public int getColorId() {
        return colorId;
    }

    /**
     * Gets SColor from buttonId
     * @param number buttonId
     * @return color SColor
     */
    public static SColor fromInt(int number){
        for (SColor type : SColor.values()) {
            if (type.getButtonId() == number) {
                return type;
            }
        }
        return null;
    }

    /**
     * Gets colorId from buttonId
     * @param buttonId buttonId
     * @return colorId
     */
    public static int getColorIdFromButtonId(int buttonId){
        for (SColor type : SColor.values()) {
            if (type.getButtonId() == buttonId) {
                return type.colorId;
            }
        }
        return 0;
    }
}