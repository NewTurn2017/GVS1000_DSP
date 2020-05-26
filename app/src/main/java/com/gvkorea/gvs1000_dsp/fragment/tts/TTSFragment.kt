package com.gvkorea.gvs1000_dsp.fragment.tts

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tts.listener.SSTTouchListener
import com.gvkorea.gvs1000_dsp.fragment.tts.listener.TTSButtonListener
import com.gvkorea.gvs1000_dsp.fragment.tts.listener.TTSSpeakListener
import com.gvkorea.gvs1000_dsp.fragment.tts.listener.TTSVolumeListener
import com.gvkorea.gvs1000_dsp.fragment.tts.presenter.STTPresenter
import com.gvkorea.gvs1000_dsp.fragment.tts.presenter.TTSPresenter
import kotlinx.android.synthetic.main.fragment_tts.*

class TTSFragment : Fragment() {

    private lateinit var pref: SharedPreferences
    private lateinit var audioManager: AudioManager
    private lateinit var presenterTTS: TTSPresenter
    private lateinit var presenterSTT: STTPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = this.activity!!.getSharedPreferences("pref_tts", Context.MODE_PRIVATE)
        audioManager = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        presenterTTS = TTSPresenter(this, audioManager, pref)
        presenterTTS.init_setup()
        presenterSTT = STTPresenter(this)
        presenterSTT.init_Setup()
        init_listener()

    }

    private fun init_listener() {
        btn_selectLang.setOnClickListener(TTSButtonListener(this, presenterTTS))
        btn_resetPitch.setOnClickListener(TTSButtonListener(this, presenterTTS))
        btn_resetRate.setOnClickListener(TTSButtonListener(this, presenterTTS))
        btn_text_clear.setOnClickListener(TTSButtonListener(this, presenterTTS))
        btn_text_save.setOnClickListener(TTSButtonListener(this, presenterTTS))
        btn_text_load.setOnClickListener(TTSButtonListener(this, presenterTTS))
        btn_TTS.setOnClickListener(TTSSpeakListener(this, presenterTTS))
        sb_ttsVolume.setOnSeekBarChangeListener(TTSVolumeListener(presenterTTS))
        btn_STT.setOnTouchListener(SSTTouchListener(this, presenterSTT))
    }

    override fun onDestroyView() {
        presenterTTS.savePreference()
        presenterTTS.tts.stop()
        super.onDestroyView()

    }


}
