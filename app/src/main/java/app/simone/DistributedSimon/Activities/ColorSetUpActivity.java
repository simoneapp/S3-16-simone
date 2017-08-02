package app.simone.DistributedSimon.Activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.simone.R;

public class ColorSetUpActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_set_up);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference();
        databaseReference.child("ciaone").setValue("hey la");

        
    }



}
