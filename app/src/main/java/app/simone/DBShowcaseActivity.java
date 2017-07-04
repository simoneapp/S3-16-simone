package app.simone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import app.simone.Controller.ControllerImplementations.UserDataControllerImpl;
import app.simone.Controller.UserDataController;
import app.simone.DataModel.Match;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DBShowcaseActivity extends AppCompatActivity {

    private Realm realm;
    private UserDataController userDataController;
    private RealmResults realmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbshowcase);
        initRealm();
        userDataController = new UserDataControllerImpl(realm);
        realmResults = userDataController.getMatchesSortedByScore();
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, R.layout.listview, render(realmResults));
        ListView listView = (ListView) findViewById(R.id.listViewDB);
        listView.setAdapter(ad);
    }


    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("DBPlayers.realm").deleteRealmIfMigrationNeeded().schemaVersion(5).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    private String[] render(RealmResults<Match> realmResults) {
        int i = 0;
        String[] renderedResults = new String[realmResults.size()];
        if (!realmResults.isEmpty()) {
            for (Match m : realmResults) {
                renderedResults[i] = Integer.toString(m.getScore());
                i++;
            }
        }
        return renderedResults;
    }
}
