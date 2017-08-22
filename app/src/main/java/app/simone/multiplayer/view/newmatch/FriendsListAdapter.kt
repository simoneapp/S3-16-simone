package app.simone.multiplayer.view.newmatch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import app.simone.R.layout
import app.simone.multiplayer.model.FacebookUser
import app.simone.multiplayer.view.FriendsCellFiller

/**
 * Created by nicola on 23/06/2017.
 */

class FriendsListAdapter : ArrayAdapter<FacebookUser> {

    var fragment : FriendsListFragment? = null

    constructor(context: Context, data: List<FacebookUser>, fragment : FriendsListFragment) : super(context, layout.cell_friends) {
        this.fragment = fragment
        //val config = ImageLoaderConfiguration.createDefault(getContext())
        //ImageLoader.getInstance().init(config)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var convertView = convertView

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout.cell_friends, parent, false)
        }

        val friend = getItem(position).let { it } ?: return convertView!!

        FriendsCellFiller.setName(convertView, friend.name)
        FriendsCellFiller.setImage(convertView, friend.picture.url, fragment?.activity)
        if(fragment?.selectedUsers != null) {
            FriendsCellFiller.setSelected(convertView, fragment!!.selectedUsers.contains(friend), fragment?.activity)
        }

        return convertView
    }
}