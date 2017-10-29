package app.simone.settings.view

import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import app.simone.R
import app.simone.settings.model.SimonCreditsColor
import app.simone.shared.utils.AudioPlayer
import java.util.*


class CreditsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)

        SimonCreditsColor.allColors.forEach {
            val image = findViewById(it.getImageViewId()) as ImageView
            image.setImageResource(it.getImageId())

            val button = findViewById(it.buttonId)
            button.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN ->
                        this.showPicture(it)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                        this.hidePicture(it)
                    else -> false
                }
            }
        }

        val creditsButton = findViewById(R.id.credits_fab)
        creditsButton.setOnClickListener { openAppInfo() }
    }

    fun openAppInfo() {
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)

            val textMessage = "Simone for Android v" + pInfo.versionName + "\n\n" +
                    "Homepage: https://simoneapp.github.io\n\n" +
                    "Simone is open source! Take a look to our repositories at https://github.com/simoneapp\n\n" +
                    "Built with ❤️ in San Marino 🇸🇲\n\n" +
                    "© " + Calendar.getInstance().get(Calendar.YEAR) + " Simone Dev Team. All rights reserved. \n\n" +
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, " +
                    "INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR " +
                    "PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE " +
                    "FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR " +
                    "OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER " +
                    "DEALINGS IN THE SOFTWARE."

            val alert = AlertDialog.Builder(this).create()
            alert.setTitle("App Info")
            alert.setMessage(textMessage)

            alert.setButton(Dialog.BUTTON_POSITIVE, "Dismiss", DialogInterface.OnClickListener { _, i ->
                //finish()
            })

            alert.show()

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    fun showPicture(color: SimonCreditsColor): Boolean {
        val button = findViewById(color.buttonId) as Button
        button.alpha = 0.4f
        AudioPlayer().play(applicationContext, color.soundId)
        return true
    }

    fun hidePicture(color: SimonCreditsColor): Boolean {
        val button = findViewById(color.buttonId) as Button
        button.alpha = 1.0f
        return true
    }
}
