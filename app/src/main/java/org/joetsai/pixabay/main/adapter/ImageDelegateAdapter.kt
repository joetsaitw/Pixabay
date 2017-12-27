package org.joetsai.pixabay.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image.view.*
import org.joetsai.pixabay.R
import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.util.inflate


class ImageDelegateAdapter(val onItemClickedListener: (position: Int) -> Unit) : ViewTypeDelegateAdapter {


    override fun onCreateViewHolder(parent: ViewGroup) = ImageViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as ImageViewHolder
        holder.bind(item as Image)
    }

    inner class ImageViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_image)) {

        private val imageView = itemView.image

        fun bind(image: Image) {
            Glide.with(itemView.context)
                    .load(image.previewURL)
                    .into(imageView)

            itemView.setOnClickListener { onItemClickedListener(adapterPosition) }
        }
    }
}