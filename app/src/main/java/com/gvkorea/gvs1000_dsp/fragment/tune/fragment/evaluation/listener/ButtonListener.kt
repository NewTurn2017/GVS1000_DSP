package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.evaluation.presenter.EvalPresenter

class ButtonListener(val presenter: EvalPresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_noiseOn -> presenter.noise(presenter.PINK)
            R.id.btn_noiseOff -> presenter.noise(presenter.NOISE_OFF)
            R.id.btn_measureAvg -> presenter.average()
            R.id.btn_save -> presenter.CSV_SaveForData()
        }
    }
}