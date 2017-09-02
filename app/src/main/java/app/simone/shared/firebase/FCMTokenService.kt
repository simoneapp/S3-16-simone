package app.simone.shared.firebase

import app.simone.multiplayer.controller.FacebookManagerActor
import app.simone.multiplayer.controller.NearbyGameController
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by nicola on 01/08/2017.
 */

class FCMTokenService : FirebaseInstanceIdService() {

    companion object{
        fun updateCurrentToken() {

            if(FacebookManagerActor.Companion.isLoggedIn()){
                val fbID = com.facebook.Profile.getCurrentProfile().id
                val refreshedToken = com.google.firebase.iid.FirebaseInstanceId.getInstance().token.toString()
                android.util.Log.d("FBToken", "Refreshed token: " + refreshedToken)

                NearbyGameController().updateToken(refreshedToken, fbID)
            }
        }
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        FCMTokenService.Companion.updateCurrentToken()
    }

}