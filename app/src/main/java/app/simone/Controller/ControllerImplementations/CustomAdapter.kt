package app.simone.Controller.ControllerImplementations

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView

/**
 * Created by gzano on 30/06/2017.
 */
class CustomAdapter(var context: Context) { //Adapter: how to fit elemts in the view
                                            //
    internal var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }
}