package app.simone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.simone.DataModel.Player;
import app.simone.users.FacebookLoginActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * @author Michele Sapignoli
 */
public class MainActivity extends FullscreenActivity {

    private Button VSCpuButton;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VSCpuButton = (Button) findViewById(R.id.button_vs_cpu);


        //Listener on vs CPUActor button
        VSCpuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openActivity(VSCpuActivity.class);
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(100);
    }

    protected void setSubclassContentView() {
        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.main_fullscreen_content);
        mVisible = true;
    }

    public void playVsCpu(View view) {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("DBPlayers.realm").deleteRealmIfMigrationNeeded().schemaVersion(5).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
             RealmResults<Player> res= realm.where(Player.class).equalTo("name","Michele Sapignoli").findAll();
                if(!res.isEmpty()){
                   res.deleteAllFromRealm();
                }
                Player player = realm.createObject(Player.class,"Michele Sapignoli");
            }
        });
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openMultiplayer(View view) {
        Intent intent = new Intent(this, FacebookLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
