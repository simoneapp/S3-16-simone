package colors;

import app.simone.R;

/**
 * Created by sapi9 on 27/06/2017.
 */

public enum SColor {
    GREEN(R.id.GREEN, R.raw.simonsound1),
    RED(R.id.RED, R.raw.simonsound2),
    BLUE(R.id.BLUE, R.raw.simonsound3),
    YELLOW(R.id.YELLOW, R.raw.simonsound4);

    private final int buttonId;
    private final int soundId;

    SColor(final int buttonId, final int soundId) {

        this.buttonId = buttonId;
        this.soundId = soundId;
    }

    public int getButtonId() { return buttonId; }
    public int getSoundId() { return soundId; }

    public static SColor fromInt(int number){
        for (SColor type : SColor.values()) {
            if (type.getButtonId() == number) {
                return type;
            }
        }
        return null;
    }
}