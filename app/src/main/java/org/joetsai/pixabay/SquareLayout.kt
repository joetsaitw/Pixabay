package org.joetsai.pixabay

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet

/**
 * Created by Joe on 2017/12/17.
 */

class SquareLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}