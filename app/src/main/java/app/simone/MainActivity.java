package app.simone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import app.simone.users.FacebookLoginActivity;

/**
 * @author Michele Sapignoli
 */
public class MainActivity extends FullscreenActivity {

    private Button VSCpuButton;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        VSCpuButton = (Button)findViewById(R.id.button_vs_cpu);


        //Listener on vs CPUActor button
        VSCpuButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openActivity(VSCpuActivity.class);
            }
        });

        FloatingActionButton mainFab = (FloatingActionButton) findViewById(R.id.main_fab);
        TextView simoneTextView = (TextView) findViewById(R.id.main_simone_textview);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        mainFab.startAnimation(animation);
        simoneTextView.startAnimation(animation);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(100);
    }

    protected void setSubclassContentView() {
        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.main_fullscreen_content);
        mVisible = true;
    }
    public void playVsCpu(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void openMultiplayer(View view) {
        Intent intent = new Intent(this, FacebookLoginActivity.class);
        startActivity(intent);
    }

}
