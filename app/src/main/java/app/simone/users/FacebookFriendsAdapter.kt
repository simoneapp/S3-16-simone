package app.simone.users

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import app.simone.R
import app.simone.users.model.FacebookFriend
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener




/**
 * Created by nicola on 23/06/2017.
 */

class FacebookFriendsAdapter : ArrayAdapter<FacebookFriend> {

    constructor(context: Context, data: List<FacebookFriend>) : super(context, R.layout.cell_friends) {
        val config = ImageLoaderConfiguration.createDefault(getContext())
        ImageLoader.getInstance().init(config)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_friends, parent, false)
        }

        val friend = getItem(position).let { it } ?: return convertView!!

        val txvName = convertView?.findViewById(R.id.text_name) as TextView
        txvName.text = friend.name

        val imgProfile = convertView?.findViewById(R.id.img_profile) as ImageView

        if(friend?.picture != null) {
            val url = friend!!.picture!!.url

            ImageLoader.getInstance().loadImage(url, object : SimpleImageLoadingListener() {
                override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                    imgProfile.setImageBitmap(loadedImage)
                }
            })
        }

        return convertView!!
    }

}