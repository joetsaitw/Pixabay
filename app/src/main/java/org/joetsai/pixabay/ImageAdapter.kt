package org.joetsai.pixabay

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.footer_progressbar.view.*
import kotlinx.android.synthetic.main.item_image.view.*
import org.joetsai.pixabay.Constants.Companion.GRID_SPAN_COUNT
import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.util.inflate

/**
 * Created by Joe on 2017/12/10.
 */

class ImageAdapter(private val onLoadMoreListener: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    // 正在讀取中
    private var isLoading = false

    //private var lastPos

    //
    private var images = ArrayList<Image>()

    companion object {
        val TYPE_IMAGE = 0
        val TYPE_PROGRESSBAR = 1
    }

//    var images = mutableListOf<Image>()


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> {
                val inflatedView = parent?.inflate(R.layout.item_image, false)
                ImageViewHolder(inflatedView)
            }
            else -> {
                val inflatedView = parent?.inflate(R.layout.footer_progressbar, false)
                ProgressbarViewHolder(inflatedView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(images[position])
            }
            is ProgressbarViewHolder -> {

            }
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)

        val layoutManager = recyclerView?.layoutManager

        if (layoutManager is GridLayoutManager) {

            // 限制 Progressbar's size = 3, Image's size = 1
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (getItemViewType(position)) {
                        TYPE_PROGRESSBAR -> {
                            GRID_SPAN_COUNT
                        }
                        else -> {
                            1
                        }
                    }
                }
            }

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    println("last index:" + layoutManager.findLastVisibleItemPosition())
                    println("itemCount = $itemCount")

                    val isEnd = layoutManager.findLastVisibleItemPosition() == itemCount - 1

                    if (!isLoading && isEnd) {
                        //notifyItemInserted(itemCount)
                        //images.add(null)
                        isLoading = true
                        onLoadMoreListener()
                    }
                }
            })
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == images.size) {
            TYPE_PROGRESSBAR
        } else {
            TYPE_IMAGE
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading) {
            images.size + 1
        } else {
            images.size
        }
    }

    fun replaceImages(hits: List<Image>) {
        images.clear()
        images.addAll(hits)
    }

    fun addImages(hits: List<Image>) {
        images.addAll(hits)
        notifyDataSetChanged()
    }


    class ImageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        private val imageView = itemView!!.image

        fun bind(image: Image) {
            Glide.with(itemView.context)
                    .load(image.previewURL)
                    // .apply(RequestOptions()
                    //.centerCrop()
                    //.placeholder(ColorDrawable(ContextCompat.getColor(itemView.context, R.color.colorPrimary))))
                    .into(imageView)
        }
    }


    class ProgressbarViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.progressbar.visibility = View.VISIBLE
        }
    }
}