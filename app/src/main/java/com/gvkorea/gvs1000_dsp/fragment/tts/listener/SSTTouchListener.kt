package com.gvkorea.gvs1000_dsp.fragment.tts.listener

import android.view.MotionEvent
import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tts.TTSFragment
import com.gvkorea.gvs1000_dsp.fragment.tts.presenter.STTPresenter
import kotlinx.android.synthetic.main.fragment_tts.*

class SSTTouchListener(val view: TTSFragment, val presenter: STTPresenter): View.OnTouchListener {
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

        when (p1?.action) {
            MotionEvent.ACTION_UP -> {
                presenter.speechRecognizer.stopListening()
                view.et_text.hint = view.context?.getString(R.string.stt_hint)
            }

            MotionEvent.ACTION_DOWN -> {
                presenter.speechRecognizer.startListening(presenter.speechIntent)
                view.et_text.setText("")
                view.et_text.hint =  view.context?.getString(R.string.listening)
            }
        }
        return false

    }

}