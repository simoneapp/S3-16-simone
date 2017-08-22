package app.simone.multiplayer.view

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.simone.R
import com.bumptech.glide.Glide

/**
 * Created by nicola on 22/08/2017.
 */
class FriendsCellFiller {
    companion object {

        fun setSelected(convertView: View?, isSelected: Boolean, activity: Activity?) {
            if(isSelected){
                convertView?.background = activity?.resources?.getDrawable(R.color.myGreen)
            } else {
                convertView?.background = activity?.resources?.getDrawable(R.color.myWhite)
            }
        }

        fun setImage(convertView: View?, url: String?, activity: Activity?){

            val imgProfile = convertView!!.findViewById(R.id.img_profile) as ImageView

            if(url != null) {
                Glide.with(activity).load(url).into(imgProfile)
            }
        }

        fun setName(convertView: View?, name: String?) {
            val txvName = convertView?.findViewById(R.id.text_name) as TextView
            txvName.text = name
        }

    }
}