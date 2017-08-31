package app.simone.multiplayer.view.nearby

import app.simone.multiplayer.controller.NearbyGameController
import com.facebook.Profile

/**
 * Created by gzano on 31/08/2017.
 */
class WaitingRoomPresenter(var waitingRoomView: DistributedView.WaitingRoomView) : Presenter {


    val nearbyGameController = NearbyGameController()
    var currentMatchID = ""
    override fun onCreate() {
        val intent = waitingRoomView.getIntent()
        if (intent.hasExtra("users")) {
            val users = intent.getSerializableExtra("users") as List<Map<String, String>>

            val profile = Profile.getCurrentProfile()
            val pid = profile.id

            val playerIDs = ArrayList<String>()

            users?.forEach { player ->
                val id = player["id"]
                if (id != null) {
                    playerIDs.add(id)
                }
            }

            currentMatchID = nearbyGameController.createMatch(playerIDs, pid)

        } else if (intent.hasExtra("matchID")) {
            currentMatchID = intent.getStringExtra("matchID")
            nearbyGameController.acceptInvite(Profile.getCurrentProfile().id, currentMatchID)

        }

        waitingRoomView.updateText(currentMatchID)
        nearbyGameController.getAndListenForNewPlayers(currentMatchID, waitingRoomView.getActivityContext())

    }
}