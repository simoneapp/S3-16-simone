package app.simone.singleplayer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.R;
import app.simone.shared.utils.Analytics;
import app.simone.shared.utils.Constants;

/**
 * VSCpuActivity, subclass of FullscreenBaseGameActivity.
 * Opens the activity corresponding to the selected chosen mode.
 * @author Michele Sapignoli
 */

public class VSCpuActivity extends FullscreenBaseGameActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton easyFab = (FloatingActionButton) this.findViewById(R.id.vs_cpu_fab);
        easyFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Analytics.Companion.logNewSinglePlayerGame(Constants.CLASSIC_MODE, getApplicationContext());
                openActivity(SingleplayerGameActivity.class, Constants.CHOSEN_MODE, Constants.CLASSIC_MODE, R.anim.right_in, R.anim.left_out);
            }
        });

        FloatingActionButton hardFab = (FloatingActionButton) this.findViewById(R.id.vs_cpu_fab_hard);
        hardFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Analytics.Companion.logNewSinglePlayerGame(Constants.CLASSIC_MODE, getApplicationContext());
                openActivity(SingleplayerGameActivity.class, Constants.CHOSEN_MODE, Constants.HARD_MODE, R.anim.right_in, R.anim.left_out);
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


}
