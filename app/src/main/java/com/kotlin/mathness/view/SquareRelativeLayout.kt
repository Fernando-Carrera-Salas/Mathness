package com.kotlin.mathness.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * A custom [RelativeLayout]. Its height will always match its width.
 */
class SquareRelativeLayout : RelativeLayout {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}