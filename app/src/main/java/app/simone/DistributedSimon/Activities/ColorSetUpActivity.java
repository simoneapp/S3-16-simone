package app.simone.DistributedSimon.Activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import app.simone.R;

public class ColorSetUpActivity extends AppCompatActivity {


    private String playerID = "";
    private String color = "";

    private DatabaseReference databaseReference;
    private final String CHILD_PLAYERS = "players";

    private Button buttonColor, buttonInstantPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_set_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("matchesTry");
        buttonColor = (Button) findViewById(R.id.colorButtonDistributed);
        setColor();

        buttonInstantPlay = (Button) findViewById(R.id.buttonStartGameDistributed);
        buttonInstantPlay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseReference.child(CHILD_PLAYERS).child(playerID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.child(CHILD_PLAYERS).child(playerID).child("start").setValue("true");


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return false;
            }
        });


    }

    public void sendColor(View view) throws ExecutionException, InterruptedException {



    }

    public void onResume() {
        super.onResume();
        Log.d("ACTIVITYRESUME", "this resume is called");
        databaseReference.child(CHILD_PLAYERS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("CHILDADDED ",dataSnapshot.getKey()+" "+dataSnapshot.getValue().toString());
                if(color.equals(dataSnapshot.getValue())){
                    buttonColor.setText(color+" blink!!!");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("CHILDADDED ",dataSnapshot.getKey()+" "+dataSnapshot.getValue().toString());
                if(color.equals(dataSnapshot.getValue())){
                    buttonColor.setText(color+" blink!!!");
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setColor() {
        Log.d("PROVA", "executing query");
        databaseReference.child(CHILD_PLAYERS).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> player_info = (HashMap<String, String>) singleDataSnapshot.getValue();
                    Log.d("PROVA", player_info.toString());
                    playerID = singleDataSnapshot.getKey();
                    Log.d("PLAYERID", playerID);

                    if (!Boolean.parseBoolean(player_info.get("taken"))) {
                        color = player_info.get("color");

                        databaseReference.child(CHILD_PLAYERS).child(playerID).child("taken").setValue("true");
                        buttonColor.setText(playerID + " " + color);


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

    public void startGame(View view) {

        if (view.isPressed()) {

        }
    }


}
