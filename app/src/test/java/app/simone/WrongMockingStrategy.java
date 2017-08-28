package app.simone;

/**
 * Created by nicola on 28/08/2017.
 */

public class WrongMockingStrategy implements MockingStrategy {
    @Override
    public String getRawContent() {
        return "";
    }

    @Override
    public boolean getSuccess() {
        return false;
    }
}
