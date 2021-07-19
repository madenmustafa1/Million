package com.maden.million.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.graphics.ColorUtils
import com.maden.million.R
import kotlinx.android.synthetic.main.activity_pop_up.*

class PopUpActivity : AppCompatActivity() {

    private var popupText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up)

        // Tam tersinde yapılacak
        //val bundle = intent.extras
        //popupText = bundle?.getString("popupText", "Title") ?: ""

        if (Build.VERSION.SDK_INT >= 21) {
            this.window.statusBarColor = Color.TRANSPARENT
            popupBG.background

            //setWindowFlag(this, false)
        }

        val alpha = 100
        val alphaColor = ColorUtils
            .setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator
            .ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popupBG.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
        /*
        popup_window_view_with_border.alpha = 0f
        popup_window_view_with_border.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

         */
    }

    /*
    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }
     */

}