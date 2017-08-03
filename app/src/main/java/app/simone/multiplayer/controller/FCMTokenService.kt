package app.simone.multiplayer.controller

import android.util.Log
import com.facebook.Profile
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by nicola on 01/08/2017.
 */

class FCMTokenService : FirebaseInstanceIdService() {

    companion object{
        fun updateCurrentToken() {

            if(FacebookManagerActor.isLoggedIn()){
                val fbID = Profile.getCurrentProfile().id
                val refreshedToken = FirebaseInstanceId.getInstance().token.toString()
                Log.d("FBToken", "Refreshed token: " + refreshedToken!!)

                NearbyGameController().updateToken(refreshedToken, fbID)
            }
        }
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        updateCurrentToken()
    }

}