package com.kotlin.mathness

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.christophesmet.android.views.maskableframelayout.MaskableFrameLayout
import com.kotlin.mathness.application.Mathness
import com.kotlin.mathness.util.Animations
import com.kotlin.mathness.util.Constants.EASY
import com.kotlin.mathness.util.Constants.HARD
import com.kotlin.mathness.util.Constants.MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_CURRENT_VICTORIES_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_CURRENT_VICTORIES_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_CURRENT_VICTORIES_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_DIFFICULTY
import com.kotlin.mathness.util.Constants.PREFERENCE_LEVEL_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_LEVEL_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_LEVEL_MEDIUM
import com.kotlin.mathness.util.Util
import com.kotlin.mathness.view.BackgroundAnimation
import com.yangp.ypwaveview.YPWaveView

@Suppress("PrivatePropertyName", "LocalVariableName")
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private lateinit var math: Mathness
    private lateinit var iv_logo_splash: List<ImageView>
    private val tv_level_easy by lazy { findViewById<TextView>(R.id.tv_level_easy_splash).apply { text = Util.getPrefInt(context,PREFERENCE_LEVEL_EASY, 1).toString() } }
    private val tv_level_medium  by lazy { findViewById<TextView>(R.id.tv_level_medium_splash).apply { text = Util.getPrefInt(context,PREFERENCE_LEVEL_MEDIUM, 1).toString() } }
    private val tv_level_hard  by lazy { findViewById<TextView>(R.id.tv_level_hard_splash).apply { text = Util.getPrefInt(context,PREFERENCE_LEVEL_HARD, 1).toString() } }
    private val v_click_splash by lazy { findViewById<View>(R.id.v_click_splash) }
    private val iv_difficulty_select by lazy { findViewById<ImageView>(R.id.iv_difficulty_select_splash) }
    private val iv_difficulty_easy by lazy { findViewById<ImageView>(R.id.iv_difficulty_easy_splash) }
    private val iv_difficulty_medium by lazy { findViewById<ImageView>(R.id.iv_difficulty_medium_splash) }
    private val iv_difficulty_hard by lazy { findViewById<ImageView>(R.id.iv_difficulty_hard_splash) }
    private val rl_bt_difficulty_easy by lazy { findViewById<RelativeLayout>(R.id.rl_bt_difficulty_easy_splash) }
    private val rl_bt_difficulty_medium by lazy { findViewById<RelativeLayout>(R.id.rl_bt_difficulty_medium_splash) }
    private val rl_bt_difficulty_hard by lazy { findViewById<RelativeLayout>(R.id.rl_bt_difficulty_hard_splash) }
    private val iv_bt_difficulty_easy by lazy { findViewById<ImageView>(R.id.iv_bt_difficulty_easy_splash) }
    private val iv_bt_difficulty_medium by lazy { findViewById<ImageView>(R.id.iv_bt_difficulty_medium_splash) }
    private val iv_bt_difficulty_hard by lazy { findViewById<ImageView>(R.id.iv_bt_difficulty_hard_splash) }
    private val mfl_level_easy by lazy { findViewById<MaskableFrameLayout>(R.id.mfl_level_easy_splash) }
    private val mfl_level_medium by lazy { findViewById<MaskableFrameLayout>(R.id.mfl_level_medium_splash) }
    private val mfl_level_hard by lazy { findViewById<MaskableFrameLayout>(R.id.mfl_level_hard_splash) }
    private val ypwv_level_easy by lazy { findViewById<YPWaveView>(R.id.ypwv_level_easy_splash) }
    private val ypwv_level_medium by lazy { findViewById<YPWaveView>(R.id.ypwv_level_medium_splash) }
    private val ypwv_level_hard by lazy { findViewById<YPWaveView>(R.id.ypwv_level_hard_splash) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        math = applicationContext as Mathness
        setContentView(R.layout.activity_splash)
        findViewById<BackgroundAnimation>(R.id.rl_bg_animation).let {
            Handler(Looper.getMainLooper()).postDelayed({ it.startAnimation() }, 50)
        }
        iv_logo_splash = listOf(
            findViewById(R.id.iv_logo_1_splash),
            findViewById(R.id.iv_logo_2_splash),
            findViewById(R.id.iv_logo_3_splash),
            findViewById(R.id.iv_logo_4_splash),
            findViewById(R.id.iv_logo_5_splash),
            findViewById(R.id.iv_logo_6_splash),
            findViewById(R.id.iv_logo_7_splash),
            findViewById(R.id.iv_logo_8_splash),
            findViewById(R.id.iv_logo_9_splash)
        )
        arrayOf(ypwv_level_easy,ypwv_level_medium,ypwv_level_hard).forEach{
            it.setWaveVector(60f)
        }
        ypwv_level_easy.max = Util.fib((maxOf(Util.getPrefInt(this, PREFERENCE_LEVEL_EASY, 1), 1)+3)) * 10
        ypwv_level_easy.progress = Util.getPrefInt(this, PREFERENCE_CURRENT_VICTORIES_EASY)*10
        ypwv_level_medium.max = Util.fib((maxOf(Util.getPrefInt(this, PREFERENCE_LEVEL_MEDIUM, 1), 1)+3)) * 10
        ypwv_level_medium.progress = Util.getPrefInt(this, PREFERENCE_CURRENT_VICTORIES_MEDIUM)*10
        ypwv_level_hard.max = Util.fib((maxOf(Util.getPrefInt(this, PREFERENCE_LEVEL_HARD, 1), 1)+3)) * 10
        ypwv_level_hard.progress = Util.getPrefInt(this, PREFERENCE_CURRENT_VICTORIES_HARD)*10
        val tv_version = findViewById<TextView>(R.id.tv_version_splash)
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            tv_version.text = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            tv_version.text = ""
        }
        arrayOf(tv_level_easy, tv_level_medium, tv_level_hard, tv_version).forEach {
            it.typeface = math.tf_handgoal
        }
        if (math.b_splash) {
            skipSplash()
        }
        else {
            val h = Handler(Looper.getMainLooper())
            iv_logo_splash.forEach { it.visibility = View.INVISIBLE }
            iv_logo_splash.forEachIndexed { index, imageView ->
                h.postDelayed({
                    Animations.enterLogoSplash(imageView, 800)
                }, 500 + index * 250L)
            }
            h.postDelayed({ menu(false) },3000)
            v_click_splash.setOnClickListener{
                h.removeCallbacksAndMessages(null)
                skipSplash()
            }
        }
    }

    private fun skipSplash() {
        findViewById<View>(R.id.v_click_splash).setOnClickListener(null)
        iv_logo_splash.forEach {
            it.clearAnimation()
            it.visibility = View.VISIBLE
        }
        iv_logo_splash.forEachIndexed { index, imageView ->
            Handler(Looper.getMainLooper()).postDelayed({
                Animations.pulse(imageView, 1200, 0.95f)
            }, (50 + 250 * index).toLong())
        }
        menu(true)
    }

    private fun menu(skip: Boolean) {
        math.b_splash = true
        var current_difficulty = math.difficulty
        v_click_splash.setOnClickListener(null)
        val m1 = ColorMatrix()
        m1.setSaturation(0.2f)
        val m2 = ColorMatrix()
        m2.setSaturation(1f)
        val f1 = ColorMatrixColorFilter(m1)
        val f2 = ColorMatrixColorFilter(m2)
        fun disableDifficulty(image: ImageView, button: ImageView, level: MaskableFrameLayout) {
            Animations.fadeFromTo(image, 600, 1f, 0.5f)
            button.clearAnimation()
            level.clearAnimation()
            button.colorFilter = f1
            level.visibility = View.INVISIBLE
        }
        fun enableDifficulty(image: ImageView, button: ImageView, level: MaskableFrameLayout, rotate_from: Float, rotate_to: Float, new_difficulty: String) {
            button.clearAnimation()
            level.clearAnimation()
            Animations.rotateFromTo(iv_difficulty_select, 600, rotate_from, rotate_to)
            Animations.fadeFromTo(image, 600, 0.5f, 1f)
            button.colorFilter = f2
            Animations.pulseCharacter(button)
            level.visibility = View.VISIBLE
            Animations.pulseLevel(level)
            current_difficulty = new_difficulty
        }
        fun switchDifficulty(
            from_image: ImageView, to_image: ImageView, from_button: ImageView, to_button: ImageView,
            from_level: MaskableFrameLayout, to_level: MaskableFrameLayout, rotate_from: Float, rotate_to: Float, new_difficulty: String
        ) {
            disableDifficulty(from_image,from_button,from_level)
            enableDifficulty(to_image,to_button,to_level,rotate_from,rotate_to,new_difficulty)
        }
        rl_bt_difficulty_easy.setOnClickListener{
            when (current_difficulty) {
                MEDIUM -> {
                    switchDifficulty(iv_difficulty_medium, iv_difficulty_easy, iv_bt_difficulty_medium, iv_bt_difficulty_easy, mfl_level_medium, mfl_level_easy, 0f, -50f, EASY)
                }
                HARD -> {
                    switchDifficulty(iv_difficulty_hard, iv_difficulty_easy, iv_bt_difficulty_hard, iv_bt_difficulty_easy, mfl_level_hard, mfl_level_easy, 50f, -50f, EASY)
                }
            }
        }
        rl_bt_difficulty_medium.setOnClickListener{
            when (current_difficulty) {
                EASY -> {
                    switchDifficulty(iv_difficulty_easy, iv_difficulty_medium, iv_bt_difficulty_easy, iv_bt_difficulty_medium, mfl_level_easy, mfl_level_medium, -50f, 0f, MEDIUM)
                }
                HARD -> {
                    switchDifficulty(iv_difficulty_hard, iv_difficulty_medium, iv_bt_difficulty_hard, iv_bt_difficulty_medium, mfl_level_hard, mfl_level_medium, 50f, 0f, MEDIUM)
                }
            }
        }
        rl_bt_difficulty_hard.setOnClickListener{
            when (current_difficulty) {
                EASY -> {
                    switchDifficulty(iv_difficulty_easy, iv_difficulty_hard, iv_bt_difficulty_easy, iv_bt_difficulty_hard, mfl_level_easy, mfl_level_hard, -50f, 50f, HARD)
                }
                MEDIUM -> {
                    switchDifficulty(iv_difficulty_medium, iv_difficulty_hard, iv_bt_difficulty_medium, iv_bt_difficulty_hard, mfl_level_medium, mfl_level_hard, 0f, 50f, HARD)
                }
            }
        }
        arrayOf(iv_bt_difficulty_easy,iv_bt_difficulty_medium,iv_bt_difficulty_hard).forEach {
            it.colorFilter = f1
        }
        arrayOf(tv_level_easy,tv_level_medium,tv_level_hard,iv_bt_difficulty_easy,iv_bt_difficulty_medium,iv_bt_difficulty_hard).forEach {
            it.clearAnimation()
        }
        arrayOf(iv_difficulty_easy,iv_difficulty_medium,iv_difficulty_hard).forEach {
            Animations.fadeFromTo(it,0,0.5f,0.5f)
        }
        arrayOf(mfl_level_easy,mfl_level_medium,mfl_level_hard).forEach {
            it.visibility = View.INVISIBLE
        }
        when (current_difficulty) {
            EASY -> {
                enableDifficulty(iv_difficulty_easy,iv_bt_difficulty_easy,mfl_level_easy,-50f,-50f,EASY)
            }
            MEDIUM -> {
                enableDifficulty(iv_difficulty_medium,iv_bt_difficulty_medium,mfl_level_medium,0f,0f,MEDIUM)
            }
            HARD -> {
                enableDifficulty(iv_difficulty_hard,iv_bt_difficulty_hard,mfl_level_hard,50f,50f,HARD)
            }
        }
        Animations.blinkCharacter(iv_bt_difficulty_easy,R.drawable.character_easy_button,R.drawable.character_easy_blink_button,Handler(Looper.getMainLooper()))
        Animations.blinkCharacter(iv_bt_difficulty_medium,R.drawable.character_medium_button,R.drawable.character_medium_blink_button,Handler(Looper.getMainLooper()))
        Animations.blinkCharacter(iv_bt_difficulty_hard,R.drawable.character_hard_button,R.drawable.character_hard_blink_button,Handler(Looper.getMainLooper()))
        val rl_menu = findViewById<RelativeLayout>(R.id.rl_menu_splash)
        rl_menu.visibility = View.INVISIBLE
        if (!skip)
            Animations.slideUpIn(rl_menu,1000)
        else
            rl_menu.visibility = View.VISIBLE
        val iv_bt_play = findViewById<ImageView>(R.id.iv_bt_play_splash)
        iv_bt_play.setOnClickListener{
            math.difficulty = current_difficulty
            getSharedPreferences("prefs", MODE_PRIVATE).edit().putString(PREFERENCE_DIFFICULTY,current_difficulty).apply()
            val i = Intent(this@SplashActivity, GameActivity::class.java)
            startActivity(i)
            handleTransition()
        }
        Animations.pulse(iv_bt_play,1000,1.2f)
        findViewById<ImageView>(R.id.iv_stats_splash).setOnClickListener { stats() }
    }

    private fun stats()
    {
        val i = Intent(this@SplashActivity, StatsActivity::class.java)
        startActivity(i)
        handleTransition()
    }

    private fun handleTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.enter_from_right, R.anim.exit_to_left)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        finish()
    }
}
