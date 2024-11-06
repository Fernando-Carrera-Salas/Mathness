package com.kotlin.mathness.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import com.kotlin.mathness.application.Mathness
import com.kotlin.mathness.R
import com.kotlin.mathness.util.Animations
import java.security.SecureRandom
import kotlin.random.Random

@Suppress("LocalVariableName")
class BackgroundAnimation : RelativeLayout {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun startAnimation() {
        val math = context.applicationContext as Mathness
        val h = Handler(Looper.getMainLooper())
        val max_width = this.width
        val max_height = this.height
        for (i in 0..200) {
            val tv = TextView(context)
            tv.text = randomString()
            tv.textSize = (20..30).random().toFloat()
            tv.alpha = (1..10).random().toFloat()/100
            tv.setTextColor(context.getColor(R.color.white))
            tv.x = (0..max_width).random().toFloat()
            tv.y = -100f
            tv.typeface = math.tf_handgoal
            this.addView(tv)
            val va = ValueAnimator.ofFloat(tv.y,max_height.toFloat())
            va.setDuration(55000+(-5000L..5000L).random())
            va.interpolator = LinearInterpolator()
            va.addUpdateListener { valueAnimator ->
                tv.y = valueAnimator.animatedValue as Float
            }
            va.addListener(object : AnimatorListenerAdapter(
            ) {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    tv.text = randomString()
                    tv.x = (0..max_width).random().toFloat()
                    tv.y = -100f
                    va.setFloatValues(tv.y,max_height.toFloat())
                    va.start()
                }
            })
            h.postDelayed({ Animations.backgroundNumbers(tv,(30000..50000).random())},(50L..5000L).random())
            va.setCurrentFraction(Random.nextFloat())
            va.start()
        }
    }

    fun randomString(): String {
        val random = SecureRandom()
        val chars = "0123456789+-x÷=".toCharArray()
        return (1..1).map { chars[random.nextInt(chars.size)] }.joinToString("")
    }
}
