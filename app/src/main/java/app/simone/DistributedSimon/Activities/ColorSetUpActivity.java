package app.simone.DistributedSimon.Activities;


import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
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
    private String sequenceIndex = "";
    private DatabaseReference databaseReference;
    private final String CHILD_PLAYERS = "users";
    private final String NODE_REF_ROOT = "matchesTry";
    private final String CHILD_PLAYERSSEQUENCE = "PlayersSequence";
    private final String CHILD_CPUSEQUENCE = "CPUSequence";
    private final String CHILD_MATCHID = "MATCHID";
    private final String CHILD_INDEX = "index";


    private Button buttonColor;
    private int blinkCount = 0;
    private static android.os.Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_set_up);
        databaseReference = FirebaseDatabase.getInstance().getReference(NODE_REF_ROOT);
        buttonColor = (Button) findViewById(R.id.beautifulButton);


        setColor();


    }

    public void sendColor(View view) throws ExecutionException, InterruptedException {
        databaseReference.child(CHILD_MATCHID).child(CHILD_PLAYERSSEQUENCE).addListenerForSingleValueEvent(new ValueEventListener() {
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
        blink();

    }


    private void setColor() {
        Log.d("PROVA", "executing query");
        databaseReference.child(CHILD_MATCHID).child(CHILD_PLAYERS).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> player_info = (HashMap<String, String>) singleDataSnapshot.getValue();
                    Log.d("PROVA", player_info.toString());
                    playerID = singleDataSnapshot.getKey();
                    Log.d("PLAYERID", playerID);

                    if (!Boolean.parseBoolean(player_info.get("taken"))) {
                        playerOwnColor = player_info.get("color");

                        render();
                        databaseReference.child(CHILD_MATCHID).child(CHILD_PLAYERS).child(playerID).child("taken").setValue("true");
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

    private void render() {
        if (playerOwnColor.equals("YELLOW")) {
            buttonColor.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.myYellow), PorterDuff.Mode.MULTIPLY);
        }
        if (playerOwnColor.equals("GREEN")) {
            buttonColor.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.myGreen), PorterDuff.Mode.MULTIPLY);
        }
        if (playerOwnColor.equals("RED")) {
            buttonColor.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.myRed), PorterDuff.Mode.MULTIPLY);

        }
        if (playerOwnColor.equals("BLUE")) {
            buttonColor.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.myBlue), PorterDuff.Mode.MULTIPLY);

        }

    }

    private void blink() {
        final DatabaseReference cpuSequenceRef = databaseReference.child(CHILD_MATCHID).child(CHILD_CPUSEQUENCE).getRef();
        databaseReference.child(CHILD_MATCHID).child(CHILD_INDEX).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("PROVAINDEX_BLINK", dataSnapshot.getValue(String.class));
                final String cpuSequenceIndex = dataSnapshot.getValue(String.class);
                cpuSequenceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String childrenCount = String.valueOf(dataSnapshot.getChildrenCount());
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String colorSequence = child.getValue(String.class);
                            String index = child.getKey();
                            Log.d("CHILDLOOP", colorSequence + " " + index);

                            if (playerOwnColor.equals(colorSequence) && cpuSequenceIndex.equals(index)) {
                                ++blinkCount;
                                Log.d("BLINKING", String.valueOf(blinkCount));
                                if (index.equals(childrenCount)) {

                                    buttonColor.setText(playerOwnColor + " " + blinkCount + " your turn!");

                                } else {
                                    int newIndex = Integer.parseInt(index) + 1;
                                    buttonColor.setText(playerOwnColor + " " + blinkCount);
                                    databaseReference.child(CHILD_MATCHID).child(CHILD_INDEX).setValue(String.valueOf(newIndex));

                                }


                            } else {
                                buttonColor.setText(playerOwnColor + " " + blinkCount);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
