package app.simone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.RadioGroup;

import akka.actor.ActorRef;
import messages.StartGameVsCPUMsg;
import utils.Constants;
import utils.Utilities;
import application.mApplication;

/**
 * @author Michele Sapignoli
 */

public class VSCpuActivity extends FullscreenActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.vs_cpu_fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int idx = 0;

                openActivity(GameActivity.class, Constants.RADIOBTN_INDEX_KEY, idx);
            }
        });
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_vs_cpu);
        mContentView = findViewById(R.id.vs_cpu_fullscreen_content);
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    protected void forwardTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

}
