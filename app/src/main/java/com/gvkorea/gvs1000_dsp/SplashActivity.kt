package com.gvkorea.gvs1000_dsp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val SPLASHTIMEOUT = 2500L
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        setContentView(R.layout.activity_splash)

        val handler = Handler()

        handler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASHTIMEOUT)
        val mAnim = AnimationUtils.loadAnimation(this, R.anim.splashanimation)
        splash_logo.startAnimation(mAnim)
    }
}
