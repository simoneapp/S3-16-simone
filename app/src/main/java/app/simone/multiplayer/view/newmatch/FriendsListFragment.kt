package app.simone.multiplayer.view.newmatch

import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.view.nearby.WaitingRoomActivity
import app.simone.multiplayer.view.pager.MultiplayerPagerActivity
import app.simone.shared.utils.Constants
import app.simone.shared.utils.Utilities
import app.simone.singleplayer.view.MultiplayerGameActivity
import com.facebook.Profile

class FriendsListFragment : Fragment() {

    var rootView : android.view.View? = null

    var listView : android.widget.ListView? = null
    var btnPlay : FloatingActionButton? = null

    var friends = ArrayList<FacebookUser>()
    var adapter : FriendsListAdapter? = null

    var selectedUsers = ArrayList<FacebookUser>()
    var currentUser: app.simone.multiplayer.model.FacebookUser? = null

    override fun onCreateView(inflater: android.view.LayoutInflater?, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {

        rootView = inflater?.inflate(app.simone.R.layout.activity_facebook_login, container, false)

        initMainList()

        val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR
                + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                app.simone.shared.application.App.getInstance().actorSystem)

        actor.tell(app.simone.multiplayer.messages.FbViewSetupMsg(activity as MultiplayerPagerActivity), akka.actor.ActorRef.noSender())

        btnPlay = rootView?.findViewById(app.simone.R.id.floatingActionButton) as FloatingActionButton
        btnPlay?.setOnClickListener({

            val type = (activity as app.simone.multiplayer.view.pager.MultiplayerPagerActivity).type

            if (selectedUsers.count() > 0 && type != null) {

                    if(type == app.simone.multiplayer.model.MultiplayerType.INSTANT) {

                        val intent = android.content.Intent(context, MultiplayerGameActivity::class.java)
                        intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                        println("ME: " + com.facebook.Profile.getCurrentProfile().firstName.toString() + " " + com.facebook.Profile.getCurrentProfile().lastName.toString())
                        setUser()

                        val onlineMatch = app.simone.multiplayer.model.OnlineMatch(currentUser, selectedUsers.first())
                        intent.putExtra("multiplayerMode", "multiplayerMode")
                        intent.putExtra("key", app.simone.multiplayer.controller.DataManager.Companion.instance.createMatch(onlineMatch))
                        intent.putExtra("whichPlayer", "firstplayer")
                        activity.startActivity(intent)

                    } else if(type == app.simone.multiplayer.model.MultiplayerType.NEARBY) {

                        val parceled = ArrayList<Map<String, String>>()

                        val profile = Profile.getCurrentProfile()
                        val userMap = HashMap<String,String>()
                        userMap[FacebookUser.kNAME] = profile.name
                        userMap[FacebookUser.kID] = profile.id
                        userMap[FacebookUser.kPICTURE] = profile.getProfilePictureUri(Constants.FB_IMAGE_PICTURE_SIZE,Constants.FB_IMAGE_PICTURE_SIZE).toString()

                        selectedUsers.mapTo(parceled) { it.toDictionary() }
                        parceled.add(userMap)

                        val intent = android.content.Intent(context, WaitingRoomActivity::class.java)
                        intent.putExtra("users", parceled)
                        this.startActivity(intent)
                    }

            }
        })

        return rootView
    }
    fun initMainList() {
        listView = rootView?.findViewById(app.simone.R.id.list_friends) as android.widget.ListView
        adapter = FriendsListAdapter(context, friends, this)
        listView?.adapter = adapter
        listView?.onItemClickListener = android.widget.AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                    app.simone.shared.application.App.getInstance().actorSystem)
            val friend = adapter?.getItem(i)
            actor.tell(app.simone.multiplayer.messages.FbItemClickMsg(friend), akka.actor.ActorRef.noSender())
            selectUser(friend)
        }
        btnPlay?.isEnabled = false
    }

    fun updateList (response : app.simone.multiplayer.messages.FbResponseFriendsMsg) {
        this.activity.runOnUiThread {
            adapter?.clear()
            if (response.isSuccess) {
                friends = response.data as ArrayList<app.simone.multiplayer.model.FacebookUser>
                adapter?.addAll(friends)
            } else {
                Utilities.displayToast(response.errorMessage, activity)
            }
        }
    }

    fun selectUser(friend: app.simone.multiplayer.model.FacebookUser?){

        if(friend != null) {

            val isSelected = !selectedUsers.contains(friend)

            val type = (this.activity as app.simone.multiplayer.view.pager.MultiplayerPagerActivity).type

            if(type == app.simone.multiplayer.model.MultiplayerType.INSTANT) {
                selectedUsers.clear()
            }

            if(isSelected && selectedUsers.count() < Constants.MAX_FRIENDS_PER_MATCH)  {
                selectedUsers.add(friend)
            } else {
                selectedUsers.remove(friend)
            }

            btnPlay?.isEnabled = selectedUsers.count() > 0

            if(type == app.simone.multiplayer.model.MultiplayerType.INSTANT) {
                adapter?.notifyDataSetChanged()
            } else if(type == app.simone.multiplayer.model.MultiplayerType.NEARBY){
                val pos = friends.indexOf(friend)
                val visiblePosition = listView?.firstVisiblePosition

                if(visiblePosition != null) {
                    val view = listView?.getChildAt(pos - visiblePosition)
                    adapter?.getView(pos, view, listView)
                }
            }
        }
    }

    fun setUser() {
        val profile = com.facebook.Profile.getCurrentProfile()
        currentUser = app.simone.multiplayer.model.FacebookUser(profile.id, profile.name)
    }
}
