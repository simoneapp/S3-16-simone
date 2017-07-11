package app.simone.scores.view;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.R;
import app.simone.shared.application.App;

/**
 * @author Michele Sapignoli
 */

public class ScoreboardBaseGameActivity extends FullscreenBaseGameActivity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton leaderboardFab = (FloatingActionButton) this.findViewById(R.id.scoreboard_fab_leaderboard);
        leaderboardFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (App.getGoogleApiHelper().getGoogleApiClient() == null || !App.getGoogleApiHelper().getGoogleApiClient().isConnected()) {
                    App.getGoogleApiHelper().buildGoogleApiClient(mContentView, getParent());
                }
            }
        });

        FloatingActionButton achievementFab = (FloatingActionButton) this.findViewById(R.id.scoreboard_fab_achievements);
        achievementFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (App.getGoogleApiHelper().getGoogleApiClient() == null || !App.getGoogleApiHelper().getGoogleApiClient().isConnected()) {
                    App.getGoogleApiHelper().buildGoogleApiClient(mContentView, getParent());
                }
            }
        });
        Log.d("Score","score");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_scoreboard);
        mContentView = findViewById(R.id.scoreboard_fullscreen_content);
    }

    @Override
    protected void backTransition() {
        overridePendingTransition(R.anim.slide_down, R.anim.slide_down_existing);
    }


}
