package com.gvkorea.gvs1000_dsp.fragment.tts.listener

import android.media.AudioManager
import android.widget.SeekBar
import com.gvkorea.gvs1000_dsp.fragment.tts.presenter.TTSPresenter

class TTSVolumeListener(val presenter: TTSPresenter) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        presenter.am.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

}