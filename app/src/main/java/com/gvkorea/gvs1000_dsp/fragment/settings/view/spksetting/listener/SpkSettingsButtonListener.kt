package com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.view.spksetting.presenter.SpkSettingsPresenter
import kotlinx.android.synthetic.main.fragment_spk_settings.*

class SpkSettingsButtonListener(val presenter: SpkSettingsPresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_settingsReset -> presenter.resetSettings()
            R.id.btn_settingsSave -> presenter.saveSettings()
            R.id.btn_settingsComplete -> presenter.settingComplete()
        }

    }
}