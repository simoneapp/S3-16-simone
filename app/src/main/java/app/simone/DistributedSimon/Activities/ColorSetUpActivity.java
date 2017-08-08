package app.simone.DistributedSimon.Activities;


import android.graphics.PorterDuff;
import android.os.Message;
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
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import app.simone.R;

public class ColorSetUpActivity extends AppCompatActivity {


    private String playerID = "";
    private String playerOwnColor = "";

    private DatabaseReference databaseReference;
    private final String CHILD_PLAYERS = "users";
    private final String NODE_REF_ROOT = "matchesTry";
    private final String CHILD_PLAYERSSEQUENCE = "PlayersSequence";
    private final String CHILD_CPUSEQUENCE = "CPUSequence";
    private final String CHILD_MATCHID = "MATCHID";


    private Button buttonColor;
    private int blinkCount = 0;
    private static android.os.Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_set_up);
        databaseReference = FirebaseDatabase.getInstance().getReference(NODE_REF_ROOT);
        buttonColor = (Button) findViewById(R.id.beautifulButton);
        handler = new android.os.Handler() {
            @Override
            public void handleMessage(final Message msg) {
                Log.d("HANDLER", "handling stuff");
                if (msg.arg2 == 0) {
                    buttonColor.setAlpha(0.4f);
                }
                if (msg.arg2 == 1) {
                    buttonColor.setAlpha(1);
                }
            }
        };
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
        databaseReference.child(CHILD_MATCHID).child(CHILD_CPUSEQUENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("I'm here in cpu", "ciao");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("CHILDLOOP", child.getValue().toString() + " " + child.getKey());
                    //get playerOwnColor and blink
                    String colorSequence = child.getValue().toString();
                    String index = child.getKey();
                    long childrenCOunt = dataSnapshot.getChildrenCount();
                    if (playerOwnColor.equals(colorSequence)) {
                        if (Long.parseLong(index) == childrenCOunt) {
                            buttonColor.setText(playerOwnColor + " " + blinkCount + " your turn!");
                        } else {


                            buttonColor.setText(playerOwnColor + " " + blinkCount);
                            //  buttonColor.setAlpha(0.4f);
                            //check if this is last playerOwnColor of sequence
                            ++blinkCount;
                            Log.d("BLINKCOUNT", String.valueOf(blinkCount));

                        }
                    } else {
                        Message m = new Message();
                        m.arg2 = 1;
                        buttonColor.setText(playerOwnColor + " " + blinkCount);
                        handler.sendMessageDelayed(m, 1000);
                        //  buttonColor.setAlpha(1);

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


}
