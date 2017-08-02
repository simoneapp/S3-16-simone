package app.simone.DistributedSimon.Activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

import app.simone.R;

public class ColorSetUpActivity extends AppCompatActivity {


    private String playerID;
    private String color;
    private final String FAKE_PLAYERID = "fake player id";
    private final String NAME_KEY = "id";
    private DatabaseReference databaseReference;
    private final String CHILD_PLAYERS = "players";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_set_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("matchesTry");

        getFirebaseID();

        //  Log.d("SHAREDPREFERENCES", sharedPreferences.getString(NAME_KEY, "").toString());


    }

    public void setPlayerID(View view) {
        ((Button) view).setText(playerID+" "+color);
    }


    private void getFirebaseID() {
        databaseReference.child(CHILD_PLAYERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> player_info = (HashMap<String, String>) singleDataSnapshot.getValue();
                    playerID = singleDataSnapshot.getKey().toString();
                    if (!Boolean.parseBoolean(player_info.get("taken"))) {
                        color=player_info.get("color");
                        databaseReference.child(CHILD_PLAYERS).child(playerID).child("taken").setValue("true");
                        Log.d("CHILDSNAPSHOT", "changing value");
                        break;
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void resetLocalPreferences() {

    }


}
