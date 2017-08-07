package app.simone.DistributedSimon.Activities;


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
import java.util.concurrent.ExecutionException;

import app.simone.R;

public class ColorSetUpActivity extends AppCompatActivity {


    private String playerID = "";
    private String playerOwnColor = "";

    private DatabaseReference databaseReference;
    private final String CHILD_PLAYERS = "players";
    private final String NODE_REF_ROOT="matchesTry";
    private final String CHILD_PLAYERSSEQUENCE="PlayersSequence";
    private final String CHILD_CPUSEQUENCE="CPUSequence";


    private Button buttonColor, buttonInstantPlay;
    private int blinkCount = 0;

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
        databaseReference.child(CHILD_PLAYERSSEQUENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                dataSnapshot.getRef().child(String.valueOf(count + 1)).setValue(playerOwnColor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onResume() {
        super.onResume();
        databaseReference.child(CHILD_CPUSEQUENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("I'm here in cpu", "ciao");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("CHILDLOOP", child.getValue().toString() + " " + child.getKey());
                    //get playerOwnColor and blink
                    String colorSequence = child.getValue().toString();
                    if (playerOwnColor.equals(colorSequence)) {
                        buttonColor.setText(playerOwnColor + " BLINK! now it is your turn "+blinkCount);
                        //check if this is last playerOwnColor of sequence
                        ++blinkCount;
                        Log.d("BLINKCOUNT", String.valueOf(blinkCount));


                    } else {
                        buttonColor.setText(playerOwnColor+" "+blinkCount);

                    }
                }

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
                        playerOwnColor = player_info.get("color");

                        databaseReference.child(CHILD_PLAYERS).child(playerID).child("taken").setValue("true");
                        buttonColor.setText(playerID + " " + playerOwnColor);


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

}
