package app.simone

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import app.simone.shared.main.MainActivity
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
class FacebookInstrumentedTest {

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
    fun testFacebookLogin() {
/*
        Espresso.onView(ViewMatchers.withId(R.id.btn_player2player)).perform(click())

        Espresso.onView(ViewMatchers.withId(R.id.button2)).perform(click())

        val btnLogin = Espresso.onView(ViewMatchers.withId(R.id.login_button))
        val manager = FacebookManager()

        if(!manager.isLoggedIn()) {
            btnLogin.perform(click())
            onWebView().withElement(findElement(Locator.NAME, "email")).perform(webKeys("simonedice123"))
            onWebView().withElement(findElement(Locator.NAME, "pass")).perform(webKeys("simonsays123"))
            onWebView().withElement(findElement(Locator.NAME, "login")).perform(webClick())
        }

        val newScore = 50
*/
        /*
        manager.updateScore(newScore) { success, error ->
            manager.getScore { success, score, error ->
                assert(score == newScore)
            }
        }
        */

    }

}
