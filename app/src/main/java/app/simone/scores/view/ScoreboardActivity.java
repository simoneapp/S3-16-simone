package app.simone.scores.view;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.google.android.gms.games.Games;

import app.simone.shared.main.FullscreenBaseGameActivity;
import app.simone.R;
import app.simone.shared.application.App;
import app.simone.shared.utils.Constants;

/**
 * @author Michele Sapignoli
 */

public class ScoreboardActivity extends FullscreenBaseGameActivity {
    int googleResource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton leaderboardFab = (FloatingActionButton) this.findViewById(R.id.scoreboard_fab_leaderboard);
        leaderboardFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                googleResource = 0;
                openIfConnected(googleResource);
            }
        });

        FloatingActionButton achievementFab = (FloatingActionButton) this.findViewById(R.id.scoreboard_fab_achievements);
        achievementFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                googleResource = 1;
                openIfConnected(googleResource);
            }
        });
        Log.d("Score", "score");
    }

    private void openIfConnected(int googleResource) {
        if (App.getGoogleApiHelper().getGoogleApiClient() == null || !App.getGoogleApiHelper().getGoogleApiClient().isConnected()) {
            Message msg = new Message();
            msg.what = Constants.CONNECT;
            msg.obj = this;
            googleHandler.sendMessage(msg);
        } else {
            if (googleResource == 0)
                startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(App.getGoogleApiHelper().getGoogleApiClient()), 1);
            else
                startActivityForResult(Games.Achievements.getAchievementsIntent(App.getGoogleApiHelper().getGoogleApiClient()), 1);
        }
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

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        if (googleResource == 0)
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(App.getGoogleApiHelper().getGoogleApiClient()), 1);
        else
            startActivityForResult(Games.Achievements.getAchievementsIntent(App.getGoogleApiHelper().getGoogleApiClient()), 1);
    }

    
}
