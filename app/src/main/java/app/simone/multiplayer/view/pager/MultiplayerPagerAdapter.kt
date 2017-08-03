package app.simone.multiplayer.view.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import app.simone.multiplayer.view.FacebookLoginActivity


/**
 * Created by nicola on 02/08/2017.
 */
class MultiplayerPagerAdapter(fm: android.support.v4.app.FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        val fragment = FacebookLoginActivity()
        //val args = Bundle()
        // Our object is just an integer :-P
        //args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1)
        //fragment.setArguments(args)
        return fragment
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Pagina " + position
    }
}