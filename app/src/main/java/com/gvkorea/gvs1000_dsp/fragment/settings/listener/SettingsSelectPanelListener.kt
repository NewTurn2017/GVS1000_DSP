package com.gvkorea.gvs1000_dsp.fragment.settings.listener

import android.view.View
import com.gvkorea.gvs1000_dsp.R
import com.gvkorea.gvs1000_dsp.fragment.settings.util.GVSSettingPanel
import com.gvkorea.gvs1000_dsp.fragment.settings.presenter.SettingsPresenter

class SettingsSelectPanelListener(val presenter: SettingsPresenter): View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_ID_setting -> presenter.selectPanel(GVSSettingPanel.ID_SETTING)
            R.id.btn_resetSettings -> presenter.selectPanel(GVSSettingPanel.RESET_SETTINGS)
            R.id.btn_setMode -> presenter.selectPanel(GVSSettingPanel.SET_MODE)
            R.id.btn_sync -> presenter.selectPanel(GVSSettingPanel.SYNC)
            R.id.btn_spk_setting -> presenter.selectPanel(GVSSettingPanel.SPK_SETTING)
            R.id.btn_preset -> presenter.selectPanel(GVSSettingPanel.PRESET)
        }
    }
}