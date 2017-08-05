package app.simone.multiplayer.view.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter


/**
 * Created by nicola on 02/08/2017.
 */
class MultiplayerPagerAdapter(fm: android.support.v4.app.FragmentManager, private val fragments: List<FragmentContainer>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return this.fragments[position].fragment
    }

    override fun getCount(): Int {
        return fragments.count()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragments[position].title
    }
}