package com.gvkorea.gvs1000_dsp.fragment.eq.listener

import android.widget.SeekBar
import com.gvkorea.gvs1000_dsp.fragment.eq.GEQFragment
import com.gvkorea.gvs1000_dsp.fragment.eq.presenter.GEQPresenter

class GEQSeekBarChangeListener(
    val view: GEQFragment,
    val presenter: GEQPresenter
) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}