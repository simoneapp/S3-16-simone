package app.simone.multiplayer.view.nearby;


import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Button;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import app.simone.R;
import app.simone.shared.utils.Constants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


/**
 * Created by gzano on 29/08/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NearbyViewIntegratedTest {
    private String fakeMatchID = "fab3fc76-d17c-448b-b364-6b600343397c";
    private CharSequence waiting = "waiting for your color...";
    private NearbyGameActivity nearbyGameActivity;
    private DatabaseReference databaseRootReference = FirebaseDatabase.getInstance().getReference(Constants.NODE_ROOT);
    private Button button;
    @Rule
    public ActivityTestRule<NearbyGameActivity> activityTestRule = new ActivityTestRule<>(NearbyGameActivity.class, true, false);

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("match", fakeMatchID);
        activityTestRule.launchActivity(intent);
        nearbyGameActivity = activityTestRule.getActivity();
        button = (Button) nearbyGameActivity.findViewById(R.id.beautifulButton);
        assertNotNull(button);
        assertNotNull(nearbyGameActivity.getIntent().getStringExtra("match"));
    }

    @Test
    public void isButtonDisplayed() {

        onView(withId(R.id.beautifulButton)).perform(click()).check(matches(isDisplayed()));


    }


    @Test
    public void wrongStatus() {
        CharSequence textBefore = button.getText();
        onView(withId(R.id.beautifulButton)).perform(click());
        CharSequence whatAShame = "what a shame you lost the game";
        Log.d("BUTTONTEXT", " before " + textBefore + " after " + whatAShame);
        assertTrue(!textBefore.equals(whatAShame));
    }


}
