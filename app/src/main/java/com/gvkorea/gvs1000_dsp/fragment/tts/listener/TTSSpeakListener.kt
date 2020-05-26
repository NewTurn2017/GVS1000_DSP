package com.gvkorea.gvs1000_dsp.fragment.tts.listener

import android.view.View
import androidx.core.content.ContextCompat
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tts.TTSFragment
import com.gvkorea.gvs1000_dsp.fragment.tts.presenter.TTSPresenter
import kotlinx.android.synthetic.main.fragment_tts.*

class TTSSpeakListener(val view: TTSFragment, val presenter: TTSPresenter) : View.OnClickListener {
    override fun onClick(p0: View?) {
        if(presenter.tts.isSpeaking){
            presenter.tts.stop()
            view.btn_TTS.setBackgroundColor(ContextCompat.getColor(view.context!!, android.R.color.holo_red_light))
        }else{
            presenter.tts()
        }

    }

}