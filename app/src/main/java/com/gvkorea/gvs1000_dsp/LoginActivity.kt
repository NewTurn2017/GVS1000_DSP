package com.gvkorea.gvs1000_dsp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), PinLockListener {

    lateinit var pref_login: SharedPreferences
    var user: Boolean = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        setContentView(R.layout.activity_login)

        pref_login = getSharedPreferences("pref_login", Context.MODE_PRIVATE)
        loadPref()
        pin_lock_view.setPinLockListener(this)
        pin_lock_view.pinLength = 4

        indicator_dots.indicatorType = IndicatorDots.IndicatorType.FILL
        pin_lock_view.attachIndicatorDots(indicator_dots)

        if(user){
            finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

    }

    private fun loadPref() {
        user = pref_login.getBoolean("user", false)
    }

    override fun onEmpty() {
        Toast.makeText(applicationContext, "비밀번호 4자리를 입력하세요", Toast.LENGTH_SHORT).show()
    }

    override fun onComplete(pin: String?) {
        when (pin) {
            "3302" -> {
                finish()
                startActivity(Intent(this, MainActivity::class.java))
                pref_login.edit().putBoolean("user", true).apply()
            }
            else -> {
                Toast.makeText(applicationContext, "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPinChange(pinLength: Int, intermediatePin: String?) {
    }
}
