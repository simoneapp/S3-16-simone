package app.simone;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import app.simone.multiplayer.model.FacebookUser;
import app.simone.multiplayer.model.OnlineMatch;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Created by Giacomo on 09/08/2017.
 *
 * This class contains some tests concerning Firebase functionalities
 *
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class DataManagerTest {

    private DatabaseReference mDatabase;
    private OnlineMatch testMatch;

    @Before
    public void initMatch(){
        mDatabase=Mockito.mock(DatabaseReference.class);
        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mDatabase);
        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);

        FacebookUser user1 = setFirstUser();
        FacebookUser user2 = setSecondUser();
        setScores(user1,user2);
        testMatch=new OnlineMatch(user1,user2);
    }

    private FacebookUser setFirstUser(){
        return new FacebookUser("1","user test 1");
    }

    private FacebookUser setSecondUser(){
        return new FacebookUser("2","user test 2");
    }

    private void setScores(FacebookUser user1,FacebookUser user2){
        user1.setScore("2");
        user2.setScore("1");
    }


    @Test
    public void listener(){

        when(mDatabase.child(anyString())).thenReturn(mDatabase);
        mDatabase.child("test").setValue(testMatch);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);
                when(mockedDataSnapshot.getValue(OnlineMatch.class)).thenReturn(testMatch);
                valueEventListener.onDataChange(mockedDataSnapshot);
                return null;
            }
        }).when(mDatabase).addListenerForSingleValueEvent(any(ValueEventListener.class));

    }


}
