package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.listener

import android.view.View
import android.widget.AdapterView
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.presenter.AutoTunePresenter

class TuneSpeakerItemSelectedListener(val presenter: AutoTunePresenter): AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        presenter.loadModel()
    }
}