package app.simone.multiplayer;

/**
 * Created by nicola on 28/08/2017.
 */

public class CorrectMockingStrategy implements MockingStrategy {
    @Override
    public String getRawContent() {
        return "{\"data\":[{\"name\":\"John Appleseed\",\"id\":\"1020304050\"}," +
                "{\"name\":\"Jon Snow\",\"id\":\"2030405060\"}," +
                "{\"name\":\"Elliot Alderson\",\"id\":\"3040506070\"}," +
                "{\"name\":\"Richard Hendricks\",\"id\":\"4050607080\"}," +
                "{\"name\":\"Noam Chomsky\",\"id\":\"5060708090\"}]}";
    }

    @Override
    public boolean getSuccess() {
        return true;
    }
}
