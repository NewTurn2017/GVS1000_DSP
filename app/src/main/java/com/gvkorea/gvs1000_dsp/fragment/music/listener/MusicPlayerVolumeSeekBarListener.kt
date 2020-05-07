package com.gvkorea.gvs1000_dsp.fragment.music.listener

import android.media.AudioManager
import android.widget.SeekBar
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment

class MusicPlayerVolumeSeekBarListener(val view: MusicFragment, private val am: AudioManager) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

}