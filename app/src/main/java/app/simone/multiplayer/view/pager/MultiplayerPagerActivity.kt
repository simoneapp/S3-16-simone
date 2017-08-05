package app.simone.multiplayer.view.pager

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import app.simone.R
import app.simone.multiplayer.messages.FbOnActivityResultMsg
import app.simone.multiplayer.model.MultiplayerType
import app.simone.multiplayer.view.invites.InvitesFragment
import app.simone.multiplayer.view.newmatch.FriendsListFragment
import app.simone.shared.application.App
import com.facebook.Profile


class MultiplayerPagerActivity : AppCompatActivity() {

    var type : MultiplayerType? = null
    var friendsList = FriendsListFragment()
    var invites = InvitesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer_pager)

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        val viewPager = findViewById(R.id.viewpager) as ViewPager
        viewPager.adapter = MultiplayerPagerAdapter(supportFragmentManager,
                arrayListOf(
                        FragmentContainer(friendsList, "New match"),
                        FragmentContainer(invites, "Invites")
                ))

        // Give the TabLayout the ViewPager
        val tabLayout = findViewById(R.id.sliding_tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)

        type = MultiplayerType.valueOf(intent.getStringExtra("source"))

        setFacebookViewVisible(Profile.getCurrentProfile() == null)

        val btnUser = findViewById(R.id.user_button) as Button
        btnUser.setOnClickListener({
            setFacebookViewVisible(true)
        })
    }

    fun setFacebookViewVisible(visible : Boolean) {

        this.runOnUiThread {
            val fbLayout = findViewById(R.id.layout_facebook)
            val pagerLayout = findViewById(R.id.layout_pager)

            if(visible) {
                pagerLayout.visibility = View.INVISIBLE
                fbLayout.visibility = View.VISIBLE
            } else {
                pagerLayout.visibility = View.VISIBLE
                fbLayout.visibility = View.INVISIBLE
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val actor = app.simone.shared.utils.Utilities.getActorByName(app.simone.shared.utils.Constants.PATH_ACTOR + app.simone.shared.utils.Constants.FBVIEW_ACTOR_NAME,
                App.getInstance().actorSystem)
        actor.tell(FbOnActivityResultMsg(requestCode, resultCode, data), akka.actor.ActorRef.noSender())
    }

}
