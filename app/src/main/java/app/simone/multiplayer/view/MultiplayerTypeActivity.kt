package app.simone.multiplayer.view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import app.simone.DistributedSimon.Activities.ColorSetUpActivity
import app.simone.R
import app.simone.multiplayer.model.MultiplayerType
import app.simone.multiplayer.view.pager.MultiplayerPagerActivity
import app.simone.shared.main.FullscreenBaseGameActivity

class MultiplayerTypeActivity : FullscreenBaseGameActivity() {

    override fun setSubclassContentView() {
        setContentView(R.layout.activity_multiplayer_type)
        mContentView = findViewById(R.id.multiplayer_fullscreen_content)
    }

    override fun backTransition() {
        overridePendingTransition(R.anim.slide_up, R.anim.slide_up_existing)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnInstant = findViewById(R.id.multiplayer_fab_instant) as FloatingActionButton

        btnInstant.setOnClickListener({

            this.call(MultiplayerType.INSTANT)
        })

        val btnNearby = findViewById(R.id.multiplayer_fab_nearby) as FloatingActionButton
        btnNearby.setOnClickListener({
            this.call(MultiplayerType.NEARBY)
        })
    }

    fun call(type : MultiplayerType) {
        val intent = Intent(applicationContext, ColorSetUpActivity::class.java)
        intent.putExtra("source", type.name)
        startActivity(intent)
    }

}
