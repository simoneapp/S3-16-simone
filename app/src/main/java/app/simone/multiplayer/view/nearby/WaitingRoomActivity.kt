package app.simone.multiplayer.view.nearby

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView
import app.simone.R
import app.simone.multiplayer.controller.NearbyGameController
import com.facebook.Profile

class WaitingRoomActivity : AppCompatActivity() {

    private var txvMatchID : TextView? = null
    private val nearbyController = NearbyGameController()
    private var currentMatchID = ""

    var listView : ListView? = null
    private val controller = NearbyGameController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_room)

        listView = findViewById(R.id.list_waiting_room) as ListView
        txvMatchID = findViewById(R.id.text_match_id) as TextView

        if(intent.hasExtra("users")) {
            val users = intent.getSerializableExtra("users") as List<Map<String,String>>

                val profile = Profile.getCurrentProfile()
                val pid = profile.id

                val playerIDs = ArrayList<String>()

                users.forEach { player ->
                    val id = player["id"]
                    if(id != null){
                        playerIDs.add(id)
                    }
                }

            currentMatchID = nearbyController.createMatch(playerIDs, pid)

        } else if (intent.hasExtra("matchID")) {
            currentMatchID = intent.getStringExtra("matchID")
            nearbyController.acceptInvite(Profile.getCurrentProfile().id, currentMatchID)
        }

        txvMatchID?.text = currentMatchID

        controller.getAndListenForNewPlayers(currentMatchID, this)
    }

    fun startGameActivity(matchID: String){
        val intent = Intent(this, NearbyGameActivity::class.java)
        intent.putExtra("match", matchID)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    }


}
