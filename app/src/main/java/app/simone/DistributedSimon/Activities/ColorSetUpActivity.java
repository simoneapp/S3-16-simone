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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import app.simone.R;

public class ColorSetUpActivity extends AppCompatActivity {


    private String playerID="";
    private String color="";

    private DatabaseReference databaseReference;
    private final String CHILD_PLAYERS = "players";

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_set_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("matchesTry");
        button = (Button) findViewById(R.id.colorButtonDistributed);
        setColor();

    }

    public void sendColor(View view) throws ExecutionException, InterruptedException {


    }


    private void setColor() {
        Log.d("PROVA", "executing query");
        databaseReference.child(CHILD_PLAYERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> player_info = (HashMap<String, String>) singleDataSnapshot.getValue();
                    Log.d("PROVA", player_info.toString());
                    playerID = singleDataSnapshot.getKey();


                    if (!Boolean.parseBoolean(player_info.get("taken"))) {
                        color = player_info.get("color");

                        databaseReference.child(CHILD_PLAYERS).child(playerID).child("taken").setValue("true");
                        button.setText(playerID+" "+color);
 

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
