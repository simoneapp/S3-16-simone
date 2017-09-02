package app.simone.singleplayer.model;
import app.simone.R;

/**
 * SimonColorImpl, enum.
 * Each value (GREEN, RED, BLUE, YELLOW) is associated with a button, a sound, and a color.
 *
 * @author Michele Sapignoli
 */

public enum SimonColorImpl implements SimonColor{
    GREEN(R.id.GREEN, R.raw.simonsound1, R.color.myGreen),
    RED(R.id.RED, R.raw.simonsound2, R.color.myRed),
    BLUE(R.id.BLUE, R.raw.simonsound3, R.color.myBlue),
    YELLOW(R.id.YELLOW, R.raw.simonsound4, R.color.myYellow);

    private final int buttonId;
    private final int soundId;
    private final int colorId;

    SimonColorImpl(final int buttonId, final int soundId, final int colorId) {
        this.buttonId = buttonId;
        this.soundId = soundId;
        this.colorId = colorId;
    }

    public int getButtonId() { return buttonId; }

    public int getSoundId() { return soundId; }

    public int getColorId() {
        return colorId;
    }

    /**
     * Gets SimonColorImpl from buttonId
     * @param number buttonId
     * @return color SimonColorImpl
     */
    public static SimonColorImpl fromInt(int number){
        for (SimonColorImpl type : SimonColorImpl.values()) {
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
        for (SimonColorImpl type : SimonColorImpl.values()) {
            if (type.getButtonId() == buttonId) {
                return type.colorId;
            }
        }
        return 0;
    }
}