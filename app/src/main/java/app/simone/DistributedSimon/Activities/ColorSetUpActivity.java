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


    private String playerID="";
    private String color="";

    private DatabaseReference databaseReference;
    private final String CHILD_PLAYERS = "players";

    private Button buttonColor,buttonInstantPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_set_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("matchesTry");
        buttonColor = (Button) findViewById(R.id.colorButtonDistributed);
        buttonInstantPlay=(Button)findViewById(R.id.buttonStartGameDistributed);
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

        setColor();

    }

    public void sendColor(View view) throws ExecutionException, InterruptedException {


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


                    if (!Boolean.parseBoolean(player_info.get("taken"))) {
                        color = player_info.get("color");

                        databaseReference.child(CHILD_PLAYERS).child(playerID).child("taken").setValue("true");
                        buttonColor.setText(playerID+" "+color);


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

    public void startGame(View view){

        if(view.isPressed()){

        }
    }




}
