package app.simone;

import org.junit.BeforeClass;

import akka.actor.ActorSystem;
import app.simone.shared.application.App;
import scala.concurrent.duration.FiniteDuration;

import static akka.testkit.JavaTestKit.duration;

/**
 * Created by nicola on 29/08/2017.
 */

public class ActorTests {

    protected static ActorSystem system;
    protected static FiniteDuration defaultDuration = duration("30 seconds");

    @BeforeClass
    public static void setup() {
        system = App.buildActorSystem();
    }

}
