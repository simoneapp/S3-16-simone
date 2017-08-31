package app.simone.multiplayer.view.nearby

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView
import app.simone.R
import app.simone.multiplayer.controller.NearbyGameController
import com.facebook.Profile

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

}
