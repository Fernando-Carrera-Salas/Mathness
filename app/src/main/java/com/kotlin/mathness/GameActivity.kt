package com.kotlin.mathness

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.text.isDigitsOnly
import com.christophesmet.android.views.maskableframelayout.MaskableFrameLayout
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.kotlin.mathness.application.Mathness
import com.kotlin.mathness.util.Animations
import com.kotlin.mathness.util.Constants.DIVIDE
import com.kotlin.mathness.util.Constants.EASY
import com.kotlin.mathness.util.Constants.HARD
import com.kotlin.mathness.util.Constants.MEDIUM
import com.kotlin.mathness.util.Constants.MINUS
import com.kotlin.mathness.util.Constants.MULTIPLY
import com.kotlin.mathness.util.Constants.PLUS
import com.kotlin.mathness.util.Constants.PREFERENCE_CURRENT_VICTORIES_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_CURRENT_VICTORIES_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_CURRENT_VICTORIES_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_LEVEL_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_LEVEL_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_LEVEL_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_MAX_VICTORY_CHAIN_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_MAX_VICTORY_CHAIN_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_MAX_VICTORY_CHAIN_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_DEFEATS_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_DEFEATS_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_DEFEATS_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_EASY
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_EXTRA_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_EXTRA_MEDIUM
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_HARD
import com.kotlin.mathness.util.Constants.PREFERENCE_TOTAL_VICTORIES_MEDIUM
import com.kotlin.mathness.util.DoubleArrayEvaluator
import com.kotlin.mathness.util.Util
import com.kotlin.mathness.util.Util.setOnCustomLongClickListener
import com.kotlin.mathness.view.BackgroundAnimation
import com.kotlin.mathness.view.SquareRelativeLayout
import com.yangp.ypwaveview.YPWaveView
import kotlinx.coroutines.Runnable

