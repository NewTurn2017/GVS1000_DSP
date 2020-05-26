package com.gvkorea.gvs1000_dsp.fragment.tts.listener

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import com.gvkorea.gvs1000_dsp.fragment.tts.TTSFragment
import kotlinx.android.synthetic.main.fragment_tts.*

class STTRecognitionListener(val view: TTSFragment) : RecognitionListener {
    override fun onReadyForSpeech(p0: Bundle?) {
    }

    override fun onRmsChanged(p0: Float) {
    }

    override fun onBufferReceived(p0: ByteArray?) {
    }

    override fun onPartialResults(p0: Bundle?) {
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(p0: Int) {
    }

    override fun onResults(p0: Bundle?) {
        var matches = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if(matches != null){
            view.et_text.setText(matches[0])
        }
    }

}