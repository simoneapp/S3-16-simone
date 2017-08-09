package app.simone.multiplayer.view.nearby

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView
import app.simone.R
import app.simone.multiplayer.controller.NearbyGameController

class WaitingRoomActivity : AppCompatActivity() {

    private var txvMatchID : TextView? = null
    private val nearbyController = NearbyGameController()
    private var currentMatchID = ""

    var listView : ListView? = null
    var users : List<Map<String,String>>? = null
    private val controller = NearbyGameController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_room)

        listView = findViewById(R.id.list_waiting_room) as ListView
        txvMatchID = findViewById(R.id.text_match_id) as TextView

        if(intent.hasExtra("users")) {
            users = intent.getSerializableExtra("users") as List<Map<String,String>>

            if(users != null) {

                val playerIDs = ArrayList<String>()

                users?.forEach { player ->
                    val id = player["id"]
                    if(id != null){
                        playerIDs.add(id)
                    }
                }

                currentMatchID = nearbyController.createMatch(playerIDs)
            }
        } else if (intent.hasExtra("matchID")) {

            currentMatchID = intent.getStringExtra("matchID")
        }

        txvMatchID?.text = currentMatchID

        controller.getAndListenForNewPlayers(currentMatchID, this, users)
    }


}
