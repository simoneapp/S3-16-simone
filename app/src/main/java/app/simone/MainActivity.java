package app.simone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import actors.PlayerActorJava;
import actors.ViewActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import messages.StartGameVsCPUMsg;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends FullscreenActivity {

    private Button VSCpuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        VSCpuButton = (Button)findViewById(R.id.button_vs_cpu);


        //Listener on vs CPU button
        VSCpuButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openVSCpuActivity();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);



        ActorSystem system = ActorSystem.create("system");
        ActorRef actor = system.actorOf(Props.create(PlayerActorJava.class), "actor");
        ActorRef view_actor = system.actorOf(Props.create(ViewActor.class), "view-actor");
        actor.tell( new StartGameVsCPUMsg(4), ActorRef.noSender());
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.main_fullscreen_content);
        mVisible = true;
    }


    /*
    VS CPU Activity
     */
    public void openVSCpuActivity() {
        Intent intent = new Intent(this, VSCpuActivity.class);
        startActivity(intent);
    }
}
