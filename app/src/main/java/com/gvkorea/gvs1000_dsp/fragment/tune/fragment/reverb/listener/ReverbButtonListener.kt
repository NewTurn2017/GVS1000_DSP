package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.ReverbFragment.Companion.reverbCount
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.presenter.ReverbPresenter

class ReverbButtonListener (val presenter: ReverbPresenter): View.OnClickListener{
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_noiseClap -> {
                presenter.mainButtonDisable()
                presenter.buttonDisable()
                presenter.impulseButtonDisenable()
                reverbCount = 0
                presenter.noiseClap()
            }
            R.id.btn_testReset -> presenter.testReset()
        }
    }
}