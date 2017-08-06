package app.simone.DistributedSimon.Activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import app.simone.DistributedSimon.IColorGenerator;
import app.simone.R;
import app.simone.singleplayer.model.SColor;

public class ColorSetUpActivity extends AppCompatActivity implements IColorGenerator {


    private String playerID = "";
    private String color = "";

    private DatabaseReference databaseReference;
    private final String CHILD_PLAYERS = "players";

    private Button buttonColor, buttonInstantPlay;
    private TextView textView;
    private int blinkCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_set_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("matchesTry");
        buttonColor = (Button) findViewById(R.id.colorButtonDistributed);
        textView = (TextView) findViewById(R.id.blinkCountTextView);
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
        databaseReference.child("Sequence").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("CHILDLOOP", child.getValue().toString() + " " + child.getKey());
                    //get color and blink
                    String colorSequence = child.getValue().toString();
                    if (color.equals(colorSequence)) {
                        buttonColor.setText(color + " BLINK!");
                        //check if this is last color of sequence
                        long index = Long.parseLong(child.getKey());
                        long sequenceColorSize = dataSnapshot.getChildrenCount();
                        if (index == sequenceColorSize) {
                            String newColorIndex = String.valueOf(sequenceColorSize + 1);
                            blinkCount++;
                            Log.d("BLINKCOUNT", String.valueOf(blinkCount));
                            Log.d("ADDEDCOLOR", "color added");
                            databaseReference.child("Sequence").child(String.valueOf(newColorIndex)).setValue(getNextColor());
                        }

                    } else {
                        buttonColor.setText(color);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        databaseReference.child("Sequence").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d("CHILDADDED",dataSnapshot.getKey());
//                final String index = dataSnapshot.getKey();
//                final String colorSequence = dataSnapshot.getValue(String.class);
//                if (color.equals(colorSequence)) {
//                    databaseReference.child("Sequence").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            long colorSequenceSize = dataSnapshot.getChildrenCount();
//                            if (colorSequenceSize == Long.parseLong(index)) {

//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                } else {
//                    buttonColor.setText(color);
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//

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


    @Override
    public String getNextColor() {
        String[] colors = new String[4];
        colors[0] = "RED";
        colors[1] = "YELLOW";
        colors[2] = "BLUE";
        colors[3] = "GREEN";
        return colors[new Random().nextInt(4)];
    }
}
