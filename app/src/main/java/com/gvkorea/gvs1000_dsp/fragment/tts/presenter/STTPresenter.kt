package com.gvkorea.gvs1000_dsp.fragment.tts.presenter

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.gvkorea.gvs1000_dsp.fragment.tts.TTSFragment
import com.gvkorea.gvs1000_dsp.fragment.tts.listener.STTRecognitionListener
import java.util.*

class STTPresenter(val view: TTSFragment) {

    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.context)
    val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    fun init_Setup(){
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizer.setRecognitionListener(STTRecognitionListener(view))
    }

}