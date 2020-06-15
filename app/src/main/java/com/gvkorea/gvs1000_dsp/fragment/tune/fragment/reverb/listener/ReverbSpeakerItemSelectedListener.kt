package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.listener

import android.view.View
import android.widget.AdapterView
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.presenter.ReverbPresenter

class ReverbSpeakerItemSelectedListener(val presenter: ReverbPresenter): AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        presenter.updateDisplay()
    }
}