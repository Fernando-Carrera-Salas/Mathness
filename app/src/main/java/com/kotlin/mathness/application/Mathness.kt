package com.kotlin.mathness.application

import android.app.Application
import android.graphics.Typeface
import com.kotlin.mathness.util.Constants.PREFERENCE_DIFFICULTY

@Suppress("PropertyName")
class Mathness : Application() {
    var b_splash = false
    lateinit var tf_handgoal: Typeface
    lateinit var difficulty: String

    override fun onCreate() {
        super.onCreate()
        tf_handgoal = Typeface.createFromAsset(assets, "font/handgoal.otf")
        difficulty = getSharedPreferences("prefs", MODE_PRIVATE).getString(PREFERENCE_DIFFICULTY,"medium").toString()
    }
}