@Suppress("PrivatePropertyName", "LocalVariableName", "LiftReturnOrAssignment")
@SuppressLint("SetTextI18n")
class GameActivity : ComponentActivity() {
    private lateinit var math:Mathness
    private val tv_target by lazy { findViewById<TextView>(R.id.tv_target_game) }
    private val tv_timer by lazy { findViewById<TextView>(R.id.tv_time_game) }
    private val tv_drag by lazy { findViewById<TextView>(R.id.tv_drag_game) }
    private val tv_new_number by lazy { findViewById<TextView>(R.id.tv_new_number_game) }
    private val tv_first_calc by lazy { findViewById<TextView>(R.id.tv_first_calc_game) }
    private val tv_symbol_calc by lazy { findViewById<TextView>(R.id.tv_symbol_calc_game) }
    private val tv_second_calc by lazy { findViewById<TextView>(R.id.tv_second_calc_game) }
    private val tv_equal_calc by lazy { findViewById<TextView>(R.id.tv_equal_calc_game) }
    private val tv_result_calc by lazy { findViewById<TextView>(R.id.tv_result_calc_game) }
    private val tv_plus by lazy { findViewById<TextView>(R.id.tv_plus_game) }
    private val tv_minus by lazy { findViewById<TextView>(R.id.tv_minus_game) }
    private val tv_multiply by lazy { findViewById<TextView>(R.id.tv_multiply_game) }
    private val tv_divide by lazy { findViewById<TextView>(R.id.tv_divide_game) }
    private val tv_num_1 by lazy { findViewById<TextView>(R.id.tv_num_1_game) }
    private val tv_num_2 by lazy { findViewById<TextView>(R.id.tv_num_2_game) }
    private val tv_num_3 by lazy { findViewById<TextView>(R.id.tv_num_3_game) }
    private val tv_num_4 by lazy { findViewById<TextView>(R.id.tv_num_4_game) }
    private val tv_num_5 by lazy { findViewById<TextView>(R.id.tv_num_5_game) }
    private val tv_num_6 by lazy { findViewById<TextView>(R.id.tv_num_6_game) }
    private val tv_num_7 by lazy { findViewById<TextView>(R.id.tv_num_7_game) }
    private val tv_level by lazy { findViewById<TextView>(R.id.tv_level_game) }
    private val tv_plus_victories by lazy { findViewById<TextView>(R.id.tv_plus_victories_game) }
    private val tv_plus_chain by lazy { findViewById<TextView>(R.id.tv_plus_chain_game) }
    private val tv_plus_extra by lazy { findViewById<TextView>(R.id.tv_plus_extra_game) }
    private val cpi_time_game by lazy { findViewById<CircularProgressIndicator>(R.id.cpi_time_game) }
    private val iv_character by lazy { findViewById<ImageView>(R.id.iv_character_game) }
    private val iv_surrender by lazy { findViewById<ImageView>(R.id.iv_surrender_game) }
    private val iv_shine by lazy { findViewById<ImageView>(R.id.iv_shine_game) }
    private val iv_shine2 by lazy { findViewById<ImageView>(R.id.iv_shine_2_game) }
    private val iv_plus_victories by lazy { findViewById<ImageView>(R.id.iv_plus_victories_game) }
    private val iv_plus_chain by lazy { findViewById<ImageView>(R.id.iv_plus_chain_game) }
    private val iv_plus_extra by lazy { findViewById<ImageView>(R.id.iv_plus_extra_game) }
    private val ll_menu by lazy { findViewById<LinearLayout>(R.id.ll_menu_game) }
    private val ll_solution by lazy { findViewById<LinearLayout>(R.id.ll_solution_game) }
    private val ll_exit by lazy { findViewById<LinearLayout>(R.id.ll_exit_game) }
    private val ll_plus_chain by lazy { findViewById<LinearLayout>(R.id.ll_plus_chain_game) }
    private val ll_plus_extra by lazy { findViewById<LinearLayout>(R.id.ll_plus_extra_game) }
    private val v_obscure by lazy { findViewById<View>(R.id.v_obscure_game) }
    private val rl_stars by lazy { findViewById<RelativeLayout>(R.id.rl_stars_game) }
    private val rl_drag by lazy { findViewById<RelativeLayout>(R.id.rl_drag_game) }
    private val mfl_level by lazy { findViewById<MaskableFrameLayout>(R.id.mfl_level_game) }
    private val ypwv_level by lazy { findViewById<YPWaveView>(R.id.ypwv_level_game) }
    private var tv_selected_first: TextView? = null
    private var tv_selected_second: TextView? = null
    private var numbers = mutableListOf<Int>()
    private var calc_steps = mutableListOf<Int>()
    private var game_steps = mutableListOf<List<Pair<String,Float>>>()
    private var originalSolution = mutableListOf<String>()
    private var dragging = ""
    private var b_block_ui = false
    private var b_sad = false
    private var b_freeze_timer = false
    private var target = 0
    private var timer = 0
    private var current_victory_chain = 0
    private lateinit var h_timer: Handler
    private lateinit var h_character: Handler
    private lateinit var h_stars: Handler
    private val result_location by lazy { intArrayOf(0,0) }
    private val num1_location by lazy { intArrayOf(0,0) }
    private val num2_location by lazy { intArrayOf(0,0) }
    private val num3_location by lazy { intArrayOf(0,0) }
    private val num4_location by lazy { intArrayOf(0,0) }
    private val num5_location by lazy { intArrayOf(0,0) }
    private val num6_location by lazy { intArrayOf(0,0) }
    private val num7_location by lazy { intArrayOf(0,0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        math = applicationContext as Mathness
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        b_block_ui = false
        initTextViews()
        findViewById<BackgroundAnimation>(R.id.rl_bg_animation).let {
            Handler(Looper.getMainLooper()).postDelayed({ it.startAnimation() }, 50)
        }
        iv_surrender.setOnClickListener{
            timer = 0
            b_freeze_timer = false
            b_block_ui = true
            h_timer.removeCallbacksAndMessages(null)
            h_timer.post(r_timer)
        }
        ypwv_level.setWaveVector(60f)
        findViewById<ImageView>(R.id.iv_reset_game).setOnClickListener{ reset() }
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!back()) {
                    showBackDialog()
                }
            }
        })
        h_character = Handler(Looper.getMainLooper())
        h_stars = Handler(Looper.getMainLooper())
        newGame()
    }

    private fun initTextViews() {
        listOf(tv_target, tv_timer, tv_drag, tv_new_number, tv_first_calc, tv_symbol_calc, tv_second_calc, tv_equal_calc, tv_result_calc,
            tv_plus, tv_minus, tv_multiply, tv_divide, tv_num_1, tv_num_2, tv_num_3, tv_num_4, tv_num_5, tv_num_6, tv_num_7,
            tv_level, tv_plus_victories, tv_plus_chain, tv_plus_extra).forEach{
            it.typeface = math.tf_handgoal
        }
        tv_first_calc.setOnCustomLongClickListener{
            if (tv_selected_first!=null&&!b_block_ui) {
                val clipData = ClipData.newPlainText("label", tv_selected_first?.id.toString())
                val shadowBuilder = View.DragShadowBuilder(rl_drag)
                dragging = tv_selected_first?.text.toString()
                tv_drag.text = dragging
                tv_selected_first?.startDragAndDrop(clipData, shadowBuilder, rl_drag, View.DRAG_FLAG_OPAQUE)
                tv_selected_first?.alpha = 0f
                tv_first_calc.alpha = 1f
                tv_symbol_calc.alpha = 0.5f
                tv_second_calc.alpha = 1f
                tv_result_calc.alpha = 0.5f
                tv_first_calc.text = ""
                tv_selected_first = null
                b_block_ui = true
            }
        }
        tv_symbol_calc.setOnCustomLongClickListener{
            if (tv_symbol_calc.text!=""&&!b_block_ui) {
                val shadowBuilder = View.DragShadowBuilder(rl_drag)
                dragging = tv_symbol_calc.text.toString()
                tv_drag.text = dragging
                when (tv_symbol_calc.text) {
                    PLUS -> {
                        val clipData = ClipData.newPlainText("label", tv_plus.id.toString())
                        tv_plus.startDragAndDrop(clipData, shadowBuilder, rl_drag, View.DRAG_FLAG_OPAQUE)
                        tv_plus.alpha = 0f
                    }
                    MINUS -> {
                        val clipData = ClipData.newPlainText("label", tv_minus.id.toString())
                        tv_minus.startDragAndDrop(clipData, shadowBuilder, rl_drag, View.DRAG_FLAG_OPAQUE)
                        tv_minus.alpha = 0f
                    }
                    MULTIPLY -> {
                        val clipData = ClipData.newPlainText("label", tv_multiply.id.toString())
                        tv_multiply.startDragAndDrop(clipData, shadowBuilder, rl_drag, View.DRAG_FLAG_OPAQUE)
                        tv_multiply.alpha = 0f
                    }
                    DIVIDE -> {
                        val clipData = ClipData.newPlainText("label", tv_divide.id.toString())
                        tv_divide.startDragAndDrop(clipData, shadowBuilder, rl_drag, View.DRAG_FLAG_OPAQUE)
                        tv_divide.alpha = 0f
                    }
                }
                tv_first_calc.alpha = 0.5f
                tv_symbol_calc.alpha = 1f
                tv_second_calc.alpha = 0.5f
                tv_result_calc.alpha = 0.5f
                tv_symbol_calc.text = ""
                b_block_ui = true
            }
        }
        tv_second_calc.setOnCustomLongClickListener{
            if (tv_selected_second!=null&&!b_block_ui) {
                val clipData = ClipData.newPlainText("label", tv_selected_second?.id.toString())
                val shadowBuilder = View.DragShadowBuilder(rl_drag)
                dragging = tv_selected_second?.text.toString()
                tv_drag.text = dragging
                tv_selected_second?.startDragAndDrop(clipData, shadowBuilder, rl_drag, View.DRAG_FLAG_OPAQUE)
                tv_selected_second?.alpha = 0f
                tv_first_calc.alpha = 1f
                tv_symbol_calc.alpha = 0.5f
                tv_second_calc.alpha = 1f
                tv_result_calc.alpha = 0.5f
                tv_second_calc.text = ""
                tv_selected_second = null
                b_block_ui = true
            }
        }
        tv_first_calc.setOnClickListener {
            if (tv_selected_first!=null&&!b_block_ui) {
                tv_selected_first?.alpha = 1f
                tv_selected_first = null
                tv_first_calc.text = ""
                calc_steps.remove(0)
            }
        }
        tv_symbol_calc.setOnClickListener {
            if (!b_block_ui) {
                tv_symbol_calc.text = ""
                calc_steps.remove(1)
            }
        }
        tv_second_calc.setOnClickListener {
            if (tv_selected_second!=null&&!b_block_ui) {
                tv_selected_second?.alpha = 1f
                tv_selected_second = null
                tv_second_calc.text = ""
                calc_steps.remove(2)
            }
        }
        listOf(tv_num_1, tv_num_2, tv_num_3, tv_num_4, tv_num_5, tv_num_6, tv_num_7, tv_plus, tv_minus, tv_multiply, tv_divide).forEach{
            enableDrag(it)
        }
        setupDragListener(tv_first_calc, ::setFirst, true)
        setupDragListener(tv_symbol_calc, ::setSymbol, false)
        setupDragListener(tv_second_calc, ::setSecond, true)
        setupNumSymClick()
    }

    private fun resetDragging() {
        dragging = ""
        tv_first_calc.setBackgroundResource(R.drawable.bg_calc_0)
        tv_symbol_calc.setBackgroundResource(R.drawable.bg_calc_sq_0)
        tv_second_calc.setBackgroundResource(R.drawable.bg_calc_0)
    }

    private fun setupDragListener(view: TextView, action: (TextView) -> Unit, isNumber: Boolean) {
        view.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    b_block_ui = false
                    val draggedData = event.clipData.getItemAt(0).text.toString()
                    val tv = findViewById<TextView>(draggedData.toInt())
                    val text = tv.text
                    if ((isNumber && text.isDigitsOnly()) || (!isNumber && Util.isOperator(text.toString()))) {
                        action(tv)
                    } else {
                        tv.alpha = 1f
                    }
                    resetDragging()
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    if ((isNumber && dragging.isDigitsOnly()) || (!isNumber && Util.isOperator(dragging))) {
                        view.setBackgroundResource(R.drawable.bg_calc_1)
                    }
                    false
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.setBackgroundResource(R.drawable.bg_calc_0)
                    false
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    checkDrags()
                    false
                }
                else -> true
            }
        }
    }

    private fun enableDrag(view: View) {
        val tv = view as TextView
        view.setOnCustomLongClickListener{
            if (!b_block_ui) {
                val clipData = ClipData.newPlainText("label", tv.id.toString())
                val shadowBuilder = View.DragShadowBuilder(rl_drag)
                if (tv.alpha!=0.2f) {
                    dragging = tv.text.toString()
                    tv_drag.text = dragging
                    view.startDragAndDrop(clipData, shadowBuilder, rl_drag, View.DRAG_FLAG_OPAQUE)
                    tv.alpha = 0f
                    if (dragging.isDigitsOnly()) {
                        tv_first_calc.alpha = 1f
                        tv_symbol_calc.alpha = 0.5f
                        tv_second_calc.alpha = 1f
                    }
                    else {
                        tv_first_calc.alpha = 0.5f
                        tv_symbol_calc.alpha = 1f
                        tv_second_calc.alpha = 0.5f
                    }
                    tv_result_calc.alpha = 0.5f
                }
                b_block_ui = true
            }
        }
    }

    private fun checkDrags() {
        if (tv_num_1.alpha==0f)
            tv_num_1.alpha = 1f
        if (tv_num_2.alpha==0f)
            tv_num_2.alpha = 1f
        if (tv_num_3.alpha==0f)
            tv_num_3.alpha = 1f
        if (tv_num_4.alpha==0f)
            tv_num_4.alpha = 1f
        if (tv_num_5.alpha==0f)
            tv_num_5.alpha = 1f
        if (tv_num_6.alpha==0f)
            tv_num_6.alpha = 1f
        if (tv_num_7.alpha==0f)
            tv_num_7.alpha = 1f
        if (tv_plus.alpha==0f)
            tv_plus.alpha = 1f
        if (tv_minus.alpha==0f)
            tv_minus.alpha = 1f
        if (tv_multiply.alpha==0f)
            tv_multiply.alpha = 1f
        if (tv_divide.alpha==0f)
            tv_divide.alpha = 1f
        resetDragging()
        listOf(tv_first_calc, tv_symbol_calc, tv_second_calc, tv_result_calc).forEach{ it.alpha = 1f }
        b_block_ui = false
    }

    private fun setupNumSymClick() {
        val num_click = View.OnClickListener { view ->
            val tv = view as TextView
            if (tv.alpha == 1f && !b_block_ui) {
                if (tv_selected_first != null) {
                    setSecond(tv)
                } else {
                    setFirst(tv)
                }
            }
        }
        val symbol_click = View.OnClickListener { view ->
            if (!b_block_ui) {
                val tv = view as TextView
                setSymbol(tv)
            }
        }
        listOf(tv_num_1, tv_num_2, tv_num_3, tv_num_4, tv_num_5, tv_num_6, tv_num_7).forEach {
            it.setOnClickListener(num_click)
        }
        listOf(tv_plus, tv_minus, tv_multiply, tv_divide).forEach {
            it.setOnClickListener(symbol_click)
        }
    }

    private fun animateCharacter(status: String) {
        h_character.removeCallbacksAndMessages(null)
        when (math.difficulty) {
            EASY -> {
                when (status) {
                    "sad" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_easy_sad,R.drawable.character_easy_sad_blink,h_character)
                    }
                    "crying" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_easy_crying,R.drawable.character_easy_crying_blink,h_character)
                    }
                    "happy" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_easy_happy,R.drawable.character_easy_happy_blink,h_character)
                    }
                    "surprised" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_easy_surprised,R.drawable.character_easy_surprised_blink,h_character)
                    }
                    else -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_easy_relaxed,R.drawable.character_easy_relaxed_blink,h_character)
                    }
                }
            }
            MEDIUM -> {
                when (status) {
                    "sad" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_medium_sad,R.drawable.character_medium_sad_blink,h_character)
                    }
                    "crying" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_medium_crying,R.drawable.character_medium_crying_blink,h_character)
                    }
                    "happy" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_medium_happy,R.drawable.character_medium_happy_blink,h_character)
                    }
                    "surprised" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_medium_surprised,R.drawable.character_medium_surprised_blink,h_character)
                    }
                    else -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_medium_thinking,R.drawable.character_medium_thinking_blink,h_character)
                    }
                }
            }
            HARD -> {
                when (status) {
                    "sad" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_hard_sad,R.drawable.character_hard_sad_blink,h_character)
                    }
                    "crying" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_hard_crying,R.drawable.character_hard_crying_blink,h_character)
                    }
                    "happy" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_hard_happy,R.drawable.character_hard_happy_blink,h_character)
                    }
                    "surprised" -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_hard_surprised,R.drawable.character_hard_surprised_blink,h_character)
                    }
                    else -> {
                        Animations.blinkCharacter(iv_character,R.drawable.character_hard_determined,R.drawable.character_hard_determined_blink,h_character)
                    }
                }
            }
        }
    }

    private fun hideBackDialog() {
        if (timer>10) {
            animateCharacter("default")
        }
        tv_target.alpha = 1f
        v_obscure.visibility = View.GONE
        ll_exit.visibility = View.GONE
    }

    private fun showBackDialog() {
        if (timer>10) {
            animateCharacter("sad")
        }
        v_obscure.visibility = View.VISIBLE
        tv_target.alpha = 0.2f
        ll_exit.visibility = View.VISIBLE
        findViewById<SquareRelativeLayout>(R.id.rl_bt_cancel_exit_game).setOnClickListener{ hideBackDialog() }
        findViewById<SquareRelativeLayout>(R.id.rl_bt_confirm_exit_game).setOnClickListener{
            when (math.difficulty) {
                EASY -> {
                    Util.putPrefInt(this, PREFERENCE_TOTAL_DEFEATS_EASY,Util.getPrefInt(this, PREFERENCE_TOTAL_DEFEATS_EASY)+1)
                }
                MEDIUM -> {
                    Util.putPrefInt(this, PREFERENCE_TOTAL_DEFEATS_MEDIUM,Util.getPrefInt(this, PREFERENCE_TOTAL_DEFEATS_MEDIUM)+1)
                }
                HARD -> {
                    Util.putPrefInt(this, PREFERENCE_TOTAL_DEFEATS_HARD,Util.getPrefInt(this, PREFERENCE_TOTAL_DEFEATS_HARD)+1)
                }
            }
            menu()
        }
    }

    private fun menu() {
        val i = Intent(this@GameActivity, SplashActivity::class.java)
        startActivity(i)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN,R.anim.enter_from_left,R.anim.exit_to_right)
        }
        else {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
        finish()
    }

    private fun showDefeat() {
        current_victory_chain = 0
        ll_plus_chain.visibility = View.GONE
        ll_plus_extra.visibility = View.GONE
        v_obscure.visibility = View.VISIBLE
        when (math.difficulty) {
            EASY -> {
                iv_plus_victories.setImageResource(R.drawable.skull_green)
                Util.putPrefInt(this, PREFERENCE_TOTAL_DEFEATS_EASY,Util.getPrefInt(this, PREFERENCE_TOTAL_DEFEATS_EASY)+1)
            }
            MEDIUM -> {
                iv_plus_victories.setImageResource(R.drawable.skull_yellow)
                Util.putPrefInt(this, PREFERENCE_TOTAL_DEFEATS_MEDIUM,Util.getPrefInt(this, PREFERENCE_TOTAL_DEFEATS_MEDIUM)+1)
            }
            HARD -> {
                iv_plus_victories.setImageResource(R.drawable.skull_red)
                Util.putPrefInt(this, PREFERENCE_TOTAL_DEFEATS_HARD,Util.getPrefInt(this, PREFERENCE_TOTAL_DEFEATS_HARD)+1)
            }
        }
        animateCharacter("crying")
        ll_menu.visibility = View.VISIBLE
        tv_target.alpha = 0.2f
        findViewById<SquareRelativeLayout>(R.id.rl_bt_exit_game).setOnClickListener{ menu() }
        findViewById<SquareRelativeLayout>(R.id.rl_bt_solution_game).apply { visibility = View.VISIBLE }.setOnClickListener{ll_solution.visibility = View.VISIBLE}
        findViewById<SquareRelativeLayout>(R.id.rl_bt_next_game).setOnClickListener{ newGame() }
        listOf(tv_timer,tv_target,tv_plus,tv_minus,tv_multiply,tv_num_1,tv_num_2,tv_num_3,tv_num_4,tv_num_5,tv_num_6,tv_num_7).forEach{it.clearAnimation()}
    }

    private fun showVictory() {
        current_victory_chain++
        if (current_victory_chain>1) {
            tv_plus_chain.text = current_victory_chain.toString()
            ll_plus_chain.visibility = View.VISIBLE
        }
        else {
            ll_plus_chain.visibility = View.GONE
        }
        ll_plus_extra.visibility = View.GONE
        tv_plus_chain.setTextColor(getColor(R.color.white))
        when (math.difficulty) {
            EASY -> {
                iv_plus_victories.setImageResource(R.drawable.crown_green)
                iv_plus_chain.setImageResource(R.drawable.chain_green)
                iv_plus_extra.setImageResource(R.drawable.puzzle_green)
                Util.putPrefInt(this, PREFERENCE_TOTAL_VICTORIES_EASY,Util.getPrefInt(this, PREFERENCE_TOTAL_VICTORIES_EASY)+1)
                if (Util.getPrefInt(this, PREFERENCE_MAX_VICTORY_CHAIN_EASY)<current_victory_chain) {
                    Util.putPrefInt(this, PREFERENCE_MAX_VICTORY_CHAIN_EASY,current_victory_chain)
                    tv_plus_chain.setTextColor(getColor(R.color.mth_yellow))
                }
            }
            MEDIUM -> {
                iv_plus_victories.setImageResource(R.drawable.crown_yellow)
                iv_plus_chain.setImageResource(R.drawable.chain_yellow)
                iv_plus_extra.setImageResource(R.drawable.puzzle_yellow)
                Util.putPrefInt(this, PREFERENCE_TOTAL_VICTORIES_MEDIUM,Util.getPrefInt(this, PREFERENCE_TOTAL_VICTORIES_MEDIUM)+1)
                if (Util.getPrefInt(this, PREFERENCE_MAX_VICTORY_CHAIN_MEDIUM)<current_victory_chain) {
                    Util.putPrefInt(this, PREFERENCE_MAX_VICTORY_CHAIN_MEDIUM,current_victory_chain)
                    tv_plus_chain.setTextColor(getColor(R.color.mth_yellow))
                }
                if (tv_num_1.alpha==1f||tv_num_2.alpha==1f||tv_num_3.alpha==1f||tv_num_4.alpha==1f||tv_num_5.alpha==1f) {
                    Util.putPrefInt(this, PREFERENCE_TOTAL_VICTORIES_EXTRA_MEDIUM,Util.getPrefInt(this, PREFERENCE_TOTAL_VICTORIES_EXTRA_MEDIUM)+1)
                    ll_plus_extra.visibility = View.VISIBLE
                }

            }
            HARD -> {
                iv_plus_victories.setImageResource(R.drawable.crown_red)
                iv_plus_chain.setImageResource(R.drawable.chain_red)
                iv_plus_extra.setImageResource(R.drawable.puzzle_red)
                Util.putPrefInt(this, PREFERENCE_TOTAL_VICTORIES_HARD,Util.getPrefInt(this, PREFERENCE_TOTAL_VICTORIES_HARD,0)+1)
                if (Util.getPrefInt(this, PREFERENCE_MAX_VICTORY_CHAIN_HARD,0)<current_victory_chain) {
                    Util.putPrefInt(this, PREFERENCE_MAX_VICTORY_CHAIN_HARD,current_victory_chain)
                    tv_plus_chain.setTextColor(getColor(R.color.mth_yellow))
                }
                if (tv_num_1.alpha==1f||tv_num_2.alpha==1f||tv_num_3.alpha==1f||tv_num_4.alpha==1f||tv_num_5.alpha==1f||tv_num_6.alpha==1f||tv_num_7.alpha==1f) {
                    Util.putPrefInt(this, PREFERENCE_TOTAL_VICTORIES_EXTRA_HARD,Util.getPrefInt(this, PREFERENCE_TOTAL_VICTORIES_EXTRA_HARD,0)+1)
                    ll_plus_extra.visibility = View.VISIBLE
                }
            }
        }
        val ll_level = findViewById<LinearLayout>(R.id.ll_level_game)
        listOf(ll_level,v_obscure,rl_stars,ll_menu).forEach{
            it.visibility = View.VISIBLE
        }
        Animations.stars(rl_stars, iv_character.x+iv_character.width/2f, iv_character.y+iv_character.height/2f, h_stars)
        Animations.shine(iv_shine,false)
        Animations.shine(iv_shine2,true)
        Animations.pulse(mfl_level,1000,0.9f)
        calculateLevel()
        animateCharacter("happy")
        tv_target.alpha = 0.2f
        findViewById<SquareRelativeLayout>(R.id.rl_bt_exit_game).setOnClickListener{ menu() }
        findViewById<SquareRelativeLayout>(R.id.rl_bt_solution_game).apply { visibility = View.INVISIBLE }.setOnClickListener(null)
        findViewById<SquareRelativeLayout>(R.id.rl_bt_next_game).setOnClickListener{
            ll_level.visibility = View.GONE
            newGame()
        }
        listOf(tv_timer,tv_target,tv_plus,tv_minus,tv_multiply,tv_num_1,tv_num_2,tv_num_3,tv_num_4,tv_num_5,tv_num_6,tv_num_7).forEach{it.clearAnimation()}
    }

    private fun newGame() {
        val game = Util.generateGame(math.difficulty)
        target = game[0].toInt()
        tv_target.text = target.toString()
        numbers = mutableListOf()
        iv_surrender.visibility = View.GONE
        b_block_ui = false
        listOf(tv_num_1,tv_num_2,tv_num_3,tv_num_4,tv_num_5,tv_num_6,tv_num_7).forEach{ it.text = "0" }
        listOf(tv_plus,tv_minus,tv_multiply,tv_divide,tv_num_1,tv_num_2,tv_num_3,tv_num_4,tv_num_5,tv_num_6,tv_num_7,tv_target).forEach {
            it.clearAnimation()
            it.alpha = 1f
        }
        listOf(tv_first_calc,tv_symbol_calc,tv_second_calc,tv_result_calc).forEach { it.text = "" }
        calc_steps.clear()
        tv_selected_first = null
        tv_selected_second = null
        Animations.pulse(tv_target,1000,0.9f)
        var separatorIndex = 0
        for (i in game.indices) {
            if (game[i] == ";") {
                separatorIndex = i
                break
            }
            if (i>0) {
                numbers.add(game[i].toInt())
                when (i) {
                    1 -> tv_num_1.text = game[i]
                    2 -> tv_num_2.text = game[i]
                    3 -> tv_num_3.text = game[i]
                    4 -> tv_num_4.text = game[i]
                    5 -> tv_num_5.text = game[i]
                    6 -> tv_num_6.text = game[i]
                    7 -> tv_num_7.text = game[i]
                }
            }
        }
        originalSolution.clear()
        listOf(v_obscure,ll_menu,ll_solution).forEach { it.visibility = View.GONE }
        h_stars.removeCallbacksAndMessages(null)
        ll_solution.removeAllViews()
        rl_stars.removeAllViews()
        listOf(rl_stars,iv_shine,iv_shine2).forEach {
            it.clearAnimation()
            it.visibility = View.INVISIBLE
        }
        for (i in separatorIndex+1..<game.size) {
            originalSolution.add(game[i])
            val ll_row = LayoutInflater.from(this).inflate(R.layout.ll_solution_row,ll_solution,false)
            val tv_calc = ll_row.findViewById<TextView>(R.id.tv_calc_solution)
            val tv_equal = ll_row.findViewById<TextView>(R.id.tv_equal_solution)
            val tv_result = ll_row.findViewById<TextView>(R.id.tv_result_solution)
            val split_step = game[i].split("=")
            tv_result.text = split_step[1].replace(" ","")
            while (tv_result.text.toString().length<4)
                tv_result.text = tv_result.text.toString() + " "
            tv_calc.text = split_step[0].dropLast(1)
            listOf(tv_calc,tv_equal,tv_result).forEach { it.typeface = math.tf_handgoal }
            if (i==game.size-1)
                tv_result.setTextColor(getColor(R.color.mth_green))
            ll_solution.addView(ll_row)
        }
        listOf(tv_plus,tv_minus,tv_multiply,tv_divide,tv_num_1,tv_num_2,tv_num_3,tv_num_4,tv_num_5,tv_num_6,tv_num_7).forEach { Animations.pulse(it,2000,0.8f) }
        listOf(tv_num_4,tv_num_5,tv_num_6,tv_num_7).forEach { it.visibility = View.VISIBLE }
        timer = 90
        b_sad = false
        when (math.difficulty) {
            EASY -> {
                listOf(tv_num_4,tv_num_5,tv_num_6,tv_num_7).forEach {
                    it.clearAnimation()
                    it.visibility = View.INVISIBLE }
                timer = 60
            }
            MEDIUM -> {
                listOf(tv_num_6,tv_num_7).forEach {
                    it.clearAnimation()
                    it.visibility = View.INVISIBLE }
                timer = 90
            }
            HARD -> {
                timer = 120
            }
        }
        animateCharacter("default")
        game_steps.clear()
        addStep()
        cpi_time_game.max = timer
        cpi_time_game.progress = timer
        tv_timer.text = timer.toString()
        tv_timer.setTextColor(getColor(R.color.white))
        if (this::h_timer.isInitialized)
            h_timer.removeCallbacksAndMessages(null)
        h_timer = Handler(Looper.getMainLooper())
        b_freeze_timer = false
        h_timer.postDelayed(r_timer,1000)
    }

    private val r_timer = Runnable{
        if (!b_freeze_timer) {
            timer-=1
            cpi_time_game.progress = timer
            if (timer<=0) {
                tv_timer.text = 0.toString()
                tv_timer.setTextColor(getColor(R.color.black))
                Animations.pulseTimer(tv_timer,800)
                ll_exit.visibility = View.GONE
                b_freeze_timer = true
                showDefeat()
            }
            else {
                tv_timer.text = timer.toString()
                if (timer<10) {
                    tv_timer.setTextColor(getColor(R.color.mth_red))
                    if (!b_sad) {
                        b_sad = true
                        animateCharacter("sad")
                    }
                }
                else {
                    when (math.difficulty) {
                        EASY -> {
                            if (timer<30&&iv_surrender.visibility==View.GONE)
                                iv_surrender.visibility = View.VISIBLE
                        }
                        MEDIUM -> {
                            if (timer<60&&iv_surrender.visibility==View.GONE)
                                iv_surrender.visibility = View.VISIBLE
                        }
                        HARD -> {
                            if (timer<90&&iv_surrender.visibility==View.GONE)
                                iv_surrender.visibility = View.VISIBLE
                        }
                    }
                    if (timer<30)
                        tv_timer.setTextColor(getColor(R.color.mth_yellow))
                    else
                        tv_timer.setTextColor(getColor(R.color.white))
                }
                h_timer.removeCallbacksAndMessages(null)
                postTimer()
                Animations.pulseTimer(tv_timer,500)
            }
        }
        else {
            h_timer.removeCallbacksAndMessages(null)
            postTimer()
        }
    }
    private fun postTimer() {
        h_timer.postDelayed(r_timer,1000)
    }

    private fun addStep() {
        val step = listOf(
            Pair(tv_num_1.text.toString(),tv_num_1.alpha),
            Pair(tv_num_2.text.toString(),tv_num_2.alpha),
            Pair(tv_num_3.text.toString(),tv_num_3.alpha),
            Pair(tv_num_4.text.toString(),tv_num_4.alpha),
            Pair(tv_num_5.text.toString(),tv_num_5.alpha),
            Pair(tv_num_6.text.toString(),tv_num_6.alpha),
            Pair(tv_num_7.text.toString(),tv_num_7.alpha))
        game_steps.add(step)
    }

    private fun checkIfLastNumber() {
        var available = 0
        if (tv_num_1.alpha==1f)
            available++
        if (tv_num_2.alpha==1f)
            available++
        if (tv_num_3.alpha==1f)
            available++
        if (tv_num_4.alpha==1f)
            available++
        if (tv_num_5.alpha==1f)
            available++
        if (tv_num_6.alpha==1f)
            available++
        if (tv_num_7.alpha==1f)
            available++
        if (available<=1) {
            b_block_ui = true
            b_freeze_timer = true
            tv_target.setTextColor(getColor(R.color.mth_red))
            val h_delay = Handler(Looper.getMainLooper())
            h_delay.postDelayed({
                b_block_ui = false
                b_freeze_timer = false
                tv_target.setTextColor(getColor(R.color.white))
                reset()
            },500)
        }
    }

    private fun reset() {
        if (!b_block_ui) {
            listOf(tv_first_calc,tv_symbol_calc,tv_second_calc,tv_result_calc).forEach { it.text = "" }
            b_block_ui = false
            tv_selected_first = null
            tv_selected_second = null
            listOf(tv_plus,tv_minus,tv_multiply,tv_divide,tv_num_1,tv_num_2,tv_num_3,tv_num_4,tv_num_5,tv_num_6,tv_num_7,tv_target).forEach {
                it.alpha = 1f
            }
            tv_num_1.text = numbers[0].toString()
            tv_num_2.text = numbers[1].toString()
            tv_num_3.text = numbers[2].toString()
            if (numbers.size>3) {
                tv_num_4.text = numbers[3].toString()
                tv_num_5.text = numbers[4].toString()
                if (numbers.size>5) {
                    tv_num_6.text = numbers[5].toString()
                    tv_num_7.text = numbers[6].toString()
                }
            }
            game_steps.clear()
            addStep()
        }
    }

    private fun back(): Boolean {
        if (b_block_ui) return false
        b_block_ui = true
        return when {
            calc_steps.isNotEmpty() -> undoCalcSteps()
            game_steps.size > 1 -> undoGameSteps()
            ll_menu.visibility == View.VISIBLE -> true
            ll_exit.visibility == View.VISIBLE -> {
                hideBackDialog()
                true
            }
            else -> false
        }.also { b_block_ui = false }
    }

    private fun undoCalcSteps(): Boolean {
        when (calc_steps.last()) {
            0 -> {
                tv_first_calc.text = ""
                tv_selected_first?.alpha = 1f
                tv_selected_first = null
                calc_steps.removeAt(calc_steps.size - 1)
            }
            1 -> {
                tv_symbol_calc.text = ""
                calc_steps.removeAt(calc_steps.size - 1)
            }
            2 -> {
                tv_second_calc.text = ""
                tv_selected_second?.alpha = 1f
                tv_selected_second = null
                calc_steps.removeAt(calc_steps.size - 1)
            }
        }
        return true
    }

    private fun undoGameSteps(): Boolean {
        val step = game_steps[game_steps.size - 2]
        val tvs = listOf(tv_num_1, tv_num_2, tv_num_3, tv_num_4, tv_num_5, tv_num_6, tv_num_7)
        for (i in tvs.indices) {
            tvs[i].text = step[i].first
            tvs[i].alpha = step[i].second
        }
        game_steps.removeLast()
        return true
    }

    private fun setFirst(tv: TextView) {
        if (tv.alpha!=0.2f&&!b_block_ui) {
            b_block_ui = true
            tv_first_calc.text = tv.text
            if (tv_selected_first!=null)
                tv_selected_first?.alpha = 1f
            tv.alpha = 0.2f
            tv_selected_first = tv
            checkCalc()
            calc_steps.remove(0)
            calc_steps.add(0)
        }
    }

    private fun setSymbol(tv: TextView) {
        if (!b_block_ui) {
            b_block_ui = true
            tv_symbol_calc.text = tv.text
            checkCalc()
            calc_steps.remove(1)
            calc_steps.add(1)
        }
    }

    private fun setSecond(tv: TextView) {
        if (tv.alpha!=0.2f&&!b_block_ui) {
            b_block_ui
            tv_second_calc.text = tv.text
            if (tv_selected_second!=null)
                tv_selected_second?.alpha = 1f
            tv.alpha = 0.2f
            tv_selected_second = tv
            checkCalc()
            calc_steps.remove(2)
            calc_steps.add(2)
        }
    }

    private fun checkCalc() {
        tv_result_calc.getLocationOnScreen(result_location)
        tv_num_1.getLocationOnScreen(num1_location)
        tv_num_2.getLocationOnScreen(num2_location)
        tv_num_3.getLocationOnScreen(num3_location)
        tv_num_4.getLocationOnScreen(num4_location)
        tv_num_5.getLocationOnScreen(num5_location)
        tv_num_6.getLocationOnScreen(num6_location)
        tv_num_7.getLocationOnScreen(num7_location)
        b_block_ui = true
        if (tv_first_calc.text!="" && tv_symbol_calc.text!="" && tv_second_calc.text!="" && tv_selected_first!=null && tv_selected_second!=null) {
            b_freeze_timer = true
            val result = calculateResult()
            if (result!=-1) {
                h_character.removeCallbacksAndMessages(null)
                tv_result_calc.text = result.toString()
                if (result==target) {
                    listOf(tv_result_calc,tv_target).forEach { it.setTextColor(getColor(R.color.mth_green)) }
                    val h_delay = Handler(Looper.getMainLooper())
                    h_delay.postDelayed({
                        b_block_ui = false
                        tv_result_calc.setTextColor(getColor(R.color.white))
                        tv_target.setTextColor(getColor(R.color.mth_yellow))
                        showVictory()
                    },500)
                }
                else {
                    animateCharacter("surprised")
                    val h_delay = Handler(Looper.getMainLooper())
                    h_delay.postDelayed({
                        if (timer>=10) {
                            animateCharacter("default")
                        }
                        else {
                            animateCharacter("sad")
                        }
                    },500)
                    h_delay.postDelayed({
                        tv_result_calc.text = ""
                        val new_number_from = doubleArrayOf(result_location[0].toDouble(),result_location[1].toDouble())
                        Animations.fadeFromTo(tv_new_number,250,1f,0.5f)
                        when (0.2f) {
                            tv_num_1.alpha -> animateNewNumber(1, new_number_from,doubleArrayOf(num1_location[0].toDouble(),num1_location[1].toDouble()-60),result.toString())
                            tv_num_2.alpha -> animateNewNumber(2, new_number_from,doubleArrayOf(num2_location[0].toDouble(),num2_location[1].toDouble()-60),result.toString())
                            tv_num_3.alpha -> animateNewNumber(3, new_number_from,doubleArrayOf(num3_location[0].toDouble(),num3_location[1].toDouble()-60),result.toString())
                            tv_num_4.alpha -> animateNewNumber(4, new_number_from,doubleArrayOf(num4_location[0].toDouble(),num4_location[1].toDouble()-60),result.toString())
                            tv_num_5.alpha -> animateNewNumber(5, new_number_from,doubleArrayOf(num5_location[0].toDouble(),num5_location[1].toDouble()-60),result.toString())
                            tv_num_6.alpha -> animateNewNumber(6, new_number_from,doubleArrayOf(num6_location[0].toDouble(),num6_location[1].toDouble()-60),result.toString())
                            tv_num_7.alpha -> animateNewNumber(7, new_number_from,doubleArrayOf(num7_location[0].toDouble(),num7_location[1].toDouble()-60),result.toString())
                        }
                        tv_selected_first = null
                        tv_selected_second = null
                        calc_steps.clear()
                    },100)
                }
            }
            else {
                listOf(tv_first_calc,tv_symbol_calc,tv_second_calc).forEach { it.setTextColor(getColor(R.color.mth_red)) }
                val h_delay = Handler(Looper.getMainLooper())
                h_delay.postDelayed({
                    listOf(tv_first_calc,tv_symbol_calc,tv_second_calc).forEach { it.setTextColor(getColor(R.color.mth_yellow)) }
                    b_freeze_timer = false
                    b_block_ui = false
                    tv_selected_first?.alpha = 1f
                    tv_selected_second?.alpha = 1f
                    listOf(tv_first_calc,tv_symbol_calc,tv_second_calc,tv_result_calc).forEach { it.text = "" }
                    tv_selected_first = null
                    tv_selected_second = null
                    calc_steps.clear()
                },500)
            }
        }
        else {
            b_block_ui = false
        }
    }

    private fun calculateResult(): Int {
        val first = tv_first_calc.text.toString().toInt()
        val second = tv_second_calc.text.toString().toInt()
        when (tv_symbol_calc.text) {
            PLUS -> {
                if ((first + second).toString().length<5)
                    return first + second
                else
                    return -1
            }
            MINUS -> {
                if (first > second)
                    return first - second
                else
                    return -1
            }
            MULTIPLY -> {
                if (first!=1 && second!=1 && (first * second).toString().length<5)
                    return first * second
                else
                    return -1
            }
            DIVIDE -> {
                if (second!=1 && first%second==0)
                    return first / second
                else
                    return -1
            }
        }
        return -1
    }

    private fun animateNewNumber(number: Int, from: DoubleArray, to: DoubleArray, result: String) {
        tv_new_number.text = result
        tv_new_number.x = from[0].toFloat()
        tv_new_number.y = from[1].toFloat()
        tv_new_number.visibility = View.VISIBLE
        val va = ValueAnimator.ofObject(DoubleArrayEvaluator(),from,to)
        va.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as DoubleArray
            tv_new_number.x = animatedValue[0].toFloat()
            tv_new_number.y = animatedValue[1].toFloat()
        }
        Animations.fadeFromTo(tv_new_number,250,1f,0.5f)
        va.interpolator = LinearInterpolator()
        va.duration = 300
        va.setObjectValues(from,to)
        va.addListener(object : AnimatorListenerAdapter(
        ) {
            override fun onAnimationEnd(animation: Animator) {
                tv_new_number.clearAnimation()
                tv_new_number.visibility = View.INVISIBLE
                when (number) {
                    1 -> {
                        tv_num_1.text = result
                        tv_num_1.alpha = 1f
                    }
                    2 -> {
                        tv_num_2.text = result
                        tv_num_2.alpha = 1f
                    }
                    3 -> {
                        tv_num_3.text = result
                        tv_num_3.alpha = 1f
                    }
                    4 -> {
                        tv_num_4.text = result
                        tv_num_4.alpha = 1f
                    }
                    5 -> {
                        tv_num_5.text = result
                        tv_num_5.alpha = 1f
                    }
                    6 -> {
                        tv_num_6.text = result
                        tv_num_6.alpha = 1f
                    }
                    7 -> {
                        tv_num_7.text = result
                        tv_num_7.alpha = 1f
                    }
                }
                listOf(tv_first_calc,tv_symbol_calc,tv_second_calc).forEach { it.text = "" }
                b_block_ui = false
                b_freeze_timer = false
                addStep()
                checkIfLastNumber()
            }
        })
        va.start()
    }

    private fun calculateLevel() {
        var current_level = 0
        val new_level: Int
        var current_victories = 0
        val current_target: Int
        when (math.difficulty) {
            EASY -> {
                current_level = Util.getPrefInt(this,PREFERENCE_LEVEL_EASY,1)
                current_victories = Util.getPrefInt(this,PREFERENCE_CURRENT_VICTORIES_EASY,0)
            }
            MEDIUM -> {
                current_level = Util.getPrefInt(this,PREFERENCE_LEVEL_MEDIUM,1)
                current_victories = Util.getPrefInt(this,PREFERENCE_CURRENT_VICTORIES_MEDIUM,0)
            }
            HARD -> {
                current_level = Util.getPrefInt(this,PREFERENCE_LEVEL_HARD,1)
                current_victories = Util.getPrefInt(this,PREFERENCE_CURRENT_VICTORIES_HARD,0)
            }
        }
        tv_level.text = current_level.toString()
        if (current_level<1)
            current_level = 1
        val current_next_level: Int = current_level + 1
        current_target = Util.fib(current_next_level+2)
        val new_victories: Int = current_victories + 1
        ypwv_level.max = current_target * 10
        ypwv_level.progress = current_victories * 10
        if (new_victories>=current_target) {
            new_level = current_level + 1
            when (math.difficulty) {
                EASY -> {
                    Util.putPrefInt(this, PREFERENCE_LEVEL_EASY,new_level)
                    Util.putPrefInt(this, PREFERENCE_CURRENT_VICTORIES_EASY,0)
                }
                MEDIUM -> {
                    Util.putPrefInt(this, PREFERENCE_LEVEL_MEDIUM,new_level)
                    Util.putPrefInt(this, PREFERENCE_CURRENT_VICTORIES_MEDIUM,0)
                }
                HARD -> {
                    Util.putPrefInt(this, PREFERENCE_LEVEL_HARD,new_level)
                    Util.putPrefInt(this, PREFERENCE_CURRENT_VICTORIES_HARD,0)
                }
            }
            animateProgress(current_victories * 10f, current_target * 10f, new_level.toString())
        }
        else {
            when (math.difficulty) {
                EASY -> {
                    Util.putPrefInt(this, PREFERENCE_CURRENT_VICTORIES_EASY,new_victories)
                }
                MEDIUM -> {
                    Util.putPrefInt(this, PREFERENCE_CURRENT_VICTORIES_MEDIUM,new_victories)
                }
                HARD -> {
                    Util.putPrefInt(this, PREFERENCE_CURRENT_VICTORIES_HARD,new_victories)
                }
            }
            animateProgress(current_victories * 10f, new_victories * 10f, "")
        }
    }

    private fun animateProgress(from: Float, to: Float, level: String) {
        val va = ValueAnimator.ofFloat(from,to)
        va.duration = 1000
        va.interpolator = LinearInterpolator()
        va.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            ypwv_level.progress = animatedValue.toInt()
        }
        if (level != "") {
            va.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val h_delay = Handler(Looper.getMainLooper())
                    h_delay.postDelayed({
                        ypwv_level.progress = 0
                        ypwv_level.max = 100
                        tv_level.text = level
                        Animations.pulseOnce(tv_level,500, 1.2f)
                    },500)
                    super.onAnimationEnd(animation)
                }
            })
        }
        va.start()
    }
}