package com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.fragment.tune.fragment.reverb.presenter.ReverbPresenter

class ReverbButtonListener (val presenter: ReverbPresenter): View.OnClickListener{
    override fun onClick(p0: View?) {
        presenter.playClap()
    }

}