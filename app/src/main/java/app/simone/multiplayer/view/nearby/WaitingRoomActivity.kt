package app.simone.multiplayer.view.nearby

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView
import app.simone.R
import app.simone.multiplayer.controller.NearbyGameController
import com.facebook.Profile

/**
 * waiting room for players
 */
class WaitingRoomActivity : AppCompatActivity(), DistributedView.WaitingRoomView {


    private var txvMatchID: TextView? = null
    private var waitingRoomPresenter: WaitingRoomPresenter? = null
    var listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_room)

        listView = findViewById(R.id.list_waiting_room) as ListView
        txvMatchID = findViewById(R.id.text_match_id) as TextView
        waitingRoomPresenter = WaitingRoomPresenter(this)
        waitingRoomPresenter?.onCreate()

    }

    override fun updateText(text: String) {
        txvMatchID?.text = text
    }

    override fun getActivityContext(): WaitingRoomActivity {

        return this
    }
    override fun startGameActivity(matchID: String){
        val intent = Intent(this, NearbyGameActivity::class.java)
        intent.putExtra("match", matchID)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)
    }

}
