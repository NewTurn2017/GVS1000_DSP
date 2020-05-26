package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.calib.presenter.CalibPresenter

class CalibListener(val presenter: CalibPresenter) : View.OnClickListener {
    override fun onClick(p0: View?) {

        when(p0?.id){
            R.id.btn_autoCalib -> presenter.autoCalib()
            R.id.btn_calibReset -> presenter.calibReset()
        }
    }
}