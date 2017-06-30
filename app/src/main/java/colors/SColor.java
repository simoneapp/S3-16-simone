package colors;

import app.simone.R;

/**
 * Created by sapi9 on 27/06/2017.
 */

public enum SColor {
    GREEN(R.id.GREEN),
    RED(R.id.RED),
    BLUE(R.id.BLUE),
    YELLOW(R.id.YELLOW);

    private final int value;

    SColor(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }

    public static SColor fromInt(int number){
        for (SColor type : SColor.values()) {
            if (type.getValue() == number) {
                return type;
            }
        }
        return null;
    }
}