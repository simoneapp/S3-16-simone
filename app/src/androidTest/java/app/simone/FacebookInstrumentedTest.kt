package app.simone

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



/**
 * Created by nicola on 21/06/2017.
 */

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    /*@Rule
    public var mActivityRule = ActivityTestRule(
            MainActivity::class.java)
*/

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals("app.simone", appContext.packageName)
    }

    @Test
    fun changeText_sameActivity() {

        Espresso.onView(ViewMatchers.withId(R.id.btn_player2player)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.login_button)).perform(click())
        //Espresso.onView(ViewMatchers.)
    }

}