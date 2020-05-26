package com.gvkorea.gvs1000_dsp.fragment.tts.listener

import android.R.*
import android.R.color.*
import android.speech.tts.UtteranceProgressListener
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.fragment.tts.TTSFragment
import kotlinx.android.synthetic.main.fragment_tts.*

class TTSProgressListener(val view: TTSFragment) : UtteranceProgressListener() {
    override fun onError(p0: String?) {
    }

    override fun onDone(p0: String?) {
        view.activity?.runOnUiThread {
            view.btn_TTS.setBackgroundColor(ContextCompat.getColor(view.context!!, holo_green_light))
        }

    }


    override fun onStart(p0: String?) {
        view.activity?.runOnUiThread {
            view.btn_TTS.setBackgroundColor(ContextCompat.getColor(view.context!!, holo_red_light))
        }

    }
}