package org.joetsai.pixabay

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pager_image.view.*
import org.joetsai.pixabay.data.Image
import org.joetsai.pixabay.util.inflate


class ImageSliderAdapter(private val context: Context,
                         private val images: ArrayList<Image>,
                         private val onClickListener: () -> Unit) : PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = images.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = container.inflate(R.layout.pager_image, false)

        // ViewPager 本身 OnClickListener 無法觸發
        // 改以此方法去觸發點擊事件
        itemView.setOnClickListener { onClickListener() }

        val imageView = itemView.imageView

        Glide.with(context)
                .load(images[position].webformatURL)
                // .apply(RequestOptions()
                //.centerCrop()
                //.placeholder(ColorDrawable(ContextCompat.getColor(itemView.context, R.color.colorPrimary))))
                .into(imageView)

        container.addView(itemView)
        return itemView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}