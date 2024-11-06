package com.kotlin.mathness.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import com.kotlin.mathness.R
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

@Suppress("LocalVariableName", "LiftReturnOrAssignment")
object Animations {
    fun slideUpIn(view: View, duration: Int) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f,
            0f,
            view.height.toFloat(),
            0f
        )
        animate.duration = duration.toLong()
        animate.fillAfter = true
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.VISIBLE
                view.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
        view.startAnimation(animate)
    }

    fun backgroundNumbers(view: View, duration: Int) {
        val pulse = ScaleAnimation(
            1f,
            0.9f,
            1f,
            0.9f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        pulse.repeatCount = Animation.INFINITE
        pulse.repeatMode = Animation.REVERSE

        val rotate = RotateAnimation(
            0f, (-180..180).random().toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.repeatCount = Animation.INFINITE
        rotate.repeatMode = Animation.REVERSE

        val set = AnimationSet(true)
        set.addAnimation(pulse)
        set.duration = duration.toLong()
        set.addAnimation(rotate)
        view.startAnimation(set)
    }

    fun pulse(view: View, duration: Long, size: Float) {
        val pulse = ScaleAnimation(
            1f,
            size,
            1f,
            size,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        pulse.duration = duration
        pulse.interpolator = OvershootInterpolator()
        pulse.repeatCount = Animation.INFINITE
        pulse.repeatMode = Animation.REVERSE
        view.startAnimation(pulse)
    }

    fun pulseOnce(view: View, duration: Long, size: Float) {
        val pulse = ScaleAnimation(
            1f,
            size,
            1f,
            size,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        pulse.duration = duration
        pulse.interpolator = OvershootInterpolator()
        pulse.repeatMode = Animation.REVERSE
        pulse.repeatCount = 2
        view.startAnimation(pulse)
    }

    fun blinkCharacter(iv: ImageView, normal:Int, blink:Int, h: Handler) {
        var r = Runnable{}
        r = Runnable{
            iv.setImageResource(blink)
            h.postDelayed({iv.setImageResource(normal)},100)
            h.postDelayed(r,(2000..6000).random().toLong())
        }
        h.postDelayed(r,(2000..6000).random().toLong())
        iv.setImageResource(normal)
    }

    fun pulseCharacter(view: View) {
        val pulse = ScaleAnimation(
            1f,
            1.2f,
            1f,
            1.2f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        pulse.duration = 2000
        pulse.interpolator = OvershootInterpolator()
        pulse.repeatCount = Animation.INFINITE
        pulse.repeatMode = Animation.REVERSE


        val rotate = RotateAnimation(
            10f, -10f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.duration = 3000
        rotate.interpolator = LinearInterpolator()
        rotate.repeatCount = Animation.INFINITE
        rotate.repeatMode = Animation.REVERSE

        val set = AnimationSet(false)
        set.addAnimation(pulse)
        set.addAnimation(rotate)
        view.startAnimation(set)
    }

    fun pulseLevel(view: View) {
        val pulse = ScaleAnimation(
            0.9f,
            1f,
            0.9f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        pulse.duration = 500
        pulse.interpolator = OvershootInterpolator()
        pulse.repeatCount = Animation.INFINITE
        pulse.repeatMode = Animation.REVERSE


        val rotate = RotateAnimation(
            -5f, 5f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.0f
        )
        rotate.duration = 3000
        rotate.interpolator = LinearInterpolator()
        rotate.repeatCount = Animation.INFINITE
        rotate.repeatMode = Animation.REVERSE

        val set = AnimationSet(false)
        set.addAnimation(pulse)
        set.addAnimation(rotate)
        view.startAnimation(set)
    }

    fun pulseTimer(view: View, duration: Int) {
        val pulse = ScaleAnimation(
            1f,
            1.2f,
            1f,
            1.2f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        pulse.duration = duration.toLong()
        pulse.interpolator = OvershootInterpolator()
        pulse.repeatCount = 1
        pulse.repeatMode = Animation.REVERSE
        view.startAnimation(pulse)
    }

    fun enterLogoSplash(view: View, duration: Int) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            (-view.width..view.width).random().toFloat(),
            0f,
            -view.height.toFloat(),
            0f
        )
        animate.duration = duration.toLong()
        animate.interpolator = AnticipateOvershootInterpolator()
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.VISIBLE
                view.clearAnimation()
                pulse(view,1200,0.95f)
            }

            override fun onAnimationRepeat(animation: Animation) {
            }
        })
        view.startAnimation(animate)
    }

    fun rotateFromTo(view: View, duration: Int, from:Float, to: Float) {
        val rotate = RotateAnimation(
            from, to,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.fillAfter = true
        rotate.interpolator = OvershootInterpolator()
        rotate.duration = duration.toLong()
        view.startAnimation(rotate)
    }

    fun fadeFromTo(view: View, duration: Int, from: Float, to: Float) {
        view.visibility = View.VISIBLE
        val animate = AlphaAnimation(from, to)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    fun shine(view: View, reversed: Boolean) {
        view.visibility = View.VISIBLE
        val rotate = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.interpolator = LinearInterpolator()
        rotate.repeatCount = Animation.INFINITE
        rotate.repeatMode = Animation.RESTART
        rotate.duration = 10000

        val alpha: AlphaAnimation
        if (reversed) {
            alpha = AlphaAnimation(1f,0.5f)
        }
        else {
            alpha = AlphaAnimation(0.5f,1f)
        }
        alpha.repeatCount = Animation.INFINITE
        alpha.repeatMode = Animation.REVERSE
        alpha.duration = 1000

        val scale: ScaleAnimation
        if (reversed) {
            scale = ScaleAnimation(
                1f,
                1.5f,
                1f,
                1.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
        }
        else {
            scale = ScaleAnimation(
                1.5f,
                1f,
                1.5f,
                1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
        }
        scale.repeatCount = Animation.INFINITE
        scale.repeatMode = Animation.REVERSE
        scale.duration = 1000

        val set = AnimationSet(false)
        set.addAnimation(rotate)
        set.addAnimation(alpha)
        set.addAnimation(scale)
        view.startAnimation(set)
    }

    fun stars(layout: RelativeLayout, center_x: Float, center_y: Float, h_delay: Handler) {
        var angle = 0.0
        for (i in 0..29) {
            h_delay.postDelayed({
                val iv1 = ImageView(layout.context)
                val iv2 = ImageView(layout.context)
                val iv3 = ImageView(layout.context)
                val iv4 = ImageView(layout.context)
                iv1.setImageResource(R.drawable.star)
                iv2.setImageResource(R.drawable.star)
                iv3.setImageResource(R.drawable.star)
                iv4.setImageResource(R.drawable.star)
                iv1.x = center_x-16
                iv1.y = center_y-16
                iv2.x = center_x-16
                iv2.y = center_y-16
                iv3.x = center_x-16
                iv3.y = center_y-16
                iv4.x = center_x-16
                iv4.y = center_y-16
                layout.addView(iv1)
                layout.addView(iv2)
                layout.addView(iv3)
                layout.addView(iv4)
                iv1.setColorFilter(layout.context.getColor(R.color.mth_yellow))
                iv2.setColorFilter(layout.context.getColor(R.color.mth_yellow))
                iv3.setColorFilter(layout.context.getColor(R.color.mth_yellow))
                iv4.setColorFilter(layout.context.getColor(R.color.mth_yellow))
                val startValues = doubleArrayOf(iv1.x.toDouble()-16,iv1.y.toDouble()-16,
                                                iv2.x.toDouble()-16,iv2.y.toDouble()-16,
                                                iv3.x.toDouble()-16,iv3.y.toDouble()-16,
                                                iv4.x.toDouble()-16,iv4.y.toDouble()-16)
                var endx1: Double
                var endy1: Double
                var endx2: Double
                var endy2: Double
                var endx3: Double
                var endy3: Double
                var endx4: Double
                var endy4: Double
                angle+=10
                endx1 = cos(angle) *layout.height*1.5;
                endy1 = sin(angle) *layout.height*1.5;
                endx2 = cos(angle+Math.PI/2) *layout.height*1.5;
                endy2 = sin(angle+Math.PI/2) *layout.height*1.5;
                endx3 = cos(angle+Math.PI) *layout.height*1.5;
                endy3 = sin(angle+Math.PI) *layout.height*1.5;
                endx4 = cos(angle+Math.PI+Math.PI/2) *layout.height*1.5;
                endy4 = sin(angle+Math.PI+Math.PI/2) *layout.height*1.5;
                var endValues = doubleArrayOf(endx1,endy1,endx2,endy2,endx3,endy3,endx4,endy4)
                val va = ValueAnimator.ofObject(DoubleArrayEvaluator(),startValues,endValues)
                va.setDuration(6000)
                fadeFromTo(iv1,1000,0.75f,0f)
                fadeFromTo(iv2,1000,0.75f,0f)
                fadeFromTo(iv3,1000,0.75f,0f)
                fadeFromTo(iv4,1000,0.75f,0f)
                va.interpolator = DecelerateInterpolator()
                va.addUpdateListener { valueAnimator ->

                    val animatedValue = valueAnimator.animatedValue as DoubleArray
                    iv1.x = animatedValue[0].toFloat()
                    iv1.y = animatedValue[1].toFloat()
                    iv2.x = animatedValue[2].toFloat()
                    iv2.y = animatedValue[3].toFloat()
                    iv3.x = animatedValue[4].toFloat()
                    iv3.y = animatedValue[5].toFloat()
                    iv4.x = animatedValue[6].toFloat()
                    iv4.y = animatedValue[7].toFloat()
                    iv1.rotation+=5
                    iv2.rotation-=5
                    iv3.rotation+=5
                    iv4.rotation-=5
                }
                va.addListener(object : AnimatorListenerAdapter(
                ) {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        iv1.clearAnimation()
                        iv2.clearAnimation()
                        iv3.clearAnimation()
                        iv4.clearAnimation()
                        fadeFromTo(iv1,1000,0.75f,0f)
                        fadeFromTo(iv2,1000,0.75f,0f)
                        fadeFromTo(iv3,1000,0.75f,0f)
                        fadeFromTo(iv4,1000,0.75f,0f)
                        iv1.x = center_x-16
                        iv1.y = center_y-16
                        iv2.x = center_x-16
                        iv2.y = center_y-16
                        iv3.x = center_x-16
                        iv3.y = center_y-16
                        iv4.x = center_x-16
                        iv4.y = center_y-16

                        angle+=10
                        endx1 = cos(angle) *layout.height*1.5;
                        endy1 = sin(angle) *layout.height*1.5;
                        endx2 = cos(angle+Math.PI/2) *layout.height*1.5;
                        endy2 = sin(angle+Math.PI/2) *layout.height*1.5;
                        endx3 = cos(angle+Math.PI) *layout.height*1.5;
                        endy3 = sin(angle+Math.PI) *layout.height*1.5;
                        endx4 = cos(angle+Math.PI+Math.PI/2) *layout.height*1.5;
                        endy4 = sin(angle+Math.PI+Math.PI/2) *layout.height*1.5;
                        endValues = doubleArrayOf(endx1,endy1,endx2,endy2,endx3,endy3,endx4,endy4)
                        va.setDuration(6000)
                        va.setObjectValues(startValues,endValues)
                        va.start()
                    }
                })
                va.start()
            },i*200L)
        }
    }
}
