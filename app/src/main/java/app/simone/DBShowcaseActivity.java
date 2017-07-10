package app.simone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import app.simone.Controller.ControllerImplementations.LocalDataControllerImpl;
import app.simone.Controller.UserDataController;
import app.simone.DataModel.Match;
import app.simone.DataModel.Player;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import utils.Constants;

public class DBShowcaseActivity extends AppCompatActivity {

    private Realm realm;
    private UserDataController userDataController;
    private RealmResults<Match> realmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbshowcase);
        initRealm();
        userDataController = new LocalDataControllerImpl(realm);
        TextView bestScoreTextView = (TextView) findViewById(R.id.textView33);
        realmResults = userDataController.getMatchesSortedByScore();
        if (realmResults.isEmpty()) {
            bestScoreTextView.setText("NO GAME YET!!");
        } else {
            ArrayAdapter<String> ad = new ArrayAdapter<>(this, R.layout.listview, render(realmResults));
            ListView listView = (ListView) findViewById(R.id.listViewDB);
            listView.setAdapter(ad);
            Log.d("BESTSCORETEST ", Integer.toString(userDataController.getBestGame().getScore()));
            String bestScore = Integer.toString(userDataController.getBestGame().getScore());
            String score = Integer.toString(userDataController.getLastGame().getScore());

            bestScoreTextView.setText(bestScore + " last game score: " + score);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(3)
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

    }

    private String[] render(RealmResults<Match> realmResults) {
        int i = 0;
        String[] renderedResults = new String[realmResults.size()];
        if (!realmResults.isEmpty()) {
            for (Match m : realmResults) {

                renderedResults[i] = "score: " + Integer.toString(m.getScore()) + " date: " + m.getGameDate()+
                        " game type: "+(m.getGameType()==Constants.CLASSIC_MODE?"classic":"hard");
                i++;
            }
        }

        return renderedResults;
    }




}
