package app.simone;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import app.simone.multiplayer.controller.DataManager;
import app.simone.multiplayer.model.FacebookUser;
import app.simone.multiplayer.model.OnlineMatch;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Giacomo on 09/08/2017.
 *
 * This class contains some tests concerning Firebase functionalities
 *
 */

public class DataManagerTest {

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private OnlineMatch testMatch;

    @Before
    public void initMatch(){
        FacebookUser user1 = new FacebookUser("1","user test 1");
        FacebookUser user2 = new FacebookUser("2","user test 2");
        user1.setScore("2");
        user2.setScore("1");
        testMatch=new OnlineMatch(user1,user2);
        mDatabase.child("test").setValue(testMatch);
    }



    @Test
    public void listener(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                OnlineMatch match = dataSnapshot.getValue(OnlineMatch.class);
                assertEquals(testMatch,match);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("CANCELLED", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }


}
