package com.gvkorea.gvs1000_dsp.fragment.tune.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.presenter.TunePresenter
import com.gvkorea.gvs1000_dsp.util.GVSTunePanel

class TuneSelectPanelListener(val presenter: TunePresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_calibration -> presenter.selectPanel(GVSTunePanel.CALIB)
            R.id.btn_autoTune -> presenter.selectPanel(GVSTunePanel.AUTO_TUNE)
            R.id.btn_evalueation -> presenter.selectPanel(GVSTunePanel.EVAL)
        }
    }
}