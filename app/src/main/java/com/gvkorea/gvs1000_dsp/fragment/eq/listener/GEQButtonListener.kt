package com.gvkorea.gvs1000_dsp.fragment.eq.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.eq.presenter.GEQPresenter

class GEQButtonListener(val presenter: GEQPresenter) : View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_geqReset -> presenter.eqReset()
            R.id.btn_geqBypass -> presenter.geqAllBypass()
            R.id.btn_saveTuning -> presenter.savePreset()
            R.id.btn_loadTuning -> presenter.loadPreset()
        }
    }
}