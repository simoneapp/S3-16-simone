package app.simone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.RadioGroup;

import akka.actor.ActorRef;
import messages.StartGameVsCPUMsg;
import scala.collection.immutable.Stream;
import utils.Constants;
import utils.Utilities;
import application.mApplication;

/**
 * Created by sapi9 on 20/06/2017.
 */

public class VSCpuActivity extends FullscreenActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.vs_cpu_fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioGroup radioButtonGroup = (RadioGroup) findViewById(R.id.vs_cpu_radiogroup);
                int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonID);
                int idx = radioButtonGroup.indexOfChild(radioButton);

                ActorRef CPUActor = Utilities.getActorByName(Constants.PATH_ACTOR + Constants.CPU_ACTOR_NAME, mApplication.getActorSystem());
                CPUActor.tell(new StartGameVsCPUMsg(idx), ActorRef.noSender());
                openActivity(GameActivity.class);
            }
        });
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_vs_cpu);
        mContentView = findViewById(R.id.vs_cpu_fullscreen_content);
    }

}
