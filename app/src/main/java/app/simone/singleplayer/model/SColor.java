package app.simone.singleplayer.model;

import app.simone.R;

/**
 * Created by sapi9 on 27/06/2017.
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

    public int getButtonId() { return buttonId; }
    public int getSoundId() { return soundId; }
    public int getColorId() {
        return colorId;
    }

    public static SColor fromInt(int number){
        for (SColor type : SColor.values()) {
            if (type.getButtonId() == number) {
                return type;
            }
        }
        return null;
    }

    public static int getColorIdFromButtonId(int buttonId){
        for (SColor type : SColor.values()) {
            if (type.getButtonId() == buttonId) {
                return type.colorId;
            }
        }
        return 0;
    }
}