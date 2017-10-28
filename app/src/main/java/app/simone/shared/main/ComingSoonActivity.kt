package app.simone.shared.main

import android.os.Bundle
import app.simone.R

class ComingSoonActivity : FullscreenBaseGameActivity() {

    override fun setSubclassContentView() {
        setContentView(R.layout.activity_comingsoon)
        mContentView = findViewById(R.id.comingsoon_fullscreen_content)
    }

    override fun backTransition() {
        overridePendingTransition(R.anim.slide_up, R.anim.slide_up_existing)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


}