package org.joetsai.pixabay.main.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import org.joetsai.pixabay.common.Constants
import org.joetsai.pixabay.common.Constants.ADAPTER_IMAGE
import org.joetsai.pixabay.common.Constants.ADAPTER_LOADING
import org.joetsai.pixabay.data.Image


class ImageAdapter(private val onLoadMoreListener: () -> Unit,
                   private val onImageClickedListener: (position: Int, imageList: List<Image>) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 正在讀取中
    private var isLoading = false

    // 還有下頁資料
    private var hasNextPage = true


    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    private val loadingItem = object : ViewType {
        override fun getViewType() = ADAPTER_LOADING
    }

    init {
        delegateAdapters.put(ADAPTER_LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ADAPTER_IMAGE, ImageDelegateAdapter(onItemClickedListener = { position ->
            onImageClickedListener(position, getImages())
        }))
        items = ArrayList()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)

        val layoutManager = recyclerView?.layoutManager

        if (layoutManager is GridLayoutManager) {

            // Limit Progressbar's size = 3, Image's size = 1
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (getItemViewType(position)) {
                        ADAPTER_LOADING -> Constants.GRID_SPAN_COUNT
                        else -> 1
                    }
                }
            }

            // Detect the end of scrolling
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val isEnd = layoutManager.findLastVisibleItemPosition() == (itemCount - 1)
                        if (!isLoading && isEnd && hasNextPage) {
                            isLoading = true
                            onLoadMoreListener()
                        }
                    }

                }
            })
        }
    }


    fun enableProgressBar(isEnable: Boolean) {
        this.isLoading = isEnable
        val lastPosition = items.size - 1

        if (isEnable) {
            items.add(loadingItem)
            notifyItemChanged(lastPosition + 1)
        } else {
            items.removeAt(lastPosition)
            notifyItemRemoved(lastPosition)
            notifyItemRangeChanged(lastPosition, items.size)
        }
    }

    fun addImages(images: List<Image>) {
        val positionStart = items.size
        items.addAll(images)
        notifyItemRangeChanged(positionStart, images.size)
    }

    fun clearImages() {
        items.clear()
        notifyDataSetChanged()
    }

    fun replaceImages(images: List<Image>) {
        hasNextPage = true
        items.clear()
        items.addAll(images)
        notifyDataSetChanged()
    }

    /**
     * 已讀取完畢，不需要加載更多了
     */
    fun noMorePages() {
        hasNextPage = false
    }

    fun getImages(): List<Image> =
            items
                    .filter { it.getViewType() == ADAPTER_IMAGE }
                    .map { it as Image }

}