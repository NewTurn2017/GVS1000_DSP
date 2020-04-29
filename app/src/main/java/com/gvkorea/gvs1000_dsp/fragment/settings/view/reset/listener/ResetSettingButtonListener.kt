package com.gvkorea.gvs1000_dsp.fragment.settings.view.reset.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.reset.presenter.ResetSettingPresenter
import kotlinx.android.synthetic.main.fragment_reset_setting.view.*

class ResetSettingButtonListener(val presenter: ResetSettingPresenter) : View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_resetSpeakerSelect -> presenter.showSelectReset()
            R.id.btn_resetSettingAll -> presenter.resetSettingsAll()
        }
    }
}