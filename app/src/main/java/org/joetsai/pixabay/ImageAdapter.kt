package org.joetsai.pixabay

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by Joe on 2017/12/10.
 */

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflatedView = parent?.inflate(R.layout.item_image, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return 50
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        init {

        }
    }
}