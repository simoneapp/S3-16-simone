package colors;

/**
 * Created by sapi9 on 27/06/2017.
 */

public enum Colors{
    GREEN(0),
    RED(1),
    BLUE(2),
    YELLOW(3);

    private final int value;

    Colors(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}