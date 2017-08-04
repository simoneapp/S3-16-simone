package app.simone.multiplayer.view.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import app.simone.multiplayer.view.invites.InvitesFragment
import app.simone.multiplayer.view.newmatch.FriendsListFragment


/**
 * Created by nicola on 02/08/2017.
 */
class MultiplayerPagerAdapter(fm: android.support.v4.app.FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when(position) {
            0 -> { return FriendsListFragment() }
            1 -> { return InvitesFragment() }
        }
        return Fragment()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position) {
            0 -> return "New match"
            1 -> return "Pending invites"
        }
        return ""
    }
}