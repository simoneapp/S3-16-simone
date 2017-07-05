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
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

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
        realmResults = userDataController.getMatchesSortedByScore();
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, R.layout.listview, render(realmResults));
        ListView listView=(ListView)findViewById(R.id.listViewDB);
        listView.setAdapter(ad);

        Log.d("BESTSCORETEST ",Integer.toString(userDataController.getBestGame().getScore()));
        String bestScore=Integer.toString(userDataController.getBestGame().getScore());
        TextView bestScoreTextView=(TextView)findViewById(R.id.textView33);


       bestScoreTextView.setText(bestScore);
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
                renderedResults[i] = "score: "+Integer.toString(m.getScore())+" date: "+m.getGameDate();
                i++;
            }
        }

        return renderedResults;
    }
    private String[] render(RealmList<Match> realmList){
        int i = 0;
        String[] renderedResults = new String[realmResults.size()];
        if (!realmResults.isEmpty()) {
            for (Match m : realmList) {
                renderedResults[i] = "score: "+Integer.toString(m.getScore())+" date: "+m.getGameDate();
                i++;
            }
        }

        return renderedResults;
    }



}
