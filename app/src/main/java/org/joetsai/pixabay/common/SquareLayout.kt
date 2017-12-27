package org.joetsai.pixabay.common

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet

/**
 * 正方形的Layout
 */

class SquareLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}