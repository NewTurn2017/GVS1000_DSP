package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.autotune.presenter.AutoTunePresenter

class TuneButtonListener(val presenter: AutoTunePresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_tune_start -> presenter.setTargetVolumeDialog()
            R.id.btn_tune_stop -> presenter.tuneStop()
            R.id.btn_showTable -> presenter.showTable()
            R.id.btn_showEQ -> presenter.showEQ()
        }
    }
}