package com.kotlin.mathness

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.kotlin.mathness.application.Mathness
import com.kotlin.mathness.util.Animations
import com.kotlin.mathness.util.Constants.PREFERENCES
import com.kotlin.mathness.util.Constants.PREFERENCE_MAX_VICTORY_CHAIN_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_MAX_VICTORY_CHAIN_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_MAX_VICTORY_CHAIN_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_DEFEATS_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_DEFEATS_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_DEFEATS_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_EXTRA_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_EXTRA_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_MEDIUM
import com.kotlin.mathness.view.BackgroundAnimation

@Suppress("PrivatePropertyName")
@SuppressLint("CustomSplashScreen")
class StatsActivity : ComponentActivity() {
    private lateinit var math: Mathness
    private val ANIMATION_DURATION = 2000L
    private val ANIMATION_SCALE = 0.75f
    private val ANIMATION_DELAY = 500L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        math = applicationContext as Mathness
        setContentView(R.layout.activity_stats)
        findViewById<BackgroundAnimation>(R.id.rl_bg_animation).let {
            Handler(Looper.getMainLooper()).postDelayed({ it.startAnimation() }, 50)
        }
        val easyIcons = listOf(
            findViewById<ImageView>(R.id.iv_victories_easy_stats),
            findViewById(R.id.iv_defeats_easy_stats),
            findViewById(R.id.iv_chain_easy_stats)
        )
        val mediumIcons = listOf(
            findViewById<ImageView>(R.id.iv_victories_medium_stats),
            findViewById(R.id.iv_defeats_medium_stats),
            findViewById(R.id.iv_chain_medium_stats),
            findViewById(R.id.iv_extra_medium_stats)
        )
        val hardIcons = listOf(
            findViewById<ImageView>(R.id.iv_victories_hard_stats),
            findViewById(R.id.iv_defeats_hard_stats),
            findViewById(R.id.iv_chain_hard_stats),
            findViewById(R.id.iv_extra_hard_stats)
        )
        animateIcons(easyIcons)
        Handler(Looper.getMainLooper()).postDelayed({ animateIcons(mediumIcons) }, ANIMATION_DELAY)
        Handler(Looper.getMainLooper()).postDelayed({ animateIcons(hardIcons) }, ANIMATION_DELAY * 2)
        setTextValuesAndTypeface()
        findViewById<ImageView>(R.id.iv_bt_back_stats).setOnClickListener { menu() }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { menu() }
        })
    }

    private fun animateIcons(icons: List<ImageView>) {
        icons.forEach { Animations.pulse(it, ANIMATION_DURATION, ANIMATION_SCALE) }
    }

    private fun setTextValuesAndTypeface() {
        fun getStat(prefKey: String) = getSharedPreferences(PREFERENCES, MODE_PRIVATE).getInt(prefKey, 0).toString()
        val tvs = listOf(
            findViewById<TextView>(R.id.tv_victories_easy_stats).apply { text = getStat(PREFERENCE_TOTAL_VICTORIES_EASY) },
            findViewById<TextView>(R.id.tv_victories_medium_stats).apply { text = getStat(PREFERENCE_TOTAL_VICTORIES_MEDIUM) },
            findViewById<TextView>(R.id.tv_victories_hard_stats).apply { text = getStat(PREFERENCE_TOTAL_VICTORIES_HARD) },

            findViewById<TextView>(R.id.tv_defeats_easy_stats).apply { text = getStat(PREFERENCE_TOTAL_DEFEATS_EASY) },
            findViewById<TextView>(R.id.tv_defeats_medium_stats).apply { text = getStat(PREFERENCE_TOTAL_DEFEATS_MEDIUM) },
            findViewById<TextView>(R.id.tv_defeats_hard_stats).apply { text = getStat(PREFERENCE_TOTAL_DEFEATS_HARD) },

            findViewById<TextView>(R.id.tv_chain_easy_stats).apply { text = getStat(PREFERENCE_MAX_VICTORY_CHAIN_EASY) },
            findViewById<TextView>(R.id.tv_chain_medium_stats).apply { text = getStat(PREFERENCE_MAX_VICTORY_CHAIN_MEDIUM) },
            findViewById<TextView>(R.id.tv_chain_hard_stats).apply { text = getStat(PREFERENCE_MAX_VICTORY_CHAIN_HARD) },

            findViewById<TextView>(R.id.tv_extra_medium_stats).apply { text = getStat(PREFERENCE_TOTAL_VICTORIES_EXTRA_MEDIUM) },
            findViewById<TextView>(R.id.tv_extra_hard_stats).apply { text = getStat(PREFERENCE_TOTAL_VICTORIES_EXTRA_HARD) },
        )
        tvs.forEach { it.typeface = math.tf_handgoal }
    }
    private fun menu() {
        val i = Intent(this@StatsActivity, SplashActivity::class.java)
        startActivity(i)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN,R.anim.enter_from_left,R.anim.exit_to_right)
        }
        else
        {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
        finish()
    }
}