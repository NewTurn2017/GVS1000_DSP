package com.gvkorea.gvs1000_dsp.fragment.music.listener

import android.widget.SeekBar
import com.gvkorea.gvs1000_dsp.fragment.music.MusicFragment
import com.gvkorea.gvs1000_dsp.fragment.music.presenter.MusicPresenter
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService
import com.gvkorea.gvs1000_dsp.fragment.music.service.PlayerService.Companion.sCurrentPlayPosition

class MusicSeekBarListener(val view: MusicFragment, val presenter: MusicPresenter, val service: PlayerService?) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        if (service?.mediaPlayer != null){
            if(p2){
                sCurrentPlayPosition = p1
            }
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        if (service?.mediaPlayer != null){
            service.mediaPlayer?.seekTo(sCurrentPlayPosition)
        }
    }

}