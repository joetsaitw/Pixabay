package org.joetsai.pixabay

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.footer_progressbar.view.*
import kotlinx.android.synthetic.main.item_image.view.*
import org.joetsai.pixabay.Constants.GRID_SPAN_COUNT
import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.util.inflate


class ImageAdapter(private val onLoadMoreListener: () -> Unit,
                   private val onItemClickedListener: (position: Int, imageList: ArrayList<Image>) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 正在讀取中
    private var isLoading = false

    // 還有下頁資料
    private var hasNextPage = true


    private var test: (Boolean) -> Boolean = { isEnabled -> isEnabled }


    private var imageList = ArrayList<Image>()

    companion object {
        val TYPE_IMAGE = 0
        val TYPE_PROGRESSBAR = 1
    }

//    var imageList = mutableListOf<Image>()


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

//        if(holder is ImageViewHolder) {
//
//        }


        when (holder) {
            is ImageViewHolder -> {
                holder.bind(imageList[position])

            }
            is ProgressbarViewHolder -> {

            }
        }

        holder?.itemView?.setOnClickListener { onItemClickedListener(position, imageList) }

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

                    val isEnd = layoutManager.findLastVisibleItemPosition() == (itemCount - 1)

                    if (!isLoading && isEnd && hasNextPage) {
                        onLoadMoreListener()
                    }
                }
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == imageList.size) {
            TYPE_PROGRESSBAR
        } else {
            TYPE_IMAGE
        }
    }

    /**
     * isLoading = true 的時候
     * 多加一個 item 給 ProgressBar使用
     */
    override fun getItemCount(): Int {
        return if (isLoading) {
            imageList.size + 1
        } else {
            imageList.size
        }
    }

    fun replaceImages(hits: List<Image>) {
        hasNextPage = true
        imageList.clear()
        imageList.addAll(hits)
        notifyDataSetChanged()
    }

    fun addImages(hits: List<Image>) {
        val positionStart = imageList.size
        imageList.addAll(hits)
        notifyItemRangeInserted(positionStart, hits.size)
    }

    /**
     * 不需要加載更多了
     */
    fun noMorePages() {
        hasNextPage = false
        notifyItemRemoved(imageList.size)
    }

    fun enableProgressBar(isEnabled: Boolean) {
        this.isLoading = isEnabled
//        val position = if (isEnabled) imageList.size else imageList.size -1
//        notifyItemChanged(position)

        notifyItemChanged(imageList.size)
        println("enableProgressBar size:" + imageList.size)
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

            //itemView.setOnClickListener { onItemClickedListener(adapterPosition) }

        }
    }


    class ProgressbarViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.progressbar.visibility = View.VISIBLE
        }
    }
}