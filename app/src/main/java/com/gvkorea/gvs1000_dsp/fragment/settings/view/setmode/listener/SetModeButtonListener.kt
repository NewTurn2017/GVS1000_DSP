package com.gvkorea.gvs1000_dsp.fragment.settings.view.setmode.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.setmode.presenter.SetModePresenter
import kotlinx.android.synthetic.main.fragment_set_mode.view.*

class SetModeButtonListener(val presenter: SetModePresenter) : View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_setModeSpeakerSelect -> presenter.showSelectMode()
            R.id.btn_saveMode -> presenter.applyModeAndHideSelect()
        }
    }
}