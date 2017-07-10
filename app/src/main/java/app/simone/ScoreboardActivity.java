package app.simone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.google.android.gms.games.Games;

import application.App;

/**
 * @author Michele Sapignoli
 */

public class ScoreboardActivity extends FullscreenActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatingActionButton leaderboardFab = (FloatingActionButton) this.findViewById(R.id.scoreboard_fab_leaderboard);
        leaderboardFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(App.getGoogleApiHelper().getGoogleApiClient()), 1);
            }
        });

        FloatingActionButton achievementFab = (FloatingActionButton) this.findViewById(R.id.scoreboard_fab_achievements);
        achievementFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivityForResult(Games.Achievements.getAchievementsIntent(App.getGoogleApiHelper().getGoogleApiClient()), 1);
            }
        });
        Log.d("Score","score");
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
